package email.smtp;

import javax.swing.*; // JOptionPane 사용을 위한 import

public class Auth {
	private String email; // 사용자 이메일
	private String password; // 사용자 비밀번호
	private String smtpServer; // SMTP 서버 주소
	private int port; // 포트 번호

	// Auth 클래스 생성자
	public Auth(String email, String password) {
		this.email = email; // 사용자 이메일 초기화
		this.password = password; // 사용자 비밀번호 초기화
		this.smtpServer = determineSmtpServer(email); // 이메일 도메인에 따른 SMTP 서버 결정
		this.port = determinePort(smtpServer); // SMTP 서버에 맞는 포트 결정
	}

	// 인증을 수행하는 메소드
	public boolean authenticate() {
		if (smtpServer == null) {
			JOptionPane.showMessageDialog(null, "지원되지 않는 이메일 도메인입니다."); // 오류 메시지 표시
			return false; // 인증 실패
		}
		// 여기에 SMTP 서버와의 연결 및 인증 로직을 추가할 수 있음
		// 인증 성공 여부를 반환
		return true; // 인증 성공 (가정)
	}

	// 이메일 도메인에 따른 SMTP 서버 주소를 결정하는 메소드
	private String determineSmtpServer(String email) {
		if (email.endsWith("@naver.com")) {
			return "smtp.naver.com"; // Naver SMTP 서버 주소
		} else if (email.endsWith("@gmail.com")) {
			return "smtp.gmail.com"; // Gmail SMTP 서버 주소
		}
		return null; // 지원되지 않는 도메인
	}

	// SMTP 서버에 맞는 포트를 결정하는 메소드
	private int determinePort(String smtpServer) {
		if (smtpServer.equals("smtp.naver.com")) {
			return 587; // Naver SMTP 포트
		} else if (smtpServer.equals("smtp.gmail.com")) {
			return 465; // Gmail SMTP 포트
		}
		return -1; // 잘못된 포트
	}
}
