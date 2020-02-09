/**
 * Class that represents a line item on in a cart
 * and on an invoice. This is a product that is
 * being purchased and all the information
 * associated with that purchase.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "line_item")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_num")
    private Long lineNum;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "current_name")
    private String currentName;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "extended_price")
    @Formula(value = "current_price * quantity")
    private BigDecimal extendedPrice;

    @ManyToOne
    @JoinColumn(name = "order_num", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "invoice_num")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "lot_num")
    private Lot lot;

}
