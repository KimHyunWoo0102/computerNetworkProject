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

