package ca.sait.vezorla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    private final String URL = "/client/";

    @GetMapping(URL + "login/auth")
    public void login(String email, String password) {

    }

    @GetMapping(URL + "login")
    public String getLoginPage() {
        return null;
    }

    @GetMapping(URL + "logout")
    public void logout() {

    }

}
