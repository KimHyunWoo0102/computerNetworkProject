<div align="center">
<h2>SMTP Email System 📧</h2>
사용자가 이메일을 송신할 수 있는 'SMTP 이메일 시스템'입니다.
</div>

## 🖥 프로젝트 개요
 - Java를 기반으로 구축
 - 사용자 인증, 이메일 전송 및 파일 첨부 기능

### 📅 개발 기간
2024.10.12 - 2024.11.06

### 🧑‍🤝‍🧑 구성원
 - Team Leader 1: 김현우
 - Team Leader 2: 박민수
 - Member 3: 홍지민
 - Member 4: 이서현
 - Member 5: 신예린

### ⚙️ 개발환경
 - Java Version: `JDK 22`
 - IDE: `Eclips 4.33.0`

## 📝 프로젝트 구성

### 패키지 구조
```
src
 └── email.gui
      ├── LoginGUI.java
      └── SendEmailGUI.java
 └── email.main
      └── EmailApp.java
 └── email.mime
      ├── FileHandler.java
      └── MimeMessageBuilder.java
 └── email.smtp
      ├── Client.java
      ├── EmailServiceWithNaver.java
      └── EmailWithNaver.java
```

## 📌 주요 기능
1. **로그인** 
   - 사용자는 `LoginGUI`에서 이메일과 비밀번호를 입력한 후, Login 버튼을 누릅니다.
   - 입력된 이메일은 객체에 전달되고, `LoginGUI`는 입력된 이메일이 올바른 이메일 주소인지 확인합니다.
   - 이메일 주소가 유효하고, 연결에 성공하면 `SendEmailGUI`가 실행됩니다.

2. **이메일 전송** 
   - `SendEmailGUI`에서 사용자는 수신자, 제목, 메시지를 입력합니다. Attach File 버튼을 눌러 첨부할 파일을 선택할 수 있습니다.
   - 사용자가 Send 버튼을 클릭하면 `Client` 클래스가 호출되어, 입력된 메시지와 파일에 대한 Base64 인코딩 등의 처리가 수행됩니다.
   - 수신자, 제목, 메시지가 올바른지 검증한 후, 선택된 파일과 함께 최종적으로 이메일이 전송됩니다.
   - Quit 버튼 클릭 시, SMTP 서버 연결을 종료하고 프로그램이 종료됩니다.

3. **파일 첨부**
   - `FileHandler`에서 파일이 유효한지, MIME 타입이 유효한지 검사합니다. (유효한 파일 형식: 이미지 파일, PDF, 텍스트 파일)
   - 조건이 충족되면 파일이 첨부되었다는 메시지를 출력하고, 그렇지 않으면 오류 메시지를 출력합니다.
   - 파일이 존재하지 않으면 파일이 없다는 메시지를 출력합니다.
   

## 🔎 패키지 상세 설명

