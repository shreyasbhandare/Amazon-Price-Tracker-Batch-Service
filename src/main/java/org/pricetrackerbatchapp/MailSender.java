package org.pricetrackerbatchapp;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;

public class MailSender {
    public static void sendEmail(String toEmail, String message) throws MessagingException {
        String userName = PropsConfig.getAppProps().getProperty("EMAIL_USERNAME");
        String password = PropsConfig.getAppProps().getProperty("EMAIL_PASSWORD");

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message and send
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toEmail) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject("Today's Amazon product prices");
        msg.setContent(message, "text/html");

        Transport.send(msg);
    }

    public static String createHtmlMessage(List<Product> products) {
        if(products == null || products.isEmpty()) {
            return null;
        }

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("<html><body>");
        for(Product product : products) {
            messageBuilder.append("<h2>")
                    .append(product.getName())
                    .append(" : ")
                    .append(product.getPrice())
                    .append("</h2>")
                    .append("<br>");
        }
        messageBuilder.append("</body></html>");
        return messageBuilder.toString();
    }
}
