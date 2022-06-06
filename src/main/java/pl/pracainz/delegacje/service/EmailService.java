package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String userEmail,
                         String subject,
                         String htmlBody,
                         String userPassword) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(getRenderedEmailTemplate(htmlBody, userEmail, userPassword), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getRenderedEmailTemplate(String template, String email, String password) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("password", password);
        String body = templateEngine.process(template, context);
        return body;
    }

}