### 1. `email.gui`
<details>
<summary> LoginGUI.java </summary> 

  ```javascript
  package email.gui;

import javax.swing.*; // Swing 컴포넌트 사용을 위한 import
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.ActionEvent; // ActionEvent 사용을 위한 import
import java.awt.event.ActionListener; // ActionListener 인터페이스 사용을 위한 import
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;

import email.smtp.*; // 클래스 import

public class LoginGUI {
    private JFrame frame; // 로그인 창을 위한 JFrame
    private JTextField emailField; // 이메일 입력 필드를 위한 JTextField
    private JPasswordField passwordField; // 비밀번호 입력 필드를 위한 JPasswordField
    private JButton loginButton; // 로그인 버튼을 위한 JButton
    private JLabel errorLabel; // 오류 메시지를 표시할 JLabel
    

    // LoginGUI 생성자
    public LoginGUI() {
        initComponents(); // 컴포넌트 초기화 메소드 호출
    }

    // 로그인 화면을 표시하는 메소드
    public void display() {
        frame.setVisible(true); // 프레임을 보이도록 설정
    }

    // GUI 컴포넌트를 초기화하는 메소드
    private void initComponents() {
        frame = new JFrame("로그인"); // 창 이름 변경
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 동작 설정
        frame.setSize(700, 450); // 창 크기 설정
        frame.setLocationRelativeTo(null);	// 가운데 정렬 
        
        // E-mail 필드 생성
        emailField = createHintTextField("이메일을 입력하세요", 1, new Color(179, 182, 197), new Color(255,255,255)); // 힌트 텍스트가 있는 JTextField 생성
        passwordField = createHintPasswordField("비밀번호를 입력하세요", 1, new Color(179, 182, 197), new Color(255,255,255)); 
        
        loginButton = new JButton("Login"); // 로그인 버튼 생성
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);	// 중앙 정렬
        loginButton.setVerticalAlignment(SwingConstants.CENTER);	// 중앙 정렬
        
        // 버튼 배경색과 글씨 색상 설정
        loginButton.setBackground(new Color(204, 122, 136));	// 배경색 지정 
        loginButton.setForeground(Color.WHITE); // 글씨색 (흰색)
        
        errorLabel = new JLabel(); // 오류 메시지를 표시할 레이블 생성

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginButtonClick(); // 로그인 버튼 클릭 시 호출되는 메소드
            }
        });
        
        // 버튼 주위에 공백 추가
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            loginButton.getBorder(),
            BorderFactory.createEmptyBorder(10, 80, 10, 80) // 위, 왼쪽, 아래, 오른쪽 공백 설정
        ));

        setLayout(); // 레이아웃 설정
    }

    // 로그인 버튼 클릭 시 호출되는 메소드
    private void onLoginButtonClick() {
        String email = emailField.getText(); // 이메일 입력값 가져오기
        String password = new String(passwordField.getPassword()); // 비밀번호 입력값 가져오기

        // 이메일과 비밀번호가 비어 있는지 확인
        if (email.isEmpty() || password.isEmpty()) {
            showError("이메일 또는 비밀번호를 입력하세요.");
            return;
        }

        // 이메일 형식 검증
        if (!isValidEmail(email)) {
            showError("이메일 주소 형식이 올바르지 않습니다. \n예시: 홍길동@naver.com 꼴로 입력해주세요.");
            return; // 유효하지 않은 경우 반환
        }

        System.out.println("이메일 : " + email + " 비밀번호 : " + password + " 입력완료!");
        // Client 인스턴스 생성
        Client client = new Client(email, password);

        // 이메일 도메인 검증
        if (!client.mailService.isCorrectAddress()) {
            showError("지원하지 않는 이메일 도메인입니다. @naver.com을 사용하세요.");
            return; // 유효하지 않은 경우 반환
        }

        // 인증 시도
        try {
        		client.mailService.connect();
			    // 인증 시도
			    JOptionPane.showMessageDialog(frame, "로그인 성공!"); // 성공 메시지 표시
			    clearFields(); // 필드 초기화
			    frame.dispose(); // 로그인 창 닫기

			    // SendEmailGUI 인스턴스 생성 및 표시
			    SendEmailGUI sendEmailGUI = new SendEmailGUI(client); // SendEmailGUI 생성
			    sendEmailGUI.display(); // SendEmailGUI 표시
		} catch (IOException e) {
	        showError(e.getMessage()); // 예외 메시지를 사용자에게 표시
	    } catch (HeadlessException e) {
	        e.printStackTrace(); // 오류 스택 트레이스 출력
	        showError("오류가 발생했습니다. 다시 시도해주세요."); // 일반 오류 메시지 표시
	    }
    }

    // 이메일 주소의 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$"; // 이메일 정규 표현식
        return email.matches(emailRegex);
    }

    // 오류 메시지를 팝업으로 보여주는 메소드
    private void showError(String message) {
        errorLabel.setText(message); // 오류 메시지를 설정
        JOptionPane.showMessageDialog(frame, message, "오류", JOptionPane.ERROR_MESSAGE); // 팝업으로 오류 메시지 표시
    }

    // 입력 필드를 초기화하는 메소드
    private void clearFields() {
        emailField.setText(""); // 이메일 필드 초기화
        passwordField.setText(""); // 비밀번호 필드 초기화
    }
    
 
    
    // 힌트 텍스트가 있는 JTextField 생성 메소드
    private JTextField createHintTextField(String hint, int columns, Color backgroundColor, Color foregroundColor) {
        JTextField textField = new JTextField(columns); // JTextField 생성
        textField.setBackground(backgroundColor); // 배경 색상 설정
        textField.setForeground(foregroundColor); // 글씨 색상 설정
        

        // 힌트 텍스트 초기화
        textField.setText(hint);
        textField.setForeground(Color.GRAY); // 힌트 색상 설정

        // 포커스 리스너 추가
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 필드가 포커스를 받을 때 힌트 텍스트 지우기
                if (textField.getText().equals(hint)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK); // 글씨 색상 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 필드가 포커스를 잃을 때 힌트 텍스트가 비어있으면 다시 설정
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY); // 힌트 색상 설정
                    textField.setText(hint);
                }
            }
        });
        
        return textField; // 설정된 JTextField 반환
    }
    
    // 힌트 텍스트가 있는 JPasswordField 생성 메소드
    private JPasswordField createHintPasswordField(String hint, int columns, Color backgroundColor, Color foregroundColor) {
        JPasswordField passwordField = new JPasswordField(columns); // JPasswordField 생성
        passwordField.setBackground(backgroundColor); // 배경 색상 설정
        passwordField.setForeground(foregroundColor); // 글씨 색상 설정
        

        // 힌트 텍스트 초기화
        passwordField.setText(hint);
        passwordField.setForeground(Color.GRAY); // 힌트 색상 설정

        // 포커스 리스너 추가
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // 필드가 포커스를 받을 때 힌트 텍스트 지우기
                if (new String(passwordField.getPassword()).equals(hint)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK); // 글씨 색상 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 필드가 포커스를 잃을 때 힌트 텍스트가 비어있으면 다시 설정
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY); // 힌트 색상 설정
                    passwordField.setText(hint);
                }
            }
        });

        return passwordField; // 설정된 JPasswordField 반환
    }


    // 레이아웃을 설정하는 메소드
    private void setLayout() {
        JPanel panel = new JPanel(); // 패널 생성
        frame.add(panel); // 패널을 프레임에 추가
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 수직 레이아웃 설정
        panel.setBackground(new Color(56,62,88));	// 배경 색 설정 
        panel.setBorder(new EmptyBorder(40,50,10,50));	// 패딩  
        
    
        // 상단 멘트 
        JLabel label_0 = new JLabel("Welcome!");
        Font currentFont = label_0.getFont(); 
        label_0.setFont(currentFont.deriveFont(36f)); // 크기를 36으로 설정
        label_0.setForeground(Color.white); // 글씨 색상 설정 
        panel.add(label_0);
        label_0.setBorder(new EmptyBorder(0,0,10,0));	// 아래 간격 10
        
        JLabel label_1 = new JLabel("This is Email Sending Program!"); 
        label_1.setFont(currentFont.deriveFont(20f)); // 크기를 20으로 설정
        label_1.setForeground(new Color(177, 181, 193)); // 글씨 색상 설정 
        panel.add(label_1);
        label_1.setBorder(new EmptyBorder(0,0,30,0));	// 아래 간격 40 
        
        JLabel label_2 = new JLabel("Please enter your E-mail address and password."); 
        label_2.setFont(currentFont.deriveFont(14f)); // 크기를 14으로 설정
        label_2.setForeground(new Color(204, 122, 136)); // 글씨 색상 설정 
        panel.add(label_2);
        label_2.setBorder(new EmptyBorder(0,0,9,0));	// 아래 간격 9

        // 이메일 레이블 추가
        JLabel emailLabel = new JLabel("E-MAIL"); 
        emailLabel.setFont(currentFont.deriveFont(Font.BOLD,20f)); // 크기를 20으로 설정
        emailLabel.setForeground(Color.white); // 글씨 색상 설정 
        panel.add(emailLabel);
        emailLabel.setBorder(new EmptyBorder(0,0,11,0));	// 아래 간격 11 
        
        
        panel.add(emailField); // 이메일 입력 필드 추가
        
        
        // 이메일 레이블 추가
        JLabel passwordLabel = new JLabel("PASSWORD"); 
        passwordLabel.setFont(currentFont.deriveFont(Font.BOLD,20f)); // 크기를 20으로 설정
        passwordLabel.setForeground(Color.white); // 글씨 색상 설정 
        panel.add(passwordLabel);
        passwordLabel.setBorder(new EmptyBorder(21,0,11,0));	// 아래 간격 11 
        
        
        panel.add(passwordField); // 비밀번호 입력 필드 추가
        
        // 버튼 패널 생성 및 FlowLayout 설정
        JPanel buttonPanel = new JPanel(new FlowLayout()); // 버튼 패널 생성
        buttonPanel.setBackground(new Color(56,62,88));	// 배경 색 설정
        buttonPanel.add(loginButton); // sendButton 추가
        
        buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT); // 왼쪽 정렬
        
        panel.add(buttonPanel); // 버튼 패널을 메인 패널에 추가
  
        panel.add(Box.createRigidArea(new Dimension(0, 20)));	// 여백
        //panel.add(loginButton); // 버튼 추가
        panel.add(Box.createRigidArea(new Dimension(0, 1)));	// 여백
        
        errorLabel.setForeground(new Color(204,122,136)); // 글씨 색상 설정
        panel.add(errorLabel); // 오류 메시지 레이블 추가

    }
}
  
  ```
  </details>

