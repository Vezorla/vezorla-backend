/**
 * Class that represents a line item on in a cart
 * and on an invoice. This is a product that is
 * being purchased and all the information
 * associated with that purchase.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "line_item")
public class LineItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_num")
    private Long lineNum;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "current_name")
    private String currentName;

    @Column(name = "current_price")
    private long currentPrice;

    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "order_num")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "invoice_num")
    private Invoice invoice;


    @ManyToOne
    @JoinColumn(name = "lot_num")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "prod_num")
    private Product product;

    public LineItem(int quantity, String currentName, long currentPrice, Cart cart, Product product) {
        this.quantity = quantity;
        this.currentName = currentName;
        this.currentPrice = currentPrice;
        this.cart = cart;
        this.product = product;
    }

}
