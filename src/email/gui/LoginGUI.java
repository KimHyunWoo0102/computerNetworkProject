package email.gui;

import javax.swing.*; // Swing 컴포넌트 사용을 위한 import
import java.awt.event.ActionEvent; // ActionEvent 사용을 위한 import
import java.awt.event.ActionListener; // ActionListener 인터페이스 사용을 위한 import
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
        frame.setSize(250, 150); // 창 크기 설정

        emailField = new JTextField(15); // 이메일 입력 필드 크기 조정
        passwordField = new JPasswordField(15); // 비밀번호 입력 필드 크기 조정
        loginButton = new JButton("Login"); // 로그인 버튼 생성
        errorLabel = new JLabel(); // 오류 메시지를 표시할 레이블 생성

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginButtonClick(); // 로그인 버튼 클릭 시 호출되는 메소드
            }
        });

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
            showError("이메일 주소 형식이 올바르지 않습니다.");
            return; // 유효하지 않은 경우 반환
        }

        System.out.println("이메일 : "+email+" 비밀번호 : "+password+" 입력완료!");
        // Auth 인스턴스 생성
        Auth auth = new Auth(email, password);
        
        // 이메일 도메인 검증
        if (!auth.isCorrectAddress()) {
            showError("지원하지 않는 이메일 도메인입니다. @naver.com을 사용하세요.");
            return; // 유효하지 않은 경우 반환
        }

        JOptionPane.showMessageDialog(frame, "로그인 성공!"); // 성공 메시지 표시
        clearFields(); // 필드 초기화
        frame.dispose(); // 로그인 창 닫기
            
        // SendEmailGUI 인스턴스 생성 및 표시
        SendEmailGUI sendEmailGUI = new SendEmailGUI(auth); // SendEmailGUI 생성
        sendEmailGUI.display(); // SendEmailGUI 표시
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

    // 레이아웃을 설정하는 메소드
    private void setLayout() {
        JPanel panel = new JPanel(); // 패널 생성
        frame.add(panel); // 패널을 프레임에 추가
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 수직 레이아웃 설정

        panel.add(new JLabel("Email:")); // 이메일 레이블 추가
        panel.add(emailField); // 이메일 입력 필드 추가
        panel.add(new JLabel("Password:")); // 비밀번호 레이블 추가
        panel.add(passwordField); // 비밀번호 입력 필드 추가
        panel.add(loginButton); // 로그인 버튼 추가
        panel.add(errorLabel); // 오류 메시지 레이블 추가
    }
}