<details>
<summary> SendEmailGUI.java </summary> 

  ```javascript
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
        noopScheduler = Executors.newSingleThreadScheduledExecutor();
        noopScheduler.scheduleAtFixedRate(() -> {
            try {
                client.mailService.sendNoop(); // NOOP 명령 전송
            } catch (IOException e) {
                showError("NOOP 명령 전송 오류: " + e.getMessage());
                stopNoopScheduler(); // 오류 발생 시 스케줄러 중지
            }
        }, 0, 9, TimeUnit.SECONDS); // 5초마다 NOOP 전송
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

  ```
  </details>
 
- `LoginGUI.java`: 사용자 로그인 인터페이스
- `SendEmailGUI.java`: 이메일 전송 인터페이스

### 2. `email.main`
<details>
<summary> EmailApp.java </summary> 
 
```javascript
package email.main;

import email.gui.LoginGUI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class EmailApp {
    // 프로그램 시작 메소드
    public static void main(String[] args) {
        // Look and Feel 설정
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginGUI loginGUI = new LoginGUI(); // LoginGUI 인스턴스 생성
        loginGUI.display(); // 로그인 화면 표시
    }
}

```
</details>

- `EmailApp.java`: 이메일 애플리케이션의 메인 엔트리 포인트 정의, 이메일 로그인 GUI를 실행
  
