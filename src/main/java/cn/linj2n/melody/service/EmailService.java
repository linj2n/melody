package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.concurrent.Future;

@Service
public class EmailService {

    public static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String FROM_ADRRESS ;


    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSenderImpl javaMailSender;


    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        logger.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(FROM_ADRRESS);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            logger.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            logger.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

//    @Async
//    public void sendActivationEmail(User user, String baseUrl) {
//        logger.debug("Sending activation e-mail to '{}'", user.getEmail());
//        /* Only support zh_CN language */
//        Locale locale = new Locale("zh","CN");
//        Context context = new Context(locale);
//        context.setVariable("user", user);
//        context.setVariable("baseUrl", baseUrl);
//        String content = templateEngine.process("mails/activationEmail", context);
//        String subject = messageSource.getMessage("email.activation.title", null, locale);
//        sendEmail(user.getEmail(), subject, content, false, true);
//    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        logger.debug("Sending password reset e-mail to '{}'", user.getEmail());
        /* Only support zh_CN language */
        Locale locale = new Locale("zh","CN");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("mails/reset-password-email", context);
        String subject = messageSource.getMessage("email.resetPwd.title", null, locale);
        sendEmail(user.getEmail(),subject,content,false,true);
        logger.info("Execute method asynchronously. {}", Thread.currentThread().getName());
    }
}
