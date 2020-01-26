package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    private final String URL = "/client/";

    @GetMapping(URL + "settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping(URL + "account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "account/confirm")
    public String getAccountConfirmationPage(Long id) {
        return null;
    }

    @GetMapping(URL + "shop")
    public String getShopPage() {
        return null;
    }

    @GetMapping(URL + "account/orders/history")
    public String getOrderHistoryPage() {
        return null;
    }

}
