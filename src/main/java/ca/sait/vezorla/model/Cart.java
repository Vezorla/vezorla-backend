/**
 * Class to represent the shopping cart
 * on Vezorla's web application.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart class.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 * <p>
 * User's cart.
 *
 * @author matthewjflee, jjrr1717
 */
@AllArgsConstructor
@Entity
@Data
@Table(name = "cart")
public class Cart implements Serializable {

    /**
     * Cart order number.
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'cart'
     */
    @Id
    @Column(name = "order_num")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNum;

    /**
     * Cart account number.
     * <p>
     * Annotated as a column within the database
     * table 'cart'
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     */
    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account account;

    /**
     * Cart account.
     * <p>
     * Annotated as a column within the database
     * table 'cart'
     */
    @Column(name = "from_account")
    private boolean fromAccount;

    /**
     * Cart line items.
     * <p>
     * Annotated as a column within the database
     * table 'cart'
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.ALL})
    @Column
    private List<LineItem> lineItems;

    /**
     * No args constructor to create a new list of line items
     */
    public Cart() {
        this.lineItems = new ArrayList<>();
    }

    /**
     * Argument constructor to fulfill Cart creation upon
     * Account creation.
     *
     * @param account Account to be linked to the cart.
     */
    public Cart(Account account) {
        this.account = account;
        this.fromAccount = true;
        this.lineItems = new ArrayList<>();
    }
}
