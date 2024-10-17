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
 - Team Leader: 김현우
 - Member 2: 박민수
 - Member 3: 홍지민
 - Member 4: 이서현
 - Member 5: 신예린

### ⚙️ 개발환경
 - Java Version: `JDK22`
 - IDE:
 - Framework:
 - ORM: 

## 📝 프로젝트 구성

### 패키지 구조
```
src
 └── email.gui
      ├── LoginGUI.java
      └── SendEmailGUI.java
 └── email.mime
      ├── FileHandler.java
      └── MimeMessageBuilder.java
 └── email.smtp
      ├── Auth.java
      ├── Client.java
 └── email.main
      └── EmailApp.java
```

## 📌 주요 기능
1. **로그인** 
   - 사용자가 `LoginGUI`에서 이메일과 비밀번호를 입력합니다.
   - 입력된 이메일은 `Auth` 객체에 전달되어 올바른 이메일 주소인지 확인합니다.
   - 이메일 주소가 유효하고 연결이 성공하면 `SendEmailGUI`가 실행됩니다.

2. **이메일 전송** 
   - `SendEmailGUI`에서 사용자는 이메일 메시지와 첨부할 파일을 선택합니다.
   - 사용자가 전송 버튼을 클릭하면 `Client` 클래스가 호출되어, 입력된 메시지와 파일에 대한 Base64 인코딩 등의 처리를 수행합니다.
   - 처리된 내용이 올바른지 검증 후, 최종적으로 이메일이 전송됩니다.

4. **파일 첨부**
   - 사용자가 첨부할 수 있는 파일의 형식은 다음과 같습니다.
     - 이미지 파일 (jpg, png 등)
     - 문서 파일 (pdf, docx 등)
     - 텍스트 파일 (txt 등)

## 🔎 패키지 상세 설명

### 1. `email.gui`
<details>
<summary> LoginGUI.java </summary> 

  ```javascript
  
  ```
  </details>

<details>
<summary> SendEmailGUI.java </summary> 

  ```javascript
  
  ```
  </details>
 
- `LoginGUI.java`: 사용자 로그인 인터페이스
- `SendEmailGUI.java`: 이메일 전송 인터페이스

### 2. `email.mime`
<details>
<summary> FileHandler.java </summary> 

  ```javascript
  package email.mime;
  
  import java.nio.file.Files;
  import java.nio.file.Path;
  import java.nio.file.Paths;
  import java.io.File;
  import java.io.IOException;
  import java.nio.file.StandardCopyOption;
  
  public class FileHandler {

      // 파일을 첨부하는 메소드
      // filePath로 지정된 파일을 첨부할 수 있는지 확인 후 처리
      public void attachFile(String filePath) {
          File file = new File(filePath);
          if (file.exists()) {
              if (isValidFileType(file)) {
                  // 유효한 파일이면 첨부
              } else {
                  System.out.println("유효하지가 않잖아."); // 오류 메시지 출력
              }
          } else {
              System.out.println("파일이 없잖아."); // 파일이 없는 경우 메시지 출력
          }
      }
  
      // 파일의 MIME 타입을 반환하는 메소드
      // 파일 경로를 입력받아 해당 파일의 MIME 타입을 반환
      // javax.activation으로도 구현하는게 있긴한데 이게 기본 jdk라이브러리에 빠져있어서 별도의 라이브러리로 추가해야함.
      // 장단점 비교 mime파트랑 논의 필요할듯
      public String getMimeType(String filePath) {
          Path path = Paths.get(filePath);
          try {
              return Files.probeContentType(path); // MIME 타입 탐지
          } catch (IOException e) {
              e.printStackTrace(); // 오류 발생 시 출력
              return "이상한 타입인데요"; // 알 수 없는 경우 처리
          }
      }
  
      // 파일 타입 유효성 확인 메소드
      // 이미지, PDF, 텍스트 파일이 유효한 파일 타입으로 간주됨
      public boolean isValidFileType(File file) {
          String mimeType = getMimeType(String.valueOf(file)); // MIME 타입 가져오기
          boolean isValid = false;
          if(mimeType.startsWith("image")) {
              isValid = true;
          } // image만 일단 했는데 뭐 파일 형식 여러개 해야겠쥬
          return isValid;
      }
  
      // 파일 크기 확인 메소드
      // 주어진 파일 경로에 대한 파일 크기를 반환
      public long getFileSize(String filePath) {
          File file = new File(filePath);
          if (file.exists()) {
              return file.length(); // 파일 크기 반환
          } else {
              System.out.println("파일이 없잖아요"); // 파일을 찾을 수 없는 경우
              return 0;
          }
      }
  
      // 파일 첨부 후 파일 목록을 관리하는 메소드
      public void manageAttachedFiles() {
          // 리스트에 파일을 추가하거나, 삭제하거나, 출력하는 기능 포함 예정
          // 한 메소드에 넣지 말고 나눌까? 좀 길어질거 같긴 함니다
      }
      // 파일을 로컬 디스크에 저장하는 메소드
      public void saveFileToDisk(File file, String destinationDirectory) throws IOException {
          // 파일이 유효한지 확인 후 저장 예정
          Path destinationPath = Path.of(destinationDirectory, file.getName());
          Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
      }
  }  

  ```
  </details>
  
