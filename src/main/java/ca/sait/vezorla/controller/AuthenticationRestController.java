package ca.sait.vezorla.controller;

import ca.sait.vezorla.model.Account;
import ca.sait.vezorla.service.AuthenticationServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(AuthenticationRestController.URL)
public class AuthenticationRestController {

    protected static final String URL = "/api/auth/";
    private AuthenticationServices authServices;

    /**
     * Find an account with that email and password
     *
     * @return
     * @author matthewjflee
     */
    @GetMapping("login")
    public ResponseEntity<String> login(@RequestBody String body, HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        String email = null;
        String password = null;

        //Grab email and password from HTTP body
        try {
            Object obj = new JSONParser().parse(body);
            JSONObject jo = (JSONObject) obj;
            email = (String) jo.get("email");
            password = (String) jo.get("password");
        } catch (ParseException e) {
        }

        //Find account in DB
        Optional<Account> account = authServices.login(email, password, session);

        //Create custom JSON for return
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("email", account.get().getEmail());
        node.put("admin", account.get().isAccountAdmin());
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        return ResponseEntity.ok().body(output);
    }

    /**
     * Method to check if the account has been persisted in the session properly
     * Testing purposes
     *
     * @author: matthewjflee
     * @param session
     */
    @GetMapping("check")
    public void check(HttpSession session) {
        Account account = (Account) session.getAttribute("ACCOUNT");
        System.out.println(account.getEmail());
    }

    /**
     * Logout
     * Grab the session by passing <code>false</code>
     * This way, it does not create a session if one is not existing
     *
     * Will invalidate and check afterwards
     * @param request Http Request
     * @return result result if the session was invalidated
     */
    @GetMapping("logout")
    public boolean logout(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession(false);
        session.invalidate();
        session = request.getSession(false);

        if(session == null)
            result = true;

        return result;
    }

}
