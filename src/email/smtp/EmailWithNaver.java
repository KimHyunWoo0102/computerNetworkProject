package email.smtp;

/*
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

*/