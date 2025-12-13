/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification;

/**
 *
 * @author Sazid R Khan
 */
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

// Provides reusable email sending operations using JavaMail.
// Configure SMTP settings below for Gmail, corporate SMTP, or FakeSMTP (local testing).
public class EmailService implements NotificationManager.MailSender {
    // FakeSMTP defaults (no auth, no TLS). Update if you switch to real SMTP.
    private static final String SMTP_HOST = "localhost";
    private static final String SMTP_PORT = "2525";
    private static final String SMTP_USERNAME = "";
    private static final String SMTP_PASSWORD = "";
    private static final String FROM_ADDRESS = "sazidrkhan@bongkus.com";

    private final Session session;

    public EmailService() {
        this.session = buildSession();
    }

    // Builds a JavaMail session; adjust properties when switching SMTP providers.
    private Session buildSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
    }

    // Sends an email without enforcing attachments.
    @Override
    public void sendEmail(NotificationMessage emailMessage) throws MessagingException {
        sendInternal(emailMessage, false);
    }

    // Sends an email and requires that the attachment (if specified) exists.
    @Override
    public void sendWithAttachment(NotificationMessage emailMessage) throws MessagingException {
        sendInternal(emailMessage, true);
    }

    // Shared send path that optionally enforces attachments.
    private void sendInternal(NotificationMessage emailMessage, boolean requireAttachment) throws MessagingException {
        if (emailMessage == null) {
            throw new MessagingException("EmailMessage is null");
        }
        if (emailMessage.getRecipient() == null || emailMessage.getRecipient().trim().isEmpty()) {
            throw new MessagingException("Recipient email is required");
        }

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_ADDRESS));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMessage.getRecipient()));
        message.setSubject(emailMessage.getSubject() == null ? "" : emailMessage.getSubject());

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(emailMessage.getBody() == null ? "" : emailMessage.getBody());

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);

        String attachmentPath = emailMessage.getAttachmentPath();
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            File file = new File(attachmentPath);
            if (!file.exists()) {
                throw new MessagingException("Attachment not found: " + file.getAbsolutePath());
            }
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
            attachmentPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentPart);
        } else if (requireAttachment) {
            throw new MessagingException("Attachment path is required for this email");
        }

        message.setContent(multipart);
        try {
            Transport.send(message);
        } catch (MessagingException ex) {
            throw new MessagingException("Failed to send email: " + ex.getMessage(), ex);
        }
    }
}
