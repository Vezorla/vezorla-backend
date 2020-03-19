package ca.sait.vezorla.service;

public interface EmailServices {

    void sendContactUsEmail(String sender, String message);
    void sendAccountConfirmationEmail(String to, String additionText);
    void sendDeclineEmail(String to, String additionText);
    void sendForgotPassword(String email, String tempPassword);
    void sendPurchaseOrderEmail(String to, String additionText);
    void sendSubscriptionEmail(String to, String additionText);
}
