package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Invoice;

public interface EmailServices {

    void sendContactUsEmail(String name, String senderEmail, String message) throws InvalidInputException;

    void sendCreateAccountEmail(String senderEmail) throws InvalidInputException;

    void sendDeclineEmail(String to, String additionText);

    void sendForgotPassword(String email, String tempPassword) throws InvalidInputException;

    void sendInvoiceEmail(String to, Invoice invoice, double total) throws InvalidInputException;

    void sendSubscriptionEmail(String to, String additionText);

    boolean verifyEmail(String email) throws InvalidInputException;
}
