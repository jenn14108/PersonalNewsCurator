package com.newscurator.app;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.newscurator.util.Constants.GMAIL_PASSWORD;
import static com.newscurator.util.Constants.GMAIL_USERNAME;


public class EmailSender {

    private static final String USERNAME = EnvironmentVariableKeeper.getInstance().getVariable(GMAIL_USERNAME);
    private static final String PASSWORD = EnvironmentVariableKeeper.getInstance().getVariable(GMAIL_PASSWORD);

    public static void main(String[] args) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(USERNAME)
            );
            message.setSubject("Here's your curated news of the day!");
            message.setText("Jk this is just for testing :)");

            Transport.send(message);

            System.out.println("Email sent.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
