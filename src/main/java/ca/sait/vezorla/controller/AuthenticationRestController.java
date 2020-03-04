package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.service.AuthenticationServicesImp;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Rest controller to handle customer authentication
 *
 * @author matthewjflee
 */
@AllArgsConstructor
@RestController
@RequestMapping(AuthenticationRestController.URL)
public class AuthenticationRestController {

    protected static final String URL = "/api/login/";

    private AuthenticationServicesImp authServices;

    /**
     * Login
     * Send a JSON of email and password
     *
     * @param httpEntity
     * @return
     * @author matthewjflee
     */
    @GetMapping("auth")
    public ResponseEntity<Account> login(HttpEntity<String> httpEntity) {
        String json = httpEntity.getBody();
        String email = null;
        String password = null;

        try {
            Object obj = new JSONParser().parse(json);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
        } catch (ParseException e) {
        }


        Optional<Account> account = authServices.login(email, password);
        return account.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

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