<details>
<summary> MimeMessageBuilder.java </summary> 
 
  ```javascript
  package email.mime;

import javax.mail.*; // javax.mail 패키지에서 필요한 클래스들을 임포트
import javax.mail.internet.*; // 이메일 전송을 위한 인터넷 메일 클래스를 임포트
import java.util.Properties; // 이메일 전송에 필요한 설정 정보를 저장하기 위한 클래스

public class MimeMessageBuilder {
    private Session session; // 메일 세션을 위한 객체
    private MimeMessage message; // MIME 메시지를 위한 객체

    // MimeMessageBuilder 생성자
    // 이메일 세션을 초기화하고 메시지 객체를 생성
    public MimeMessageBuilder(String smtpHost, String smtpPort, String email, String password) {
        Properties properties = new Properties(); // 메일 서버 연결을 위한 설정
        properties.put("mail.smtp.host", smtpHost); // SMTP 서버 호스트 설정
        properties.put("mail.smtp.port", smtpPort); // SMTP 서버 포트 설정
        properties.put("mail.smtp.auth", "true"); // SMTP 인증 사용 설정
        properties.put("mail.smtp.starttls.enable", "true"); // TLS 사용 설정

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password); // 이메일과 비밀번호로 인증
            }
        });

        try {
            message = new MimeMessage(session); // 세션을 사용하여 MIME 메시지 생성
        } catch (MessagingException e) {
            e.printStackTrace(); // 메시지 생성 시 오류가 발생하면 출력
        }
    }

    // 수신자 이메일 주소 설정 메소드
    // 수신자의 이메일 주소를 추가
    public void setRecipient(String recipient) throws MessagingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // 수신자 추가
    }

    // 이메일 제목 설정 메소드
    // 이메일 제목을 설정
    public void setSubject(String subject) throws MessagingException {
        message.setSubject(subject); // 제목 설정
    }

    // 이메일 본문 설정 메소드
    // 이메일 내용을 설정 (HTML 형식 지원)
    public void setBody(String body) throws MessagingException {
        message.setContent(body, "text/html; charset=utf-8"); // 본문 내용 설정
    }

    // 이메일 첨부 파일 추가 메소드
    // 첨부할 파일 경로를 받아 MIME 메시지에 첨부
    public void addAttachment(String filePath) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart(); // 첨부 파일을 위한 바디 파트 생성
        messageBodyPart.setFileName(filePath); // 파일 이름 설정

        MimeMultipart multipart = new MimeMultipart(); // 여러 부분으로 구성된 MIME 메시지 생성
        multipart.addBodyPart(messageBodyPart); // 바디 파트 추가
        message.setContent(multipart); // 메시지에 MIME 멀티파트 설정
    }

    // MIME 메시지 전송 메소드
    // 생성된 메시지를 전송
    public void send() throws MessagingException {
        Transport.send(message); // 이메일 전송
        System.out.println("이메일이 전송되었습니다."); // 전송 성공 메시지 출력
    }
}
  ```
  </details>
  
- `FileHandler.java`: 파일 첨부 처리 및 파일 관련 작업
- `MimeMessageBuilder.java`: MIME 메시지 생성 및 관련 처리

### 3. `email.smtp`
<details>
<summary> Auth.java </summary> 
 
```javascript

```
</details>

<details>
<summary> Client.java </summary> 
 
