package ca.sait.vezorla.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * PaypalServices class.
 *
 * This class acts as the intermediary between the controllers
 * and the repositories.
 *
 * Contains all services to handle PayPal payments.
 *
 * Taken from: https://github.com/Java-Techie-jt/spring-boot-paypal-example/blob/master/src/main/java/com/javatechie/spring/paypal/api/PaypalService.java
 */
@Service
public class PaypalServices {

    private APIContext apiContext;

    /**
     * Constructor that requires the PayPal APIContext.
     *
     * @param apiContext PayPal API.
     */
    public PaypalServices(APIContext apiContext){
        this.apiContext = apiContext;
    }

    /**
     * Creates a payment in the PayPal API.
     *
     * @param total Total of the order.
     * @param currency Type of currency used.
     * @param method Method of payment.
     * @param intent Intent of the payment.
     * @param description Order description.
     * @param cancelUrl URL to cancel the payment.
     * @param successUrl URL if payment is successful.
     * @return Payment object used for payments.
     * @throws PayPalRESTException If PayPal API error occurs.
     */
    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    /**
     * Executes the payment within the PayPal API.
     *
     * @param paymentId ID of payment.
     * @param payerId ID of payer.
     * @return Payment Object for payments.
     * @throws PayPalRESTException If PayPal API error occurs.
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
