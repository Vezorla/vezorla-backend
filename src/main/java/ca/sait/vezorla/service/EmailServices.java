package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;

public interface EmailServices {

    void sendContactUsEmail(String name, String senderEmail, String message) throws InvalidInputException;

    void sendAccountConfirmationEmail(String to, String additionText);

    void sendDeclineEmail(String to, String additionText);

    void sendForgotPassword(String email, String tempPassword);

    void sendInvoiceEmail(String to, String additionText);

    void sendSubscriptionEmail(String to, String additionText);
}
