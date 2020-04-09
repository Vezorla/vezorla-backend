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
 * <p>
 * Class to represent an account for
 * a client (more details included),
 * or a customer (less details included).
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
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
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'account'
     */
    @Id
    private String email;

    /**
     * Account type.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "account_type")
    private char accountType;

    /**
     * Account holders last name.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Account holders first name.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Account holders phone number.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * Account holders address.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "address")
    private String address;

    /**
     * Account holders city.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "city")
    private String city;

    /**
     * Account holders province.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "province")
    private String province;

    /**
     * Account holders country.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "country")
    private String country;

    /**
     * Account holders postal code.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "postalCode")
    //@Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    private String postalCode;

    /**
     * Account password
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "password")
    private String password;

    /**
     * Account user created boolean.
     * <p>
     * Determines if the account is system or user
     * generated.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "user_created", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean userCreated;

    /**
     * Account admin boolean.
     * <p>
     * Determines if the account is an admin account.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "account_admin", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean accountAdmin;

    /**
     * Account confirmation boolean.
     * <p>
     * Determines if the account has been confirmed.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "is_confirmed", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isConfirmed;

    /**
     * Account subscription boolean.
     * <p>
     * Determines if the account is subscribed to
     * the Vezorla mailing list.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "is_subscript", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isSubscript;

    /**
     * Account pickup boolean.
     * <p>
     * Determines if the account holder will pickup
     * the order.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @Column(name = "pickup", columnDefinition = "BIT")
    private boolean pickup;

    /**
     * Account cart.
     * <p>
     * Contains the Accounts cart object.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @Column(name = "cart")
    @OneToMany(mappedBy = "account")
    private List<Cart> carts;

    /**
     * Account invoices.
     * <p>
     * Contains the Invoices of the Account.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @Column(name = "invoice")
    @OneToMany(mappedBy = "account")
    private List<Invoice> invoices;

    /**
     * Account discounts.
     * <p>
     * Contains the Discounts within the Account.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database via email.
     * <p>
     * Annotated as a column within the database
     * table 'account'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "email")
    private List<AccountDiscount> accountDiscounts;

    /**
     * Constructor for getting shipping information from a customer.
     *
     * @param email      email
     * @param lastName   last name
     * @param firstName  first name
     * @param phoneNum   phone number
     * @param address    address
     * @param city       city
     * @param province   province
     * @param country    country
     * @param postalCode postal
     * @author matthewjflee, jjrr1717
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
     * @param email    email
     * @param password password
     * @author matthewjflee
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
     * <p>
     * Ensures the account created is a customer.
     *
     * @param email email
     * @author matthewjflee
     */
    public Account(String email) {
        this.email = email;
        this.accountType = 'C';
    }
}