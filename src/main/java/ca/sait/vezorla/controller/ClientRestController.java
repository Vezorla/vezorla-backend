package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientRestController {

    private final String URL = "/client/";

    @GetMapping(URL + "find/{id}")
    public void findById(@PathVariable Long id) {

    }

    @GetMapping(URL + "settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping(URL + "account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping(URL + "order/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
