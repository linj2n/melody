package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.User;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendPasswordResetLinkToUserMail(User user, String baseUrl);

    void sendVerificationCode(User user, String baseUrl);
}
