package xeubiart.com.infra.emailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xeubiart.com.infra.emailSender.dto.EmailDuplicatedRegisterDTO;
import xeubiart.com.infra.emailSender.dto.EmailSendDTO;
import xeubiart.com.infra.emailSender.dto.EmailVerificationCodeDTO;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    @Async
    public void sendEmail(EmailSendDTO dto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom("help@xeubiart.com");
        helper.setTo(dto.getTo());
        helper.setSubject(dto.getTitle());
        helper.setText(dto.getContent(), true);

        mailSender.send(message);
    }

    public void sendConfirmationEmail(EmailVerificationCodeDTO dto) throws MessagingException {
        Context context = new Context();
        context.setVariable("code", dto.getCode());
        context.setVariable("email", dto.getEmail());
        context.setVariable("name", dto.getName());

        String htmlContent = templateEngine.process("email-verification", context);

        this.sendEmail(EmailSendDTO.builder()
                .to(dto.getEmail())
                .title("Seu código de acesso Xeubiart")
                .content(htmlContent)
                .build()
        );
    }

    public void sendDuplicatedRegisterEmail(EmailDuplicatedRegisterDTO dto) throws MessagingException {
        Context context = new Context();
        context.setVariable("email", dto.getEmail());
        context.setVariable("name", dto.getName());

        String htmlContent = templateEngine.process("email-duplicated", context);

        this.sendEmail(EmailSendDTO.builder()
                .to(dto.getEmail())
                .title("Tentativa de registro duplicada")
                .content(htmlContent)
                .build()
        );
    }

}