### 3. `email.mime`
<details>
<summary> FileHandler.java </summary> 

  ```javascript
 package email.mime;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class FileHandler {

    public void attachFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (isValidFileType(file)) {
                System.out.println(file.getName() + " 파일이 첨부되었습니다.");
            } else {
                System.out.println("유효하지 않은 파일입니다.");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    public String getMimeType(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            System.err.println("MIME 타입 탐지 오류: " + e.getMessage());
            return "알 수 없는 MIME 타입";
        }
    }

    public boolean isValidFileType(File file) {
        String mimeType = getMimeType(file.getAbsolutePath());
        return mimeType.startsWith("image") || mimeType.equals("application/pdf") || mimeType.startsWith("text");
    }

    public long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        } else {
            System.out.println("파일을 찾을 수 없습니다.");
            return 0;
        }
    }

    public void saveFileToDisk(File file, String destinationDirectory) {
        Path destinationPath = Paths.get(destinationDirectory, file.getName());
        try {
            Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("파일 저장 오류: " + e.getMessage());
        }
    }

    public String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
        return contentBuilder.toString();
    }
}

  ```
  </details>
  
<details>
<summary> MimeMessageBuilder.java </summary> 
 
  ```javascript
package email.mime;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class MimeMessageBuilder {
    private String boundary;
    private String from;
    private String to;
    private String subject;
    private String body;
    private File[] attachments;

    public MimeMessageBuilder(String from, String to, String subject, String body, File[] attachments) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
        this.boundary = "----=_MIME_BOUNDARY_" + System.currentTimeMillis();
    }

    // From 헤더
    private String createFromHeader() {
        return "From: " + from + "\r\n";
    }

    // To 헤더
    private String createToHeader() {
        return "To: " + to + "\r\n";
    }

    // Subject 헤더
    private String createSubjectHeader() {
        return "Subject: " + subject + "\r\n";
    }

    // MIME 본문
    private String createMimeBody() {
        StringBuilder mimeBody = new StringBuilder();
        mimeBody.append("Content-Type: text/plain; charset=UTF-8\r\n");
        mimeBody.append("Content-Transfer-Encoding: 7bit\r\n");
        mimeBody.append("\r\n");
        mimeBody.append(body).append("\r\n");
        return mimeBody.toString();
    }

    // 첨부 파일을 MIME 파트로 인코딩
    private String createMimeAttachment(File file) throws IOException {
        StringBuilder mimeAttachment = new StringBuilder();
        mimeAttachment.append("Content-Type: application/octet-stream; name=\"").append(file.getName()).append("\"\r\n");
        mimeAttachment.append("Content-Transfer-Encoding: base64\r\n");
        mimeAttachment.append("Content-Disposition: attachment; filename=\"").append(file.getName()).append("\"\r\n");
        mimeAttachment.append("\r\n");
        mimeAttachment.append(encodeFileToBase64(file)).append("\r\n");
        return mimeAttachment.toString();
    }

    // Boundary 추가
    private String createMimeBoundary() {
        return "--" + boundary + "\r\n";
    }

    // Base64 인코딩
    private String encodeFileToBase64(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    // 최종 MIME 메시지 생성
    public String build() throws IOException {
        StringBuilder mimeMessage = new StringBuilder();

        mimeMessage.append(createFromHeader());
        mimeMessage.append(createToHeader());
        mimeMessage.append(createSubjectHeader());
        mimeMessage.append("MIME-Version: 1.0\r\n");
        mimeMessage.append("Content-Type: multipart/mixed; boundary=\"").append(boundary).append("\"\r\n");
        mimeMessage.append("\r\n");

        // 본문 추가
        mimeMessage.append(createMimeBoundary());
        mimeMessage.append(createMimeBody());

        // 첨부 파일 추가
        if (attachments != null) {
            for (File file : attachments) {
                mimeMessage.append(createMimeBoundary());
                mimeMessage.append(createMimeAttachment(file));
            }
        }

        // 메시지 종료
        mimeMessage.append("--").append(boundary).append("--\r\n");

        return mimeMessage.toString();
    }
}
 
  ```
  </details>
  
