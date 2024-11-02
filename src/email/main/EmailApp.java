package email.main;

import email.gui.LoginGUI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class EmailApp {
    // 프로그램 시작 메소드
    public static void main(String[] args) {
        // Look and Feel 설정
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginGUI loginGUI = new LoginGUI(); // LoginGUI 인스턴스 생성
        loginGUI.display(); // 로그인 화면 표시
    }
}
