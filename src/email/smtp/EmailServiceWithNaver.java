package email.smtp;

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
    
    // 요청과 응답을 저장할 리스트
    protected List<String> responses;

    public EmailServiceWithNaver(String mail, String password, String smtpServer, int port) {
        this.mail = mail;
        this.password = password;
        this.smtpServer = smtpServer;
        this.port = port;
        this.responses = new ArrayList<>(); // 응답 리스트 초기화
    }

    // 정상적인 도메인 이름을 입력받았는지 검사
    public boolean isCorrectAddress() {
        return port != -1;
    }

    public void connect() throws IOException {
        System.out.println("SMTP 서버 연결 시도: " + smtpServer + " 포트: " + port);

        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket(smtpServer, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 핸드셰이크 시작
        System.out.println("핸드셰이크 시작...");
        socket.startHandshake(); // 핸드셰이크 시작

        // 서버 응답 처리 및 명령어 전송
        getResponse(); // 서버 응답 읽기

        // EHLO 명령어 전송
        sendCommand("EHLO " + smtpServer);
        getResponse();
        // AUTH LOGIN 명령어 전송
        
        sendCommand("AUTH LOGIN");
        getResponse();
        
        // 이메일 주소 인코딩 전송
        sendCommand(Base64.getEncoder().encodeToString(mail.getBytes("UTF-8")));
        
        String []authResponses = getResponse();
        
        // 인증 과정에서 응답 확인
     
        // 비밀번호 인코딩 전송
        sendCommand(Base64.getEncoder().encodeToString(password.getBytes("UTF-8")));
        authResponses = getResponse();
        
        // 인증 과정에서 응답 확인
        if (!checkResponse(authResponses)) {
            throw new IOException("인증 실패: 올바른 이메일/비밀번호를 입력해주세요.");
        }
        // 마지막 응답 확인
    }



    // 요청과 응답을 출력하는 메서드
    protected void sendCommand(String command) throws IOException {
        System.out.println("명령어 전송: " + command);
        out.println(command);
        out.flush();
    }

    public void sendEmail(String to, String subject, String body) throws IOException {
        // 메시지 생성 및 전송
        sendCommand("MAIL FROM:<" + mail + ">");
        String[] response = getResponse();
        

        sendCommand("RCPT TO:<" + to + ">");
        response = getResponse();
        if (!checkResponse(response)) {
            throw new IOException("수신자 이메일 주소에 오류가 발생했습니다. 올바른 이메일 주소인지 확인해주세요.\n"+ String.join(", ", response));
        }

        sendCommand("DATA");
        response = getResponse();
        if (!checkResponse(response)) {
            throw new IOException("데이터 전송 준비 과정에서 오류가 발생했습니다.\n " +  String.join(", ", response));
        }

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
        response = getResponse();
        if (!checkResponse(response)) {
            throw new IOException("이메일 본문 전송 중 오류가 발생했습니다.\n " +  String.join(", ", response));
        }
        
        // QUIT 명령어 전송
        System.out.println("QUIT 명령어 전송");
        out.println("QUIT");
        out.flush();
        response = getResponse();
        if (!checkResponse(response)) {
            throw new IOException("연결 종료 과정에서 오류가 발생했습니다.\n " + String.join(", ", response));
        }

        // 소켓 닫기
        socket.close();
        out.close();
        in.close();
        
        System.out.println("이메일 전송 완료 및 연결 종료");
    }



    // 서버 응답 읽기
    public String[] getResponse() throws IOException {
        ArrayList<String> responses = new ArrayList<>(); // 응답을 저장할 ArrayList
        String response;

        // 여러 줄의 응답을 읽어오기
        while ((response = in.readLine()) != null) {
            responses.add(response); // 응답 추가
            System.out.println(response);
            if (!response.startsWith("250-")) { // 250으로 시작하지 않으면 반복 종료
                break;
            }
        }

        return responses.toArray(new String[0]); // ArrayList를 배열로 변환하여 반환
    }
    
    
 
    // 모든 응답을 반환하는 메서드
    public boolean checkResponse(String []replys) {
    	for(String reply:replys) {
    		if(!reply.startsWith("2")||!reply.startsWith("3"))
    			return false;
    	}
    	
    	return true;
    }
    // 이메일 주소의 유효성 검사
    @Override
    public String toString() {
        return "Server: " + smtpServer + ", Mail ID: " + mail;
    }
}
