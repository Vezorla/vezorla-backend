package ca.sait.vezorla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(AuthenticationRestController.URL)
public class AuthenticationRestController {

    protected static final String URL = "/api/login/";

    @GetMapping("auth")
    public void login(String email, String password) {

    }

    @GetMapping("logout")
    public void logout() {

    }

    /**
     * Reopening a tab after logging in
     */
    @GetMapping
    public void checkSession(HttpServletRequest request) {
        //Return minh what the current role is
        //Prevent clients from admin
    }

}
