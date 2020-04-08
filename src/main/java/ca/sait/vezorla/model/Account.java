package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Account class.
 *
 * Class to represent an account for
 * a client (more details included),
 * or a customer (less details included).
 *
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
 * @author Jocelyn Wegen
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "account")
public class Account implements Serializable {

    /**
     * Account email.
     *
     * Annotated to be the Primary Key for the database
     * table 'account'
     */
    @Id
    private String email;

    /**
     * Account type.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "account_type")
    private char accountType;

    /**
     * Account holders last name.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Account holders first name.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Account holders phone number.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * Account holders address.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "address")
    private String address;

    /**
     * Account holders city.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "city")
    private String city;

    /**
     * Account holders province.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "province")
    private String province;

    /**
     * Account holders country.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "country")
    private String country;

    /**
     * Account holders postal code.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "postalCode")
    //@Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    private String postalCode;

    /**
     * Account password
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "password")
    private String password;

    /**
     * Account user created boolean.
     *
     * Determines if the account is system or user
     * generated.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "user_created", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean userCreated;

    /**
     * Account admin boolean.
     *
     * Determines if the account is an admin account.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "account_admin", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean accountAdmin;

    /**
     * Account confirmation boolean.
     *
     * Determines if the account has been confirmed.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "is_confirmed", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isConfirmed;

    /**
     * Account subscription boolean.
     *
     * Determines if the account is subscribed to
     * the Vezorla mailing list.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "is_subscript", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isSubscript;

    /**
     * Account pickup boolean.
     *
     * Determines if the account holder will pickup
     * the order.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "pickup", columnDefinition = "BIT")
    private boolean pickup;

    /**
     * Account cart.
     *
     * Contains the Accounts cart object.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @Column(name = "cart")
    @OneToMany(mappedBy = "account")
    private List<Cart> carts;

    /**
     * Account invoices.
     *
     * Contains the Invoices of the Account.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @Column(name = "invoice")
    @OneToMany(mappedBy ="account")
    private List<Invoice> invoices;

    /**
     * Account discounts.
     *
     * Contains the Discounts within the Account.
     *
     * Mapped by a one-to-many relationship within
     * the database via email.
     *
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "email")
    private List<AccountDiscount> accountDiscounts;

     /**
     * Constructor for getting shipping information from a customer.
     *
     * @author matthewjflee, jjrr1717
     * @param email email
     * @param lastName last name
     * @param firstName first name
     * @param phoneNum phone number
     * @param address address
     * @param city city
     * @param province province
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
     * Constructor for creating account.
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
     * Constructor for subscribing email.
     *
     * Ensures the account created is a customer.
     *
     * @author matthewjflee
     * @param email email
     */
    public Account(String email) {
        this.email = email;
        this.accountType = 'C';
    }
}