package ca.sait.vezorla.service;

import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Invoice;

import javax.mail.MessagingException;
import java.io.File;

/**
 * EmailServices interface.
 *
 * This interface outlines the services as it relates to
 * Emails.
 *
 * This interface acts as the intermediary between the controllers
 * and the repositories.
 */
public interface EmailServices {

    /**
     * Sends the message from the Contact Us page to the
     * admin email account.
     *
     * @param name Name of the sender.
     * @param senderEmail Email of the sender.
     * @param message Message of the sender.
     * @throws InvalidInputException If sender inputs are invalid.
     */
    void sendContactUsEmail(String name, String senderEmail, String message) throws InvalidInputException;

    /**
     * Sends the create account email.
     *
     * @param senderEmail Receivers email address.
     * @throws InvalidInputException If sender inputs are invalid.
     */
    void sendCreateAccountEmail(String senderEmail) throws InvalidInputException;

    void sendBackupEmail(String date, File file) throws MessagingException;

    /**
     * Sends the decline email.
     *
     * @param to Email to decline.
     * @param additionText Message of the email.
     */
    void sendDeclineEmail(String to, String additionText);

    /**
     * Sends the forgot password email.
     *
     * @param email Email to send to.
     * @param tempPassword Password to include in the email.
     * @throws InvalidInputException If email inputs are invalid.
     */
    void sendForgotPassword(String email, String tempPassword) throws InvalidInputException;

    /**
     * Sends an invoice in an email.
     *
     * @param to Receivers email address.
     * @param invoice Invoice to be sent.
     * @param total Total amount on the invoice.
     * @throws InvalidInputException If email inputs are invalid.
     */
    void sendInvoiceEmail(String to, Invoice invoice, double total) throws InvalidInputException;

    /**
     * Sends the subscription emails.
     *
     * @param to Receivers email address
     * @param additionText Message of the email.
     */
    void sendSubscriptionEmail(String to, String additionText);

    /**
     * Verifies the email address format.
     *
     * @param email Email to be verified.
     * @return boolean true if valid, false otherwise.
     * @throws InvalidInputException If email is invalid.
     */
    boolean verifyEmail(String email) throws InvalidInputException;
}
