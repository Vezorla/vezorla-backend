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
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Entity
@Data
@Table(name = "cart")
public class Cart implements Serializable {

    @Id
    @Column(name = "order_num")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNum;

    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account account;

    @Column(name = "from_account")
    private boolean fromAccount;

    @JsonIgnore
    @OneToMany(mappedBy = "cart")
    @Column
    private List<LineItem> lineItems;

    /**
     * No args constructor to create a new list of line items
     */
    public Cart() {
        this.lineItems = new ArrayList<>();
    }
}
