package com.newscurator.app;

import com.newscurator.schema.NewsArticle;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

import static com.newscurator.util.Constants.GMAIL_PASSWORD;
import static com.newscurator.util.Constants.GMAIL_USERNAME;


public class EmailSender {

    private static final String USERNAME = EnvironmentVariableKeeper.getInstance().getVariable(GMAIL_USERNAME);
    private static final String PASSWORD = EnvironmentVariableKeeper.getInstance().getVariable(GMAIL_PASSWORD);

    private static final Properties properties = new Properties();

    static {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
    }

    public void sendNewsCuratorEmail(List<NewsArticle> newsArticles) {
        Session session = createSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(USERNAME)
            );

            // create message
            message.setSubject("Here's your curated news of the day!");
            String body = "";
            for (NewsArticle article: newsArticles){
                String articleName = article.getTitle();
                String url = article.getUrl();
                body += "\u2022 " + articleName + ":\n" + url + "\n\n";
            }
            message.setText(body);

            Transport.send(message);

            // add logger

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Session createSession() {
        return Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }
}
