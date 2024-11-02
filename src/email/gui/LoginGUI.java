package email.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import email.smtp.Client;

public class LoginGUI {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginGUI() {
        initComponents(); // Call component initialization method
    }

    public void display() {
        frame.setVisible(true);
    }

    private void initComponents() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setResizable(false); // Make the frame non-resizable

        emailField = new JTextField(8); // Width reduced for input field
        passwordField = new JPasswordField(8); // Width reduced for input field
        loginButton = new JButton("Login");
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED); // Set error message color

        // Set component styles
        emailField.setBackground(new Color(210, 210, 210)); // Light grey for input fields
        passwordField.setBackground(new Color(210, 210, 210)); // Light grey for input fields
        emailField.setForeground(Color.BLACK); // Input text color
        passwordField.setForeground(Color.BLACK); // Input text color
        emailField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set button styles
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginButtonClick();
            }
        });

        setLayout(); // Set layout
    }

    private void onLoginButtonClick() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("이메일과 비밀번호를 입력하세요.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("잘못된 이메일 형식입니다. 예: johndoe@domain.com");
            return;
        }

        Client client = new Client(email, password);

        if (!client.mailService.isCorrectAddress()) {
            showError("지원되지 않는 이메일 도메인입니다. @naver.com을 사용하세요.");
            return;
        }

        try {
            client.mailService.connect();
            JOptionPane.showMessageDialog(frame, "로그인 성공!");
            clearFields();
            frame.dispose();

            SendEmailGUI sendEmailGUI = new SendEmailGUI(client);
            sendEmailGUI.display();
        } catch (IOException | HeadlessException e) {
            showError("오류가 발생했습니다. 다시 시도하세요.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        JOptionPane.showMessageDialog(frame, message, "오류", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    private void setLayout() {
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50)); // Dark background for the panel

        // Add top margin
        panel.add(Box.createVerticalStrut(10));
        
        // Add components with spacing
        panel.add(createLabelField("이메일:", emailField));
        panel.add(Box.createVerticalStrut(25)); // Add spacing between input fields
        panel.add(createLabelField("비밀번호:", passwordField));
        panel.add(Box.createVerticalStrut(25)); // Add more spacing after the password field
        
        // Set login button panel spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(70, 70, 70)); // Dark background for the button panel
        buttonPanel.add(loginButton);
        panel.add(buttonPanel);

        // Add error message with margin
        panel.add(Box.createVerticalStrut(5));
        panel.add(errorLabel);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JPanel createLabelField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 70, 70)); // Set label field background to dark
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.WHITE); // Set label text color to white
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return panel;
    }
}
