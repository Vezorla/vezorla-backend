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

    /**
     * Send contact us email to vezorla.test@gmail.com
     *
     * @author: matthewjflee
     * @param senderEmail
     * @param message
     * @throws MailException
     */
    public void sendContactUsEmail(String name, String senderEmail, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("vezorla.test@gmail.com");
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Message from " + name);
        mail.setText("Contact-Us Message"+ "\n\n" +
                "Sender's Name: " + name + "\n\n" +
                "Sender's email: " + senderEmail + "\n\n" +
                "Message: " + message);

        mailSender.send(mail);
    }

    public void sendAccountConfirmationEmail(String to, String additionText) {

    }

    public void sendDeclineEmail(String to, String additionText) {

    }

    public void sendForgotPassword(String email, String tempPassword) {

    }

    public void sendInvoiceEmail(String to, String additionText) {

    }

    public void sendSubscriptionEmail(String to, String additionText) {

    }

}
