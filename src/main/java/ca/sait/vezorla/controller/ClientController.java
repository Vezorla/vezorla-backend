package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ClientController.URL)
public class ClientController {

    protected static final String URL = "/client/";

    @GetMapping("settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping("account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("account/confirm")
    public String getAccountConfirmationPage(Long id) {
        return null;
    }

    @GetMapping("shop")
    public String getShopPage() {
        return null;
    }

    @GetMapping("account/orders/history")
    public String getOrderHistoryPage() {
        return null;
    }

}
