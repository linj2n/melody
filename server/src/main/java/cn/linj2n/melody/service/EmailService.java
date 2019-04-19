package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.User;

public interface EmailService {

    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendPasswordResetMail(User user, String baseUrl);
}
