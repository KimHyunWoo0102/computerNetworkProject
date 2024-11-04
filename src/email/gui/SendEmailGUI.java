package email.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.*;

import email.smtp.Client;
import email.smtp.EmailServiceWithNaver;
import email.mime.FileHandler;

public class SendEmailGUI {
    private JFrame frame;
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton attachButton;
    private JButton quitButton;	// quit 버튼 추가 
    private JTextField fileField;
    private JLabel statusLabel;
    private JLabel errorLabel;
    private File attachedFile;
    private Client client;
    private FileHandler fileHandler; // File handling utility
    private ScheduledExecutorService noopScheduler;

    public SendEmailGUI(Client client) {
        this.client = client;
        this.fileHandler = new FileHandler();
        initComponents();
        startNoopScheduler();
    }

    public void display() {
        frame.setVisible(true);
    }

    private void initComponents() {
        frame = new JFrame("메일 전송");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450); // 창 크기 설정
        frame.setLocationRelativeTo(null); // Center the frame

        recipientField = new JTextField(15);
        subjectField = new JTextField(15);
        messageArea = new JTextArea(10, 25);
        sendButton = new JButton("Send");
        attachButton = new JButton("Attach File");
        quitButton = new JButton("QUIT");
        fileField = new JTextField(15);
        fileField.setEditable(false);
        statusLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED); // Set error message color

        // Set colors for the components
        recipientField.setForeground(new Color(56, 62, 88));
        recipientField.setBackground(new Color(179, 182, 197));
        subjectField.setForeground(new Color(56, 62, 88));
        subjectField.setBackground(new Color(179, 182, 197));
        messageArea.setForeground(new Color(56, 62, 88));
        messageArea.setBackground(new Color(179, 182, 197));
        fileField.setForeground(new Color(56, 62, 88));
        fileField.setBackground(new Color(179, 182, 197));
       
        attachButton.setBackground(new Color(204, 122, 136));	// 배경색 지정
        attachButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(204, 122, 136));	// 배경색 지정
        sendButton.setForeground(Color.WHITE); // White text
        quitButton.setBackground(new Color(225, 188, 181));	// 배경색 지정
        quitButton.setForeground(new Color(56, 62, 88));	// 글자 색 지정 

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSendButtonClick();
            }
        });

        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAttachButtonClick();
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onQuitButtonClick();
            }
        });

        setLayout();
    }
    
    private void onSendButtonClick() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();
        String[] recipients = recipient.split(",");
        int maxRetries = 3; // 최대 재시도 횟수
        int retryCount = 0;
        boolean isSent = false;

        if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showError("이메일 주소, 제목, 메시지를 모두 입력하세요.");
            return;
        }
        
        // 이메일 형식 유효성 검사
        if (!isValidEmailFormat(recipients)) {
            showError("유효한 이메일 주소를 입력하세요.");
            return; // 유효하지 않은 경우 전송 시도하지 않음
        }

        while (retryCount < maxRetries && !isSent) {
            try {
                EmailServiceWithNaver emailService = client.mailService;
                if (emailService.isCorrectAddress()) {
                    emailService.connect();
                    for (String mailRec : recipients) {
                        if (attachedFile != null && attachedFile.exists()) {
                            File[] attachments = {attachedFile};
                            emailService.sendEmail(mailRec.trim(), subject, message, attachments); // 첨부 파일 있는 경우
                        } else {
                            emailService.sendEmail(mailRec.trim(), subject, message, null); // 첨부 파일 없는 경우
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "이메일이 성공적으로 전송되었습니다.", "전송 완료", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("이메일이 성공적으로 전송되었습니다.");
                    clearFields();
                    isSent = true; // 전송 성공 시 true로 설정
                }
            } catch (IOException e) {
                retryCount++; // 실패 시 재시도 횟수 증가
                showError("전송 오류: " + e.getMessage() + " 재시도 중... (" + retryCount + "/" + maxRetries + ")");
            }
        }

        if (!isSent) {
            showError("이메일 전송 실패: 최대 재시도 횟수를 초과했습니다.");
        }
    }
    
    // 이메일 형식 유효성 검사 메서드
    private boolean isValidEmailFormat(String[] emails) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // 이메일 형식 정규식
        for (String email : emails) {
            if (!email.trim().matches(emailRegex)) {
                return false; // 이메일 형식이 유효하지 않으면 false 반환
            }
        }
        return true;
    }


    private void onAttachButtonClick() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (fileHandler.isValidFileType(selectedFile)) {
                attachedFile = selectedFile;
                fileField.setText(attachedFile.getAbsolutePath());
                statusLabel.setText("첨부 파일: " + attachedFile.getName());
            } else {
                showError("유효하지 않은 파일 형식입니다.");
            }
        }
    }
    
    private void onQuitButtonClick() {
        try {
            EmailServiceWithNaver emailService = client.mailService;
            
            stopNoopScheduler();
            // 서버에 QUIT 명령을 보내고 연결 종료
            emailService.Quit();

            // 종료 메시지 표시
            JOptionPane.showMessageDialog(frame, "연결이 종료되었습니다.", "종료", JOptionPane.INFORMATION_MESSAGE);

            // 프로그램 종료
            frame.dispose();
        } catch (IOException e) {
            showError("연결 종료 오류: " + e.getMessage());
        }
    }
    
    
    // 주기적으로 NOOP 명령을 보내기 위한 스케줄러 설정
    private void startNoopScheduler() {
        if (noopScheduler != null && !noopScheduler.isShutdown()) {
            System.out.println("NOOP 스케줄러가 이미 실행 중입니다.");
            return;
        }
        noopScheduler = Executors.newSingleThreadScheduledExecutor();
        noopScheduler.scheduleAtFixedRate(() -> {
            try {
                client.mailService.sendNoop(); // NOOP 명령 전송
                System.out.println("NOOP 명령이 전송되었습니다.");
            } catch (IOException e) {
                showError("NOOP 명령 전송 오류: " + e.getMessage());
                stopNoopScheduler(); // 오류 발생 시 스케줄러 중지
            }
        }, 0, 9, TimeUnit.SECONDS); // 9초마다 NOOP 전송
    }


    // 스케줄러 중지
    private void stopNoopScheduler() {
        if (noopScheduler != null && !noopScheduler.isShutdown()) {
            noopScheduler.shutdown();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        JOptionPane.showMessageDialog(frame, message, "오류", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        recipientField.setText("");
        subjectField.setText("");
        messageArea.setText("");
        fileField.setText("");
        statusLabel.setText("");
        errorLabel.setText("");
        attachedFile = null; // Reset attached file
    }

    private void setLayout() {
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(56,62,88));	// 배경 색 설정 
        panel.setBorder(new EmptyBorder(40,50,10,50));

        
        JLabel toLabel = new JLabel("To.");
        Font currentFont = toLabel.getFont();
        toLabel.setFont(currentFont.deriveFont(Font.BOLD,13f)); // 크기를 13으로 설정
        toLabel.setForeground(Color.white); // 글씨 색상 설정 
        toLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT); // 왼쪽 정렬
        panel.add(toLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));	// 여백 
        
        panel.add(recipientField);
        panel.add(Box.createRigidArea(new Dimension(0, 9)));	// 여백
        
        JLabel subjectLabel = new JLabel("Subject.");
        subjectLabel.setFont(currentFont.deriveFont(Font.BOLD,13f)); // 크기를 13으로 설정
        subjectLabel.setForeground(Color.white); // 글씨 색상 설정
        subjectLabel.setHorizontalAlignment(SwingConstants.LEFT);	// 왼쪽 정렬
        panel.add(subjectLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));	// 여백
        
        panel.add(subjectField);
        panel.add(Box.createRigidArea(new Dimension(0, 9)));	// 여백
        
        JLabel messageLabel = new JLabel("Message.");
        messageLabel.setFont(currentFont.deriveFont(Font.BOLD,13f)); // 크기를 13으로 설정
        messageLabel.setForeground(Color.white); // 글씨 색상 설정 
        panel.add(messageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));	// 여백
        
        panel.add(new JScrollPane(messageArea));
        
        panel.add(Box.createRigidArea(new Dimension(0, 9))); 
        
        panel.add(attachButton); // 파일 첨부 버튼 
        
        panel.add(Box.createRigidArea(new Dimension(0, 9))); // 여백 추가
        
        panel.add(fileField);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // 여백 추가
        
        panel.add(statusLabel);
        panel.add(errorLabel);
        
        // 버튼 패널 생성 및 FlowLayout 설정
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 버튼 패널 생성
        buttonPanel.setBackground(new Color(56,62,88));	// 배경 색 설정
        buttonPanel.add(sendButton); // sendButton 추가
        buttonPanel.add(quitButton); // quitButton 추가
        buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT); // 왼쪽 정렬
        
        panel.add(buttonPanel); // 버튼 패널을 메인 패널에 추가
        

    }
}