```javascript
package email.smtp;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import email.mime.FileHandler;
import email.smtp.Auth;

public class Client {
	private Auth auth; // 사용자 인증 정보
	private FileHandler fileHandler; // 파일 처리

	// Client 생성자
	public Client(Auth auth, FileHandler fileHandler) {
		this.auth = auth; // 인증 객체 설정
		this.fileHandler = fileHandler; // 파일 처리 객체 설정
	}

	// 이메일 전송 메소드
	// 이메일 작성한 내용을 받아와서 전송하는 메소드 입니다. 
	// Auth class 참고하면서 작성했는데 private 변수를 public으로 바꾸서 사용했습니다..
	public boolean sendEmail(String recipient, String subject, String messageBody, String filePath) {
		// 지원하는 이메일 형식인지 구분
		if (!auth.authenticate()) { // 인증 되지 않았으면 false 반환 
			return false;
		}

		// 소켓 설정
		try (Socket socket = new Socket(auth.smtpServer, auth.port); // SMTP 서버에 소켓 연결
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버 응답 읽기
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) { // 서버에
																												// 명령어
																												// 쓰기

			// 서버 응답 확인
			readResponse(reader);

			// EHLO 명령 - SMTP 서버 통신 시작
			sendCommand(writer, "EHLO " + auth.smtpServer);
			readResponse(reader);

			// SMTP 인증
			sendCommand(writer, "AUTH LOGIN");	// 인증 시작 
			readResponse(reader);
			
			// 일단 이메일,비밀번호 인코딩 작성하기는했는데 MIME 이나 FileHandler 에서 처리해주시면 코드 다시 바꾸면 될듯합니다.
			sendCommand(writer, Base64.getEncoder().encodeToString(auth.email.getBytes()));	// 이메일 주소 인코딩 전송 
			readResponse(reader);
			sendCommand(writer, Base64.getEncoder().encodeToString(auth.password.getBytes()));	// 비밀번호 인코딩 전송 
			readResponse(reader);

			// 송신자와 수신자 설정
			sendCommand(writer, "MAIL FROM:<" + auth.email + ">");	// 송신자 설정 
			readResponse(reader);
			sendCommand(writer, "RCPT TO:<" + recipient + ">");	// 수신자 설정 
			readResponse(reader);

			// 데이터 전송 시작
			sendCommand(writer, "DATA");	// 데이터 전송  
			readResponse(reader);

			// 이메일 본문 작성
			writer.write("Subject: " + subject + "\r\n");	// 제목 
			writer.write("From: " + auth.email + "\r\n");	// 송신자 
			writer.write("To: " + recipient + "\r\n");	// 수신자 
			writer.write("MIME-Version: 1.0\r\n");	// MIME 버전 1.0으로 설정 
			writer.write("Content-Type: multipart/mixed; boundary=\"boundary\"\r\n");	// 이메일 본문 유형 멀티파트(텍스트+첨부파일)으로 지정 
			writer.write("\r\n--boundary\r\n");	// 본문과 첨부 파일 구분 
			writer.write("Content-Type: text/plain; charset=UTF-8\r\n\r\n");	// 텍스트 형식 명시 
			writer.write(messageBody + "\r\n");	// 본문 내용 추가 

			// 첨부 파일 처리
			// FileHandler 작성해주신 함수 사용해서 처리하면 될 듯합니다.

			// 이메일 전송 종료
			writer.write("\r\n--boundary--\r\n.\r\n");	// 종료 표시 
			writer.flush();
			readResponse(reader);

			// QUIT 명령으로 연결 종료
			sendCommand(writer, "QUIT"); 	// 종료 
			System.out.println("이메일이 성공적으로 전송되었습니다.");
			return true; // 성공하면 true 반환

		} catch (IOException e) {	// 오류 처리 
			e.printStackTrace();
			System.out.println("이메일 전송 실패.");
			return false; // 실패하면 false 반환
		}
	}

	// SMTP 서버에 명령어 전송 메소드 
	// writer 명시 + 명령어(String) 형식으로 작성 
	private void sendCommand(BufferedWriter writer, String command) throws IOException {
		writer.write(command + "\r\n");	// 명령어 전송 
		writer.flush();	// 버퍼 비움 
	}

	// SMTP 서버의 응답을 읽고 출력
	private String readResponse(BufferedReader reader) throws IOException {
		String response = reader.readLine(); // 응답 읽음 
		System.out.println("Server: " + response); // 서버 응답 출력 
		return response; // 응답 반환 
	}
}

```
</details>
  
- `Auth.java`: 사용자 인증 및 SMTP 서버 연결 처리
- `Client.java`: 이메일 전송 및 파일 첨부 관련 기능

