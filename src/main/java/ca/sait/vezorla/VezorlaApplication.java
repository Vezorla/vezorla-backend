package ca.sait.vezorla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VezorlaApplication class.
 *
 * This class is the main class for the backend program.
 *
 * The SpringBootApplication annotation denotes this program
 * as a Spring Boot Application and uses Spring annotation
 * through the programs classes, methods, and variables.
 */
@SpringBootApplication
public class VezorlaApplication {

    /**
     * Main method of the VezorlaApplication.
     *
     * @param args Optional Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SpringApplication.run(VezorlaApplication.class, args);
    }

}
