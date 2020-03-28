package ca.sait.vezorla.controller;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.EmailServices;
import ca.sait.vezorla.service.PaypalServices;
import ca.sait.vezorla.service.UserServices;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller for PayPal payments
 * Taken from: https://github.com/Java-Techie-jt/spring-boot-paypal-example/blob/master/src/main/java/com/javatechie/spring/paypal/api/PaypalController.java
 *
 * @author jjrr1717
 */
@AllArgsConstructor
@Controller
@CrossOrigin
public class PayPalController {

    public static final String SUCCESS_URL = "cart/payment/success";
    public static final String CANCEL_URL = "cart/payment";

    private PaypalServices paypalServices;
    private UserServices userServices;
    private EmailServices emailServices;

    /**
     * Method to make the payment through PayPal
     *
     * @param request the session
     * @return a String to redirect user
     * @throws UnauthorizedException if user is not authorized
     * @author jjrr1717
     */
    @PostMapping("/customer/cart/payment")
    public String makePayment(HttpServletRequest request) throws UnauthorizedException, InvalidInputException {
        HttpSession session = request.getSession();

        userServices.decreaseInventory(session);
        userServices.applyDiscount(session);
        Invoice newInvoice = userServices.saveInvoice(session);
        userServices.applyLineItemsToInvoice(newInvoice);


        userServices.saveLineItems(session, newInvoice);

        //check to ensure all previous steps have been performed
        if (session.getAttribute("INVOICE") == null) {
            throw new UnauthorizedException();
        }

        CustomerClientUtil ccu = new CustomerClientUtil();
        Invoice invoice = (Invoice) session.getAttribute("INVOICE");

        String formattedTotal = ccu.formatAmount(invoice.getTotal());
        double totalAsDouble = Double.parseDouble(formattedTotal);

        //divide by 100 for total
        double finalTotal = (double) newInvoice.getTotal() / 100;

        try {
            Payment payment = paypalServices.createPayment(finalTotal, "CAD", "paypal",
                    "sale", "Place Order", "http://localhost:3000/" + CANCEL_URL,
                    "http://localhost:3000/" + SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        System.out.println("Made payment");

        //Send email
        emailServices.sendInvoiceEmail(invoice.getAccount().getEmail(), invoice, totalAsDouble);

        return "redirect:/";
    }

    /**
     * Method if the payment was cancelled
     *
     * @return cancel URL
     * @author jjrr1717
     */
    @GetMapping(value = "/customer/cart/payment/cancel")
    public String cancelPay() {
        System.out.println("Cancelled");
        return "redirect:/";
    }

    /**
     * Method if the payment was successful
     *
     * @param paymentId payment ID
     * @param payerId   payer's ID
     * @param request   user request
     * @return success URL
     * @author jjrr1717
     */
    @GetMapping(value = "/customer/cart/payment/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request) {
        try {
            Payment payment = paypalServices.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Success: " + paymentId);


        return "redirect:/";
    }
}
