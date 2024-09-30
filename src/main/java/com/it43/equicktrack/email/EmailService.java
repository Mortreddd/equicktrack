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


    public void sendVerifyEmail(String email, String otpCode) throws EmailMessageException {
        Context context = new Context();
        String emailUrl = frontendUrl + "/verify-email/" + email;
        context.setVariable("otpCode", otpCode);
        context.setVariable("emailUrl", emailUrl);
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
}
