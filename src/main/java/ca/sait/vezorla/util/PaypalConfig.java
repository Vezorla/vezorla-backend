package ca.sait.vezorla.util;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * PayPalConfig class.
 *
 * This class configures the PayPal API to work
 * with the Vezorla application.
 *
 * More info can be found at the link below.
 *
 * https://github.com/Java-Techie-jt/spring-boot-paypal-example/tree/master/src/main/java/com/javatechie/spring/paypal/api
 */
@Configuration
public class PaypalConfig {

    /**
     * Client ID for the PayPal API.
     */
    @Value("${paypal.client.id}")
    private String clientId;

    /**
     * Client secret for the PayPal API.
     */
    @Value("${paypal.client.secret}")
    private String clientSecret;

    /**
     * Mode of the PayPal API.
     * Sandbox = development mode.
     */
    @Value("${paypal.mode}")
    private String mode;

    /**
     * Sets the SDK Configurations of the PayPal API.
     *
     * @return Map that contains the SDK configurations.
     */
    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    /**
     * Sets the Authentication Tokens of the PayPal API.
     *
     * @return OAuthTokenCredential Valid PayPal credential.
     */
    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    /**
     * Gets the PayPal API Context to be used for
     * the application.
     *
     * @return APIContext PayPal API.
     * @throws PayPalRESTException If PayPal API runs an error.
     */
    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
