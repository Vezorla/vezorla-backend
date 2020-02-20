package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.Product;
import ca.sait.vezorla.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ClientRestController.URL)
public class ClientRestController {

    protected static final String URL = "/api/client/";

    @Autowired
    private UserServices userServices;

    @GetMapping("find/{id}")
    public void findById(@PathVariable Long id) {

    }

    @GetMapping("settings")
    public String getSettingsPage() {
        return null;
    }

    @GetMapping("account/update")
    public String updateAccount(Account account, Model model, BindingResult bindingResult) {
        return null;
    }

    @GetMapping("order/{id}")
    public List<Invoice> getOrder(@PathVariable Long id) {
        return null;
    }

}
