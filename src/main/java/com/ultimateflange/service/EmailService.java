package com.ultimateflange.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderNotification(String orderDetails) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("mushaabkhan894@gmail.com"); // jaha mail aani hai
        message.setSubject("New Order Received");
        message.setText(orderDetails);

        mailSender.send(message);
    }
}