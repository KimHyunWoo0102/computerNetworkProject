package email.main;

import email.gui.LoginGUI;

public class EmailApp {
	// 프로그램 시작 메소드
    public static void main(String[] args) {
        LoginGUI loginGUI = new LoginGUI(); // LoginGUI 인스턴스 생성
        loginGUI.display(); // 로그인 화면 표시
    }
}
