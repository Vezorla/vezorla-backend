
package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * LineItem class.
 * <p>
 * Class that represents a line item on in a cart
 * and on an invoice. This is a product that is
 * being purchased and all the information
 * associated with that purchase.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
 * @author jjrr1717
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "line_item")
public class LineItem implements Serializable {

    /**
     * LineItem number.
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'line_item'
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_num")
    private Long lineNum;

    /**
     * LineItem quantity.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * LineItem current name.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @Column(name = "current_name")
    private String currentName;

    /**
     * LineItem current price.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @Column(name = "current_price")
    private long currentPrice;

    /**
     * LineItem order number.
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "order_num")
    private Cart cart;

    /**
     * LineItem invoice number.
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @ManyToOne
    @JoinColumn(name = "invoice_num")
    private Invoice invoice;

    /**
     * LineItem lot number.
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @ManyToOne
    @JoinColumn(name = "lot_num")
    private Lot lot;

    /**
     * LineItem product number.
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'line_item'
     */
    @ManyToOne
    @JoinColumn(name = "prod_num")
    private Product product;

    /**
     * Constructor for LineItem.
     *
     * @param quantity     Quantity of product in line item.
     * @param currentName  Name of the line item.
     * @param currentPrice Price of the line item
     * @param cart         Cart of the line item.
     * @param product      Product linked to the line item.
     */
    public LineItem(int quantity, String currentName, long currentPrice, Cart cart, Product product) {
        this.quantity = quantity;
        this.currentName = currentName;
        this.currentPrice = currentPrice;
        this.cart = cart;
        this.product = product;
    }

}
