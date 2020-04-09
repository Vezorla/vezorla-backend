package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * AccountDiscount class.
 * <p>
 * Account_Discount bridging table modeling.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column.
 *
 * @author matthewjflee, jjrr1717
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@IdClass(AccountDiscount.class)
@Table(name = "account_discount")
public class AccountDiscount implements Serializable {

    /**
     * Account email.
     * <p>
     * Annotated to be a Foreign Key for the database
     * table 'account_discount'
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "account_email")
    private Account email;

    /**
     * Discount code.
     * <p>
     * Annotated to be a Foreign Key for the database
     * table 'account_discount'
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "discount_code")
    private Discount code;
}
