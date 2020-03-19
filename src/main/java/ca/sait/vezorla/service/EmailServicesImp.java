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
     * @param senderEmail
     * @param message
     * @throws MailException
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

    public void sendForgotPassword(String email, String tempPassword) {

    }

    public void sendInvoiceEmail(String to, Invoice invoice, double total) throws MailException,
                                                                InvalidInputException {
        verifyEmail(to);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setFrom("vezorla.test@gmail.com");
        mail.setSubject("Your Vezorla Receipt");


        //Create header
        String msgHeader = "Hello " + invoice.getAccount().getFirstName() + "," +
                "\nYour order details are indicated below.\n\n" +
                "Order Details\n" +
                "Invoice #" + invoice.getInvoiceNum() + "\n" +
                "Placed on " + invoice.getDate();

        //Append Line Items
        StringBuilder sb = new StringBuilder(msgHeader);

        List<LineItem> lineItems = lineItemRepo.findLineItemByInvoice(invoice);

        for(LineItem li : lineItems) {
            sb.append(li.getCurrentName())
                    .append("\nQuantity: ")
                    .append(li.getQuantity())
                    .append("\tPrice: ")
                    .append(li.getExtendedPrice());
        }

        CustomerClientUtil ccu = new CustomerClientUtil();

        //Append pricing
        sb.append("\n\n\t\t\t")
                .append("Item Subtotal:")
                .append("\tCDN$")
                .append(ccu.formatAmount(invoice.getSubtotal()));
        sb.append("\n\n\t\t\t")
                .append("Shipping & Handling:")
                .append("\tCDN$")
                .append(ccu.formatAmount(invoice.getShippingCost()));
        sb.append("\n\n\t\t\t")
                .append("Discount:")
                .append("\tCDN$")
                .append(ccu.formatAmount(invoice.getDiscount()));
        sb.append("\n\n\t\t\t")
                .append("Taxes:")
                .append("\tCDN$")
                .append(ccu.formatAmount(invoice.getTaxes()));
        sb.append("\n\n\t\t\t")
                .append("Total:")
                .append("\tCDN$").append(total);

        String message = sb.toString();
        System.out.println(message);
    }

    public void sendSubscriptionEmail(String to, String additionText) {

    }

    /**
     * Verify email
     *
     * @author: matthewjflee
     * @param email
     * @return
     * @throws InvalidInputException
     */
    public boolean verifyEmail(String email) throws InvalidInputException {
        CustomerClientUtil ccu = new CustomerClientUtil();
        return ccu.validateEmail(email);
    }
}
