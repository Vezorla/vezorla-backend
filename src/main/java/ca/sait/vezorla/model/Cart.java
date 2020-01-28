/**
 * Class to represent the shopping cart
 * on Vezorla's web application.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Cart {

    @Id
    @Column(name = "order_num")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderNum;

    @Column(name = "account")
    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account account;

    @Column(name = "from_account")
    private boolean fromAccount;

    @OneToMany(mappedBy = "order_num")
    private List<LineItem> lineItems;
}
