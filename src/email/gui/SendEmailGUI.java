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

    public SendEmailGUI(Client client) {
        this.client = client;
        this.fileHandler = new FileHandler();
        initComponents();
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
        recipientField.setForeground(Color.WHITE);
        subjectField.setForeground(Color.WHITE);
        messageArea.setForeground(Color.WHITE);
        fileField.setForeground(Color.WHITE);
       
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

        if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showError("이메일 주소, 제목, 메시지를 모두 입력하세요.");
            return;
        }

        try {
            EmailServiceWithNaver emailService = client.mailService;
            if (emailService.isCorrectAddress()) {
                emailService.connect();
                for (String mailRec : recipients) {
                    if (attachedFile != null && attachedFile.exists()) {
                        // If an attachment exists
                        File[] attachments = {attachedFile};
                        emailService.sendEmail(mailRec.trim(), subject, message, attachments); // Send email with attachment
                    } else {
                        // If no attachment
                        emailService.sendEmail(mailRec.trim(), subject, message, null); // Send email without attachment
                    }
                }
                // Show success message
                JOptionPane.showMessageDialog(frame, "이메일이 성공적으로 전송되었습니다.", "전송 완료", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("이메일이 성공적으로 전송되었습니다.");
                clearFields();
            }
        } catch (IOException e) {
            showError("전송 오류: " + e.getMessage());
        }
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
