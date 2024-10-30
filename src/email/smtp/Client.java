package email.smtp;

public class Client {
    
    public EmailServiceWithNaver mailService;
    
    public Client(String mail, String password) {
    	if (mail.endsWith("@naver.com")) {
    		mailService=new EmailServiceWithNaver(mail,password,"smtp.naver.com",465);
          
        }else
        	mailService=new EmailServiceWithNaver(mail,password,null,-1);
        }
}

