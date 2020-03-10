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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("login")
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
     * Check the current role of the authenticated user
     * @author: matthewjflee
     * @param session
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("checkRole")
    public ResponseEntity<String> checkRole(HttpSession session) throws JsonProcessingException {
        Account account = (Account) session.getAttribute("ACCOUNT");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("admin", account.isAccountAdmin());
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        return ResponseEntity.ok().body(output);
    }

    @GetMapping("logout")
    public void logout() {

    }

}
