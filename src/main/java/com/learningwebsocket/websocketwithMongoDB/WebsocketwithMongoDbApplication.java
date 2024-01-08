package com.learningwebsocket.websocketwithMongoDB;

import com.learningwebsocket.websocketwithMongoDB.*;
import jakarta.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class WebsocketwithMongoDbApplication {

    @Autowired
    private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(WebsocketwithMongoDbApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void triggerMail() {
        // List of email addresses
        String[] recipients = {"bjnkuli008@gmail.com", "bjnsieuquay007@gmail.com"};

        String subject = "Subject of the Email";
        String body = "This is the email body.";

        // Send emails asynchronously
        CompletableFuture<?>[] futures = new CompletableFuture<?>[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            try {
                futures[i] = senderService.sendHtmlEmail(recipients[i], subject, body);
                long endTime = System.currentTimeMillis();
                long startTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Time taken for sending emails: " + elapsedTime + " milliseconds");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    }
}
