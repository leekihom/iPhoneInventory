package com.apple.iphoneinventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Author leezihong
 * @Date 2024/9/23 12:39
 * @Version 1.0
 * @description TODO
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2578527296@qq.com");
        message.setTo("2578527296@qq.com");
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }
}