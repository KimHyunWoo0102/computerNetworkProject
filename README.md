<div align="center">
<h2>SMTP Email System 📧</h2>
사용자가 이메일을 송신할 수 있는 'SMTP 이메일 시스템'입니다.
</div>

## 🖥 프로젝트 개요
 - Java를 기반으로 구축
 - 사용자 인증, 이메일 전송 및 파일 첨부 기능

### 📅 개발 기간
2024.10.12 - 2024.11.06

### 🧑‍🤝‍🧑 팀원
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

## 패키지 상세 설명

### 1. `email.gui`
- `LoginGUI.java`: 사용자 로그인 인터페이스
- `SendEmailGUI.java`: 이메일 전송 인터페이스

### 2. `email.mime`
- `FileHandler.java`: 파일 첨부 처리 및 파일 관련 작업
- `MimeMessageBuilder.java`: MIME 메시지 생성 및 관련 처리

### 3. `email.smtp`
- `Auth.java`: 사용자 인증 및 SMTP 서버 연결 처리
- `Client.java`: 이메일 전송 및 파일 첨부 관련 기능
