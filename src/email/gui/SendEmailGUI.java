package email.gui;

import javax.swing.*; 
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
    private JTextField fileField;
    private JLabel statusLabel;
    private JLabel errorLabel;
    private File attachedFile;
    private Client client;
    private FileHandler fileHandler; // 파일 처리 핸들러

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
        frame.setSize(400, 400);

        recipientField = new JTextField(15);
        subjectField = new JTextField(15);
        messageArea = new JTextArea(10, 25);
        sendButton = new JButton("Send");
        attachButton = new JButton("Attach File");
        fileField = new JTextField(15);
        fileField.setEditable(false);
        statusLabel = new JLabel();
        errorLabel = new JLabel();

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

        setLayout();
    }

    private void onSendButtonClick() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showError("이메일 주소, 제목, 메시지를 모두 입력하세요.");
            return;
        }

        try {
            EmailServiceWithNaver emailService = client.mailService;

            if (emailService.isCorrectAddress()) {
                emailService.connect();
                if (attachedFile != null && attachedFile.exists()) {
                    // 첨부파일이 있는 경우
                    File[] attachments = { attachedFile }; // 첨부파일 배열
                    emailService.sendEmail(recipient, subject, message, attachments); // MIME 이메일 전송
                } else {
                    // 첨부파일이 없는 경우
                    emailService.sendEmail(recipient, subject, message, null); // 첨부파일 없이 전송
                }

                System.out.println("이메일이 성공적으로 전송되었습니다.");
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
        attachedFile = null; // 필드를 null로 초기화
    }

    private void setLayout() {
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("To:"));
        panel.add(recipientField);
        panel.add(new JLabel("Subject:"));
        panel.add(subjectField);
        panel.add(new JLabel("Message:"));
        panel.add(new JScrollPane(messageArea));
        panel.add(attachButton);
        panel.add(fileField);
        panel.add(statusLabel);
        panel.add(errorLabel);
        panel.add(sendButton);
    }
}
