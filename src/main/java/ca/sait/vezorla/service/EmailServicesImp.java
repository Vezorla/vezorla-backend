package ca.sait.vezorla.service;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.repository.LineItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to send emails
 *
 * @author: matthewjflee
 */
@Service
@AllArgsConstructor
public class EmailServicesImp implements EmailServices {

    private JavaMailSender mailSender;
    private LineItemRepo lineItemRepo;

    /**
     * Send contact us email to vezorla.test@gmail.com
     *
     * @param senderEmail user's email
     * @param message message to send
     * @throws MailException thrown when the email is not sent
     * @author: matthewjflee
     */
    public void sendContactUsEmail(String name, String senderEmail, String message) throws MailException,
                                                                                    InvalidInputException {
        //Validate email
        verifyEmail(senderEmail);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("vezorla.test@gmail.com");
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Message from " + name);
        mail.setText("Contact-Us Message" + "\n\n" +
                "Sender's Name: " + name + "\n\n" +
                "Sender's email: " + senderEmail + "\n\n" +
                "Message: " + message);

        mailSender.send(mail);
    }

    public void sendAccountConfirmationEmail(String to, String additionText) {

    }

    public void sendDeclineEmail(String to, String additionText) {

    }

    public void sendForgotPassword(String email, String tempPassword) throws InvalidInputException {

        //Validate email
        verifyEmail(email);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Vezorla - Password Reset");
        mail.setText("You have requested a password reset." + "\n\n" +
                "Your new password: " + tempPassword + "\n\n" +
                "Please use the above password to log into your Vezorla account.");

        mailSender.send(mail);

    }

    /**
     * Send invoice to user
     * @param to user's email
     * @param invoice user's invoice/receipt
     * @param total total cost
     * @throws MailException thrown if email is not sent
     * @throws InvalidInputException thrown if email is not valid
     */
    public void sendInvoiceEmail(String to, Invoice invoice, double total) throws MailException,
                                                                InvalidInputException {
        verifyEmail(to);

        //Set up email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Your Vezorla Receipt");

        //Create header
        String msgHeader = "Hello " + invoice.getAccount().getFirstName() + "," +
                "\nYour order details are indicated below.\n\n" +
                "Order Details\n" +
                "Invoice #" + invoice.getInvoiceNum() + "\n" +
                "Placed on " + invoice.getDate() + "\n\n" +
                "--------------------------------------------------------------------------\n";

        //Append Line Items
        StringBuilder sb = new StringBuilder(msgHeader);
        List<LineItem> lineItems = lineItemRepo.findLineItemByInvoice(invoice);
        CustomerClientUtil ccu = new CustomerClientUtil();

        for(LineItem li : lineItems) {
            sb.append(li.getCurrentName())
                    .append("\nQuantity: ")
                    .append(li.getQuantity())
                    .append("\t\tPrice:\t\t\t\tCDN$")
                    .append(ccu.formatAmount(li.getQuantity() * li.getCurrentPrice()))
                    .append("\n\n");
        }

        //Append pricing
        sb.append("\n\n")
                .append("--------------------------------------------------------------------------\n\n\t\t\t")
                .append("Item Subtotal:")
                .append("\t\t\t\tCDN$")
                .append(ccu.formatAmount(invoice.getSubtotal()));
        sb.append("\n\n\t\t\t")
                .append("Shipping & Handling:")
                .append("\t\tCDN$")
                .append(ccu.formatAmount(invoice.getShippingCost()));
        sb.append("\n\n\t\t\t")
                .append("Discount:")
                .append("\t\t\t\tCDN$")
                .append(ccu.formatAmount(invoice.getDiscount()));
        sb.append("\n\n\t\t\t")
                .append("Taxes:")
                .append("\t\t\t\t\tCDN$")
                .append(ccu.formatAmount(invoice.getTaxes()));
        sb.append("\n\n\t\t\t")
                .append("Total:")
                .append("\t\t\t\t\tCDN$").append(total);

        String message = sb.toString();
        mail.setText(message);

        mailSender.send(mail);
    }

    public void sendSubscriptionEmail(String to, String additionText) {

    }

    /**
     * Verify email
     *
     * @param email email to verify
     * @return if email is valid
     * @throws InvalidInputException if email is not valid
     * @author: matthewjflee
     */
    public boolean verifyEmail(String email) throws InvalidInputException {
        CustomerClientUtil ccu = new CustomerClientUtil();
        return ccu.validateEmail(email);
    }
}
