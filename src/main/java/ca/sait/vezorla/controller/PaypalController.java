package ca.sait.vezorla.controller;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.PaypalServices;
import ca.sait.vezorla.service.UserServices;
import ca.sait.vezorla.service.UserServicesImp;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller for paypal payments
 * JR is the number one
 *
 * Taken from: https://github.com/Java-Techie-jt/spring-boot-paypal-example/blob/master/src/main/java/com/javatechie/spring/paypal/api/PaypalController.java
 */
@Controller
@CrossOrigin
public class PaypalController {

    public static final String SUCCESS_URL = "cart/payment/success";
    public static final String CANCEL_URL = "cart/payment";
    PaypalServices paypalServices;

    private UserServices userServices;

    public PaypalController(PaypalServices paypalServices, UserServices userServices){
        this.paypalServices = paypalServices;
        this.userServices = userServices;
    }

    @PostMapping("/customer/cart/payment")
    public String makePayment(HttpServletRequest request) throws UnauthorizedException {
        HttpSession session = request.getSession();

        userServices.decreaseInventory(request);
        userServices.applyDiscount(request);
        Invoice newInvoice = userServices.saveInvoice(request);
        userServices.applyLineItemsToInvoice(newInvoice);


        userServices.saveLineItems(request, newInvoice);

        //check to ensure all previous steps have been performed
        if(session.getAttribute("INVOICE") == null){
            throw new UnauthorizedException();
        }

        CustomerClientUtil ccu = new CustomerClientUtil();
        Invoice invoice = (Invoice) session.getAttribute("INVOICE");

        String formattedTotal = ccu.formatAmount(invoice.getTotal());
        double totalAsDouble = Double.parseDouble(formattedTotal);

        try {
            Payment payment = paypalServices.createPayment(10.0, "CAD", "paypal",
                    "sale", "Place Order", "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        System.out.println("Made payment");

        return "redirect:/";
    }

    @GetMapping(value = "/customer/cart/payment/cancel")
    public String cancelPay() {
        System.out.println("Cancelled");
        return "redirect:/";
    }

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
