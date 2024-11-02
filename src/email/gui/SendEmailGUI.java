package email.gui;

import javax.swing.*;
import java.awt.Color;
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
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the frame

        recipientField = new JTextField(15);
        subjectField = new JTextField(15);
        messageArea = new JTextArea(10, 25);
        sendButton = new JButton("Send");
        attachButton = new JButton("Attach File");
        fileField = new JTextField(15);
        fileField.setEditable(false);
        statusLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED); // Set error message color

        // Set colors for the components
        recipientField.setBackground(new Color(210, 210, 210)); // Light gray
        subjectField.setBackground(new Color(210, 210, 210)); // Light gray
        messageArea.setBackground(new Color(210, 210, 210)); // Light gray
        fileField.setBackground(new Color(210, 210, 210)); // Light gray

        sendButton.setBackground(new Color(70, 130, 180)); // Steel blue
        sendButton.setForeground(Color.WHITE); // White text
        attachButton.setBackground(new Color(70, 130, 180)); // Steel blue
        attachButton.setForeground(Color.WHITE); // White text

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
                emailService.Quit();
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
        attachedFile = null; // Reset attached file
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
