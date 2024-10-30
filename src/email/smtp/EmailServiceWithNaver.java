package email.smtp;

import email.mime.MimeMessageBuilder;

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

    // List to store requests and responses
    protected List<String> responses;

    public EmailServiceWithNaver(String mail, String password, String smtpServer, int port) {
        this.mail = mail;
        this.password = password;
        this.smtpServer = smtpServer;
        this.port = port;
        this.responses = new ArrayList<>(); // Initialize response list
    }

    // Check if a valid domain name has been entered
    public boolean isCorrectAddress() {
        return port != -1;
    }

    public void connect() throws IOException {
        System.out.println("Attempting to connect to SMTP server: " + smtpServer + " Port: " + port);

        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket(smtpServer, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Start handshake
        System.out.println("Starting handshake...");
        socket.startHandshake(); // Start handshake

        // Process server response and send commands
        getSingleResponse(); // Read server response

        // Send EHLO command
        sendCommand("EHLO " + smtpServer);
        get250Response();

        // Send AUTH LOGIN command
        sendCommand("AUTH LOGIN");
        getSingleResponse();

        // Send encoded email address
        sendCommand(Base64.getEncoder().encodeToString(mail.getBytes("UTF-8")));

        String authResponses = getSingleResponse();

        // Check responses during authentication process

        // Send encoded password
        sendCommand(Base64.getEncoder().encodeToString(password.getBytes("UTF-8")));
        authResponses = getSingleResponse();

        // Check responses during authentication process
        if (!checkResponse(authResponses)) {
            throw new IOException("Authentication failed: Please enter a valid email/password.");
        }
        // Check final response
    }

    // Method to output requests and responses
    protected void sendCommand(String command) throws IOException {
        System.out.println("Client command: " + command);
        out.println(command);
        out.flush();
    }

    public void sendEmail(String to, String subject, String body,File[] attachments) throws IOException {
        // Create and send message
        MimeMessageBuilder builder = new MimeMessageBuilder(mail,to,subject,body,attachments);
        String mimeMessage = builder.build();

        sendCommand("MAIL FROM:<" + mail + ">");
        String response = getSingleResponse();

        sendCommand("RCPT TO:<" + to + ">");
        response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred with the recipient's email address. Please check if it is correct.\n" + String.join(", ", response));
        }

        sendCommand("DATA");
        response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred during the data transmission preparation.\n " + String.join(", ", response));
        }

        // Write and send email body

        out.print(mimeMessage + "\r\n.\r\n");
        out.flush();
        response = getSingleResponse();

        if (!checkResponse(response)) {
            throw new IOException("An error occurred while sending the email body.\n " + String.join(", ", response));
        }

        // Send QUIT command
        System.out.println("Sending QUIT command");
        out.println("QUIT");
        out.flush();
        response = getSingleResponse();
        if (!checkResponse(response)) {
            throw new IOException("An error occurred during the connection termination process.\n " + String.join(", ", response));
        }

        // Close socket
        socket.close();
        out.close();
        in.close();

        System.out.println("Email sent and connection terminated");
    }

    // Read server response
    public String[] get250Response() throws IOException {
        ArrayList<String> responses = new ArrayList<>(); // ArrayList to store responses
        String response;

        // Read multiple lines of responses
        while ((response = getSingleResponse()) != null) {
            responses.add(response); // Add response
            if (!response.startsWith("250-")) { // Stop loop if not starting with 250
                break;
            }
        }

        return responses.toArray(new String[0]); // Convert ArrayList to array and return
    }

    public String getSingleResponse() throws IOException {
        String response = in.readLine(); // Read a single line
        if (response != null) {
            System.out.println(response); // Print the response
        }
        return response; // Return the response
    }

    // Check an array of responses

    // Check a single response
    public boolean checkResponse(String reply) {
        return reply.startsWith("2") || reply.startsWith("3");
    }


    // Email address validation
    @Override
    public String toString() {
        return "Server: " + smtpServer + ", Mail ID: " + mail;
    }
}
