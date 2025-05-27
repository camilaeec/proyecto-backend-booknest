package com.example.booknest.email.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);

        String process = templateEngine.process("sign-in-template.html", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(process, true);
        helper.setSubject("¡Bienvenido a BookNest!");

        mailSender.send(mimeMessage);
    }

    public void sendTransactionConfirmed(String to, String name, String title) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("title", title);

        String process = templateEngine.process("transaction-confirmed.html", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(process, true);
        helper.setSubject("Transacción confirmada: " + title);

        mailSender.send(mimeMessage);
    }

    public void sendTransactionOffer(String to, String title, String offerer) throws MessagingException {
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("offerer", offerer);

        String process = templateEngine.process("transaction-offer-template.html", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(process, true);
        helper.setSubject("Nueva oferta de intercambio para " + title);

        mailSender.send(mimeMessage);
    }
}
