package ca.sait.vezorla.controller;

import ca.sait.vezorla.controller.util.CustomerClientUtil;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.service.PaypalServices;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller for paypal payments
 *
 * Taken from: https://github.com/Java-Techie-jt/spring-boot-paypal-example/blob/master/src/main/java/com/javatechie/spring/paypal/api/PaypalController.java
 */
@Controller
@CrossOrigin
public class PaypalController {

    public static final String SUCCESS_URL = "cart/payment/success";
    public static final String CANCEL_URL = "cart/payment";
    PaypalServices paypalServices;

    public PaypalController(PaypalServices paypalServices){
        this.paypalServices = paypalServices;
    }

    @PostMapping("/customer/cart/payment")
    public String makePayment(HttpServletRequest request){
        HttpSession session = request.getSession();
        CustomerClientUtil ccu = new CustomerClientUtil();
        Invoice invoice = (Invoice) session.getAttribute("INVOICE");

        String formattedTotal = ccu.formatAmount(invoice.getTotal());
        double totalAsDouble = Double.parseDouble(formattedTotal);

        try {
            Payment payment = paypalServices.createPayment(10.0, "USD", "paypal",
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
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
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
