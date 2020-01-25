/**
 *  Class for creating emails from the Vezorla
 *  web application. These objects are
 *  NOT stored in the database.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.Data;

@Data
public class Email {

    @javax.validation.constraints.Email
    private String fromEmail;
    @javax.validation.constraints.Email
    private String toEmail;
    private String body;
    private String subject;
}
