package com.learningwebsocket.websocketwithMongoDB;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async("emailTaskExecutor")  // Make sure you have a task executor configured with this name
    public CompletableFuture<Void> sendHtmlEmailAsync(String to, String subject, String body) {
        try {
            sendHtmlEmail(to, subject, body);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException | IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<?> sendHtmlEmail(String to, String subject, String body) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);

        // Read HTML content from the template file or use provided body
        String htmlContent = readHtmlTemplate("email-template.html");
        htmlContent = htmlContent.replace("{{subject}}", subject).replace("{{body}}", body);

        // Set the HTML content in the email
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
        return null;
    }

    private String readHtmlTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource(templateName);
        Path path = resource.getFile().toPath();
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
