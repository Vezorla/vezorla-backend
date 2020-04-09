package ca.sait.vezorla.controller;

import ca.sait.vezorla.exception.AccountNotFoundException;
import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.service.AuthenticationServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * AuthenticationRestController Class.
 * <p>
 * This class maps the frontend to the backend. It does
 * so by implementing Spring RestController annotation.
 * <p>
 * Lombok is used to reduce boilerplate code for constructors.
 *
 * @author matthewjflee
 */
@AllArgsConstructor
@RestController
@RequestMapping(AuthenticationRestController.URL)
public class AuthenticationRestController {

    protected static final String URL = "/api/auth/";
    private AuthenticationServices authServices;

    /**
     * Find an account with that email and password.
     *
     * @param body    Contains the login information
     * @param request HTTP request
     * @return email and role of client
     * @throws JsonProcessingException If JSON cannot be processed
     * @author matthewjflee
     */
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody String body, HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        String email = null;
        String password = null;

        //Grab email and password from HTTP body
        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
        } catch (ParseException ignored) {
        }

        //Find account in DB
        Optional<Account> findAccount = authServices.login(email, password, session);
        if (findAccount.isPresent()) {
            Account account = findAccount.get();
            node.put("email", account.getEmail());
            node.put("admin", account.isAccountAdmin());
        }

        //Create custom JSON for return
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        return ResponseEntity.ok().body(output);
    }

    /**
     * Check the current role of the authenticated user.
     *
     * @param request: HTTP request
     * @return role
     * @throws JsonProcessingException thrown if there is a problem parsing JSON
     * @author matthewjflee
     */
    @GetMapping("check-role")
    public ResponseEntity<String> checkRole(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        //Grab account
        Account account = (Account) session.getAttribute("ACCOUNT");
        Boolean tempAccount = (Boolean) session.getAttribute("TEMP-ACCOUNT");

        if (tempAccount == null && account != null)
            node.put("admin", account.isAccountAdmin());
        else
            throw new AccountNotFoundException();

        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        return ResponseEntity.ok().body(output);
    }

    /**
     * Logout.
     * Grab the session by passing <code>false</code>
     * This way, it does not create a session if one is not existing
     * Will invalidate and check afterwards.
     *
     * @param request Http Request
     * @return result result if the session was invalidated
     * @author matthewjflee
     */
    @DeleteMapping("logout")
    public boolean logout(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession(false);
        session.invalidate();
        session = request.getSession(false); //Need second session getting to ensure session is invalidated.

        if (session == null)
            result = true;

        return result;
    }

    /**
     * Password reset self service tool for a client.
     *
     * @param email account email
     * @return if the password was reset
     * @throws InvalidInputException error if email is invalid
     * @author kwistech
     */
    @PutMapping("account/forgot-password")
    public boolean forgotPassword(@RequestBody String email) throws InvalidInputException {
        email = email.replaceAll("\"", "");
        return authServices.forgotPassword(email);
    }
}
