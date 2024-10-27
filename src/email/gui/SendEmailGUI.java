package email.gui;

import javax.swing.*; // Swing 컴포넌트 사용을 위한 import
import java.awt.event.ActionEvent; // ActionEvent 사용을 위한 import
import java.awt.event.ActionListener; // ActionListener 인터페이스 사용을 위한 import
import email.smtp.Auth; // 이메일 전송을 위한 Client 클래스 import

public class SendEmailGUI {
    private JFrame frame; // 전송 창을 위한 JFrame
    private JTextField recipientField; // 수신자 이메일 입력 필드를 위한 JTextField
    private JTextField subjectField; // 제목 입력을 위한 JTextField 추가
    private JTextArea messageArea; // 메시지 입력을 위한 JTextArea
    private JButton sendButton; // 전송 버튼을 위한 JButton
    private JProgressBar progressBar; // 전송 진행 상황을 표시할 JProgressBar
    private JLabel statusLabel; // 전송 상태 메시지를 표시할 JLabel
    private JLabel errorLabel; // 오류 메시지를 표시할 JLabel
    private Auth auth; // 이메일 전송을 위한 Client 클래스의 인스턴스

    // SendEmailGUI 생성자
    public SendEmailGUI(Auth auth) {
        this.auth = auth; // Client 인스턴스 초기화
        
        System.out.println(this.auth);
        initComponents(); // 컴포넌트 초기화 메소드 호출
    }

    // 이메일 전송 화면을 표시하는 메소드
    public void display() {
        frame.setVisible(true); // 프레임을 보이도록 설정
    }

    // GUI 컴포넌트를 초기화하는 메소드
    private void initComponents() {
        frame = new JFrame("메일 전송"); // 창 이름 변경
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 동작 설정
        frame.setSize(300, 350); // 창 크기 설정 (높이를 50 픽셀 늘림)

        recipientField = new JTextField(15); // 수신자 이메일 입력 필드 생성
        subjectField = new JTextField(15); // 제목 입력 필드 생성
        messageArea = new JTextArea(10, 25); // 메일 내용 입력 필드 생성
        sendButton = new JButton("Send"); // 전송 버튼 생성
        progressBar = new JProgressBar(); // 진행 바 생성
        statusLabel = new JLabel(); // 상태 레이블 생성

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSendButtonClick(); // 전송 버튼 클릭 시 호출되는 메소드
            }
        });

        setLayout(); // 레이아웃 설정
    }

    // 전송 버튼 클릭 시 호출되는 메소드
    private void onSendButtonClick() {
        String recipient = recipientField.getText(); // 수신자 이메일 입력값 가져오기
        String subject = subjectField.getText(); // 제목 입력값 가져오기
        String message = messageArea.getText(); // 메시지 입력값 가져오기
        
        if (recipient.isEmpty()) {
            showError("보낼 이메일 주소를 올바르게 입력해 주세요.");
            return;
        }
        
        System.out.println(recipient+" "+subject+" "+message);
        // 이메일 전송 요청 (비동기 처리를 위해 새 스레드 사용)
        new Thread(() -> {
            updateProgress(0); // 진행 바 초기화
            statusLabel.setText("전송 중..."); // 상태 메시지 업데이트
            try {
                // 이메일 전송 로직을 Client 클래스에 구현해야 함
            	//auth.sendEmailTT(recipient, subject, message);
                auth.sendEmail(recipient, subject, message); // 이메일 전송
                
            } catch (Exception e) {
                
            }
        }).start();
    }
    
    //오류메시지 업로드
    private void showError(String message) {
        errorLabel.setText(message); // 오류 메시지를 설정
        JOptionPane.showMessageDialog(frame, message, "오류", JOptionPane.ERROR_MESSAGE); // 팝업으로 오류 메시지 표시
    }
    // 전송 진행 상황을 업데이트하는 메소드
    private void updateProgress(int percentage) {
        progressBar.setValue(percentage); // 진행 바 값 설정
        progressBar.setString(percentage + "%"); // 진행 바 텍스트 설정
        progressBar.setStringPainted(true); // 텍스트 표시
    }

    // 전송 완료 메시지를 팝업으로 보여주는 메소드
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(frame, message); // 성공 메시지 팝업으로 표시
        clearFields(); // 입력 필드 초기화
    }

    // 입력 필드를 초기화하는 메소드
    private void clearFields() {
        recipientField.setText(""); // 수신자 필드 초기화
        subjectField.setText(""); // 제목 필드 초기화
        messageArea.setText(""); // 메시지 필드 초기화
        statusLabel.setText(""); // 상태 메시지 초기화
    }

    // 레이아웃을 설정하는 메소드
    private void setLayout() {
        JPanel panel = new JPanel(); // 패널 생성
        frame.add(panel); // 패널을 프레임에 추가
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 수직 레이아웃 설정

        panel.add(new JLabel("To:")); // 수신자 레이블 추가
        panel.add(recipientField); // 수신자 이메일 입력 필드 추가
        panel.add(new JLabel("Subject:")); // 제목 레이블 추가
        panel.add(subjectField); // 제목 입력 필드 추가
        panel.add(new JLabel("Message:")); // 메시지 레이블 추가
        panel.add(new JScrollPane(messageArea)); // 메시지 입력 필드 추가
        panel.add(sendButton); // 전송 버튼 추가
        panel.add(progressBar); // 진행 바 추가
        panel.add(statusLabel); // 상태 레이블 추가
    }
}