- `FileHandler.java`: 파일 첨부, 첨부된 파일의 유효성 검사
- `MimeMessageBuilder.java`: 헤더 생성, 이메일 본문과 첨부 파일을 MIME 형식으로 구성, 완성된 MIME 메시지를 문자열 형태로 반환
  
### 4. `email.smtp`
<details>
<summary> Client.java </summary> 
 
```javascript
package email.smtp;

public class Client {
    
    public EmailServiceWithNaver mailService;
    
    public Client(String mail, String password) {
    	if (mail.endsWith("@naver.com")) {
    		mailService=new EmailServiceWithNaver(mail,password,"smtp.naver.com",465);
          
        }else
        	mailService=new EmailServiceWithNaver(mail,password,null,-1);
        }
}

```
</details>

<details>
<summary> EmailServiceWithNaver.java </summary> 
 
```javascript
package email.smtp;

import email.mime.MimeMessageBuilder;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.*;

public class EmailServiceWithNaver {
    protected SSLSocketFactory factory;
    protected SSLSocket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected String mail, password, smtpServer;
    protected int port;

    // List to store requests and responses
    protected List<String> responses;

    public EmailServiceWithNaver(String mail, String password, String smtpServer, int port) {
        this.mail = mail;
        this.password = password;
        this.smtpServer = smtpServer;
        this.port = port;
        this.responses = new ArrayList<>(); // Initialize response list
    }

    // Check if a valid domain name has been entered
    public boolean isCorrectAddress() {
        return port != -1;
    }

    public void connect() throws IOException {
        System.out.println("Attempting to connect to SMTP server: " + smtpServer + " Port: " + port);

        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket(smtpServer, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Start handshake
        System.out.println("Starting handshake...");
        socket.startHandshake(); // Start handshake

        // Process server response and send commands
        getSingleResponse(); // Read server response

        // Send EHLO command
        sendCommand("EHLO " + smtpServer);
        get250Response();

        // Send AUTH LOGIN command
        sendCommand("AUTH LOGIN");
        getSingleResponse();

        // Send encoded email address
        sendCommand(Base64.getEncoder().encodeToString(mail.getBytes("UTF-8")));

        String authResponses = getSingleResponse();

        // Check responses during authentication process

        // Send encoded password
        sendCommand(Base64.getEncoder().encodeToString(password.getBytes("UTF-8")));
        authResponses = getSingleResponse();

        // Check responses during authentication process
        if (!checkResponse(authResponses)) {
            throw new IOException("Authentication failed: Please enter a valid email/password.");
        }
        // Check final response
    }

    // Method to output requests and responses
    protected void sendCommand(String command) throws IOException {
        System.out.println("Client command: " + command);
        out.println(command);
        out.flush();
    }

    public void sendEmail(String to, String subject, String body,File[] attachments) throws IOException {
        // Create and send message
        MimeMessageBuilder builder = new MimeMessageBuilder(mail, to, subject, body, attachments);
        String mimeMessage = builder.build();

        sendCommand("MAIL FROM:<" + mail + ">");
        String response = getSingleResponse();

        sendCommand("RCPT TO:<" + to + ">");
        response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred with the recipient's email address. Please check if it is correct.\n" + String.join(", ", response));
        }

        sendCommand("DATA");
        response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred during the data transmission preparation.\n " + String.join(", ", response));
        }

        // Write and send email body

        out.print(mimeMessage + "\r\n.\r\n");
        out.flush();
        response = getSingleResponse();

        if (!checkResponse(response)) {
            throw new IOException("An error occurred while sending the email body.\n " + String.join(", ", response));
        }
    }
    public void Quit() throws IOException {
        // Send QUIT command
        System.out.println("Sending QUIT command");
        out.println("QUIT");
        out.flush();
        String response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred during the connection termination process.\n " + String.join(", ", response));
        }
        // Close socket
        socket.close();
        out.close();
        in.close();
        System.out.println("Email sent and connection terminated");
    }

    // Read server response
    public String[] get250Response() throws IOException {
        ArrayList<String> responses = new ArrayList<>(); // ArrayList to store responses
        String response;

        // Read multiple lines of responses
        while ((response = getSingleResponse()) != null) {
            responses.add(response); // Add response
            if (!response.startsWith("250-")) { // Stop loop if not starting with 250
                break;
            }
        }

        return responses.toArray(new String[0]); // Convert ArrayList to array and return
    }

    public String getSingleResponse() throws IOException {
        String response = in.readLine(); // Read a single line
        if (response != null) {
            System.out.println(response); // Print the response
        }
        return response; // Return the response
    }

    // Check an array of responses

    // Check a single response
    public boolean checkResponse(String reply) throws NullPointerException {
        return reply.startsWith("2") || reply.startsWith("3");
    }


    // Email address validation
    @Override
    public String toString() {
        return "Server: " + smtpServer + ", Mail ID: " + mail;
    }
}

```
</details>

