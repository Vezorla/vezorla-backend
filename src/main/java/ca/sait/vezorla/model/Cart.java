/**
 * Class to represent the shopping cart
 * on Vezorla's web application.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "cart")
public class Cart {

    @Id
    @Column(name = "order_num")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNum;

    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account account;

    @Column(name = "from_account")
    private boolean fromAccount;

    @OneToMany(mappedBy = "cart")
    @Column
    private List<LineItem> lineItems;
}
