package ca.sait.vezorla.service;

public interface EmailServices {

    public void sendAccountConfirmationEmail(String to, String additionText);
    public void sendDeclineEmail(String to, String additionText);
    public void sendEmail(String to, String message);
    public void sendForgotPassword(String email, String tempPassword);
    public void sendPurchaseOrderEmail(String to, String additionText);
    public void sendSubscriptionEmail(String to, String additionText);
}
