package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Account_Discount bridging table modeling
 *
 * @author matthewjflee, jjrr1717
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "account_discount")
public class AccountDiscount implements Serializable {

//    @Id
//    @Column(name="account_email")
//    private String accountEmail;
//
//    @Id
//    @Column(name="discount_code")
//    private String discountCode;

//    @Id
//    @ManyToOne
//    @PrimaryKeyJoinColumn(name="email", referencedColumnName = "email")
//    private Account email;
//
//    @Id
//    @ManyToOne
//    @PrimaryKeyJoinColumn(name="code", referencedColumnName = "code")
//    private Discount code;

    @Id
    @ManyToOne
    @JoinColumn(name="account_email")
    private Account email;

    @Id
    @ManyToOne
    @JoinColumn(name="discount_code")
    private Discount code;

}
