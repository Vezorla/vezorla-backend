package ca.sait.vezorla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AuthenticationController.URL)
public class AuthenticationController {

    protected static final String URL = "/client/";

    @GetMapping("login/auth")
    public void login(String email, String password) {

    }

    @GetMapping("login")
    public String getLoginPage() {
        return null;
    }

    @GetMapping("logout")
    public void logout() {

    }

}
