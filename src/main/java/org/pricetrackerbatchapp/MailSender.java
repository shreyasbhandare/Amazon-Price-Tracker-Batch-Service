package org.pricetrackerbatchapp;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailSender {
    public static void sendEmail(String toEmail, String message) throws MessagingException {
        String userName = System.getenv("EMAIL_USERNAME") != null ? System.getenv("EMAIL_USERNAME") : PropsConfig.getAppProps().getProperty("EMAIL_USERNAME");
        String password = System.getenv("EMAIL_PASSWORD") != null ? System.getenv("EMAIL_PASSWORD") : PropsConfig.getAppProps().getProperty("EMAIL_PASSWORD");

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

    public static String createHtmlMessage(String email, List<Product> products) {
        if(products == null || products.isEmpty()) {
            return null;
        }

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(App.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        try {
            Template template = cfg.getTemplate("email_template.ftl");

            Map<String, Object> templateData = new HashMap<>();
            templateData.put("products", products);
            templateData.put("email", email);

            StringWriter out = new StringWriter();
            template.process(templateData, out);
            String htmlMessage = out.getBuffer().toString();
            out.flush();
            return htmlMessage;
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return null;
    }
}
