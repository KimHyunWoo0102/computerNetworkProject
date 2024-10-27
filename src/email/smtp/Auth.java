package email.smtp;

import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.net.ssl.*;

public class Auth {
    private Client client;
    SSLSocketFactory factory;
    SSLSocket sslSocket;
    // 내부 클래스: 고객에 대한 정보를 담기 위한 클래스
    private class Client {
        public String mail;
        public String password;
        public String smtpServer;
        public int port;

        public Client(String mail, String password) {
            this.mail = mail;
            this.password = password;
            setData();
        }

        private boolean setData() {
            if (mail.endsWith("@naver.com")) {
                this.smtpServer = "smtp.naver.com";
                this.port = 465;
                return true;
            }else {
                this.port = -1;
                return false;
            }
        }
    }

    public Auth(String mail, String password) {
        client = new Client(mail, password);
    }

    // 정상적인 도메인 이름을 입력받았는지 검사
    public boolean isCorrectAddress() {
        return client.port != -1;
    }

    public void sendEmailTT(String recipientEmail, String subject, String body) {
        try {
            System.out.println("SMTP 서버 연결 시도: " + client.smtpServer + " 포트: " + client.port);

            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) factory.createSocket(client.smtpServer, client.port);

            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            // 핸드셰이크 시작
            System.out.println("핸드셰이크 시작...");
            sslSocket.startHandshake(); // 핸드셰이크 시작

            // 서버 응답 처리 및 명령어 전송
            readResponse(in);  // 서버 응답 읽기
            
            // EHLO 명령어 전송
            System.out.println("EHLO 명령어 전송");
            out.println("EHLO " + client.smtpServer);
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // AUTH LOGIN 명령어 전송
            System.out.println("AUTH LOGIN 명령어 전송");
            out.println("AUTH LOGIN");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 이메일 주소 인코딩 전송
            System.out.println("이메일 주소 인코딩 전송");
            out.println(Base64.getEncoder().encodeToString(client.mail.getBytes("UTF-8")));
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 비밀번호 인코딩 전송
            System.out.println("비밀번호 인코딩 전송");
            out.println(Base64.getEncoder().encodeToString(client.password.getBytes("UTF-8")));
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // MAIL FROM 명령어 전송
            System.out.println("MAIL FROM 명령어 전송");
            out.println("MAIL FROM:<" + client.mail + ">");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 발신자 이메일 형식 확인
            if (!isValidEmail(client.mail)) {
                System.out.println("발신자 이메일 주소 형식이 잘못되었습니다: " + client.mail);
                return;
            }

            // RCPT TO 명령어 전송
            System.out.println("RCPT TO 명령어 전송");
            out.println("RCPT TO:<" + recipientEmail + ">");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // DATA 명령어 전송
            System.out.println("DATA 명령어 전송");
            out.println("DATA");
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // 이메일 본문 작성 및 전송
            System.out.println("이메일 본문 전송 중...");
           // 수신자 이메일
            out.println("Subject: " + subject); // 이메일 제목
            out.println(); // 빈 줄 추가
            String[] lines = body.split("\n");
            for (String line : lines) {
                out.println(line); // 본문 내용 전송
            }
            out.println("."); // 데이터 전송 완료를 알림
            readResponse(in);

            // QUIT 명령어 전송
            System.out.println("QUIT 명령어 전송");
            out.println("QUIT");
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // 소켓 닫기
            out.close();
            in.close();
            sslSocket.close();
            System.out.println("이메일 전송 완료 및 연결 종료");
        } catch (IOException e) {
            System.err.println("이메일 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

 // 이메일 전송 로직
 // 이메일 전송 로직
    public void sendEmail(String recipientEmail, String subject, String body) {
        try {
            System.out.println("SMTP 서버 연결 시도: " + client.smtpServer + " 포트: " + client.port);

            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) factory.createSocket(client.smtpServer, client.port);

            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            // 핸드셰이크 시작
            System.out.println("핸드셰이크 시작...");
            sslSocket.startHandshake(); // 핸드셰이크 시작

            // 서버 응답 처리 및 명령어 전송
            readResponse(in);  // 서버 응답 읽기
            
            // EHLO 명령어 전송
            System.out.println("EHLO 명령어 전송");
            out.println("EHLO " + client.smtpServer);
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // AUTH LOGIN 명령어 전송
            System.out.println("AUTH LOGIN 명령어 전송");
            out.println("AUTH LOGIN");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 이메일 주소 인코딩 전송
            System.out.println("이메일 주소 인코딩 전송");
            out.println(Base64.getEncoder().encodeToString(client.mail.getBytes("UTF-8")));
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 비밀번호 인코딩 전송
            System.out.println("비밀번호 인코딩 전송");
            out.println(Base64.getEncoder().encodeToString(client.password.getBytes("UTF-8")));
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // MAIL FROM 명령어 전송
            System.out.println("MAIL FROM 명령어 전송");
            out.println("MAIL FROM:<" + client.mail + ">");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // 발신자 이메일 형식 확인
            if (!isValidEmail(client.mail)) {
                System.out.println("발신자 이메일 주소 형식이 잘못되었습니다: " + client.mail);
                return;
            }

            // RCPT TO 명령어 전송
            System.out.println("RCPT TO 명령어 전송");
            out.println("RCPT TO:<" + recipientEmail + ">");
            out.flush(); // 버퍼 비우기
            readResponse(in);
            
            // DATA 명령어 전송
            System.out.println("DATA 명령어 전송");
            out.println("DATA");
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // 이메일 본문 작성 및 전송
            System.out.println("이메일 본문 전송 중...");
            out.println("From: " + client.mail); // 발신자 이메일 똑같은 코드인데도 해당 부분 헤더 빼먹으면 전송 안해줌
            out.println("To: " + recipientEmail); // 수신자 이메일
            out.println("Subject: " + subject); // 이메일 제목
            out.println(); // 빈 줄 추가
            String[] lines = body.split("\n");
            for (String line : lines) {
                out.println(line); // 본문 내용 전송
            }
            out.println("."); // 데이터 전송 완료를 알림
            readResponse(in);

            // QUIT 명령어 전송
            System.out.println("QUIT 명령어 전송");
            out.println("QUIT");
            out.flush(); // 버퍼 비우기
            readResponse(in);

            // 소켓 닫기
            out.close();
            in.close();
            sslSocket.close();
            System.out.println("이메일 전송 완료 및 연결 종료");
        } catch (IOException e) {
            System.err.println("이메일 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // 서버 응답 읽기 및 출력
    private void readResponse(BufferedReader in) throws IOException {
        String response = in.readLine();
        System.out.println("서버 응답: " + response);
    }

    // 이메일 주소의 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    @Override
    public String toString() {
        return "Server: " + client.smtpServer + ", Mail ID: " + client.mail;
    }
}