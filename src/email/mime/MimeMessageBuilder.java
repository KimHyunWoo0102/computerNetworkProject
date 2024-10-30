package email.mime;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class MimeMessageBuilder {
    private String boundary;
    private String from;
    private String to;
    private String subject;
    private String body;
    private File[] attachments;

    public MimeMessageBuilder(String from, String to, String subject, String body, File[] attachments) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
        this.boundary = "----=_MIME_BOUNDARY_" + System.currentTimeMillis();
    }

    // From 헤더
    private String createFromHeader() {
        return "From: " + from + "\r\n";
    }

    // To 헤더
    private String createToHeader() {
        return "To: " + to + "\r\n";
    }

    // Subject 헤더
    private String createSubjectHeader() {
        return "Subject: " + subject + "\r\n";
    }

    // MIME 본문
    private String createMimeBody() {
        StringBuilder mimeBody = new StringBuilder();
        mimeBody.append("Content-Type: text/plain; charset=UTF-8\r\n");
        mimeBody.append("Content-Transfer-Encoding: 7bit\r\n");
        mimeBody.append("\r\n");
        mimeBody.append(body).append("\r\n");
        return mimeBody.toString();
    }

    // 첨부 파일을 MIME 파트로 인코딩
    private String createMimeAttachment(File file) throws IOException {
        StringBuilder mimeAttachment = new StringBuilder();
        mimeAttachment.append("Content-Type: application/octet-stream; name=\"").append(file.getName()).append("\"\r\n");
        mimeAttachment.append("Content-Transfer-Encoding: base64\r\n");
        mimeAttachment.append("Content-Disposition: attachment; filename=\"").append(file.getName()).append("\"\r\n");
        mimeAttachment.append("\r\n");
        mimeAttachment.append(encodeFileToBase64(file)).append("\r\n");
        return mimeAttachment.toString();
    }

    // Boundary 추가
    private String createMimeBoundary() {
        return "--" + boundary + "\r\n";
    }

    // Base64 인코딩
    private String encodeFileToBase64(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    // 최종 MIME 메시지 생성
    public String build() throws IOException {
        StringBuilder mimeMessage = new StringBuilder();

        mimeMessage.append(createFromHeader());
        mimeMessage.append(createToHeader());
        mimeMessage.append(createSubjectHeader());
        mimeMessage.append("MIME-Version: 1.0\r\n");
        mimeMessage.append("Content-Type: multipart/mixed; boundary=\"").append(boundary).append("\"\r\n");
        mimeMessage.append("\r\n");

        // 본문 추가
        mimeMessage.append(createMimeBoundary());
        mimeMessage.append(createMimeBody());

        // 첨부 파일 추가
        if (attachments != null) {
            for (File file : attachments) {
                mimeMessage.append(createMimeBoundary());
                mimeMessage.append(createMimeAttachment(file));
            }
        }

        // 메시지 종료
        mimeMessage.append("--").append(boundary).append("--\r\n");

        return mimeMessage.toString();
    }
}