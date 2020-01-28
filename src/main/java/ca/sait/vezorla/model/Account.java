/**
 * Class to represent an account for
 * a client (more details included),
 * or a customer (less details included).
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountNum;

    @Column(name = "account_type")
    private char accountType;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone_num")
    @Pattern(regexp = "((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}")
    private String phoneNum;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postalCode")
    @Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    private String postalCode;

    @Column(name = "password")
    private String password;

    @Column(name = "user_created", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean userCreated;

    @Column(name = "account_admin", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean accountAdmin;

    @Column(name = "is_confirmed", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isConfirmed;
    
    //Cart and Invoice
}
