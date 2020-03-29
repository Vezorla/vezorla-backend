package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class to represent an account for
 * a client (more details included),
 * or a customer (less details included).
 *
 * @author Jocelyn Wegen
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "account")
public class Account implements Serializable {

    @Id
    private String email;

    @Column(name = "account_type")
    private char accountType;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "postalCode")
    //@Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
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
    
    @Column(name = "is_subscript", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isSubscript;

    @Column(name = "pickup", columnDefinition = "BIT")
    private boolean pickup;

    @Column(name = "cart")
    @OneToMany(mappedBy = "account")
    private List<Cart> carts;

    @JsonIgnore
    @Column(name = "invoice")
    @OneToMany(mappedBy ="account")
    private List<Invoice> invoices;

    @JsonIgnore
    @OneToMany(mappedBy = "email")
    private List<AccountDiscount> accountDiscounts;

     /**
     * Constructor for gettings shipping information from a customer
     *
     * @author matthewjflee, jjrr1717
     * @param email email
     * @param lastName last name
     * @param firstName first name
     * @param phoneNum phone number
     * @param address address
     * @param city city
     * @param country country
     * @param postalCode postal
     */
    public Account(String email, String lastName, String firstName,
                   String phoneNum, String address, String city,
                   String country, String province, String postalCode) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.accountType = 'C';
        this.carts = new ArrayList<>();
        this.invoices = new ArrayList<>();
        this.accountDiscounts = new ArrayList<>();
    }

    /**
     * Constructor for creating account
     *
     * @author matthewjflee
     * @param email email
     * @param password password
     */
    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.userCreated = true;
        this.accountType = 'C';
        this.carts = new ArrayList<>();
        this.invoices = new ArrayList<>();
        this.accountDiscounts = new ArrayList<>();
    }

    /**
     * Constructor for subscribing email
     * Ensures the account created is a customer
     *
     * @author matthewjflee
     * @param email email
     */
    public Account(String email) {
        this.email = email;
        this.accountType = 'C';
    }
}