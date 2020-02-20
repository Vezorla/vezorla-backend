package ca.sait.vezorla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
public class VezorlaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VezorlaApplication.class, args);
    }

}
