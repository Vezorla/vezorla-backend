package ca.sait.vezorla.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service to send emails
 *
 * @author: matthewjflee
 */
@Service
@AllArgsConstructor
public class EmailServicesImp implements EmailServices{

    private JavaMailSender mailSender;

    public void sendContactUsEmail(String sender, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("vezorla.test@gmail.com");
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Message from " + sender);
        mail.setText(message);

        mailSender.send(mail);
    }

    public void sendAccountConfirmationEmail(String to, String additionText) {

    }

    public void sendDeclineEmail(String to, String additionText) {

    }

    public void sendForgotPassword(String email, String tempPassword) {

    }

    public void sendPurchaseOrderEmail(String to, String additionText) {

    }

    public void sendSubscriptionEmail(String to, String additionText) {

    }

}
