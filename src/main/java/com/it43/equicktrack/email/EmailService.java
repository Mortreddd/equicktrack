package com.it43.equicktrack.email;

import com.it43.equicktrack.exception.EmailMessageException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${frontend.url}")
    private String frontendUrl;


    public void sendVerifyEmail(String email, String uuid) throws EmailMessageException {
        Context context = new Context();
        String resetPasswordUrl = String.format("%s/auth/verify-email/%s", frontendUrl, uuid);
        context.setVariable("resetPasswordUrl", resetPasswordUrl);
        String process = templateEngine.process("email-verification", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Email Verification");
            mimeMessageHelper.setText(process, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailMessageException("Can't send an email: " + e.getMessage());
        }
    }

    public void sendResetPassword(String email, String uuid) throws EmailMessageException {
        Context context = new Context();
        String resetPasswordUrl = frontendUrl + "/auth/forgot-password/" + uuid;
        context.setVariable("resetPasswordUrl", resetPasswordUrl);
        String process = templateEngine.process("reset-password", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Reset Password");
            mimeMessageHelper.setText(process, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailMessageException("Can't send an email: " + e.getMessage());
        }

    }
}