<details>
<summary> EmailWithNaver.java </summary> 
 
```javascript
package email.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class EmailWithNaver extends EmailService{
	
	public EmailWithNaver(String mail, String password,String smtpServer,int port) {
    	super(mail,password,smtpServer,port);
    }
	
	@Override
    public void sendEmail(String to, String subject, String body) {
        try {
      
            // 메시지 생성 및 전송
            sendCommand("MAIL FROM:<" + mail + ">");
            getResponse();
            sendCommand("RCPT TO:<" + to + ">");
            getResponse();
            sendCommand("DATA");
            getResponse();

            // 이메일 본문 작성 및 전송
            System.out.println("이메일 본문 전송 중...");
            sendCommand("From: " + mail);
            sendCommand("To: " + to);
            sendCommand("Subject: " + subject);
            sendCommand(""); // 빈 줄 추가
            String[] lines = body.split("\n");
            for (String line : lines) {
            	sendCommand(line); // 본문 내용 전송
            }
            sendCommand("."); // 데이터 전송 완료를 알림
            getResponse();
            
            // QUIT 명령어 전송
            System.out.println("QUIT 명령어 전송");
            out.println("QUIT");
            out.flush();
            getResponse();

            // 소켓 닫기
            socket.close();
            out.close();
            in.close();
           
            System.out.println("이메일 전송 완료 및 연결 종료");
        } catch (IOException e) {
            System.err.println("이메일 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

```
</details>

- `Client.java`: 네이버 SMTP 서버를 사용하여 이메일을 보내기 위한 기본 설정
- `EmailServiceWithNaver.java`: 네이버 SMTP 서버와 안전한 연결을 구축하고, 이메일 인증과 데이터 전송을 수행
- `EmailWithNaver.java`: 네이버 SMTP 서버를 통해 이메일을 전송하는 기능
