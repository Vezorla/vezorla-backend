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
@IdClass(AccountDiscount.class)
@Table(name = "account_discount")
public class AccountDiscount implements Serializable {

//    @Id
//    @Column(name="account_email")
//    private String accountEmail;
//
//    @Id
//    @Column(name="discount_code")
//    private String discountCode;



    @Id
    @ManyToOne
    @JoinColumn(name="account_email")
    private Account email;

    @Id
    @ManyToOne
    @JoinColumn(name="discount_code")
    private Discount code;

//    @Id
//    private String emailString;// = email.getEmail();
//
//    @Id
//    private String codeString;// = code.getCode();
//
//    public AccountDiscount(Account account, Discount discount){
//        this.email = account;
//        this.code = discount;
//        this.emailString = email.getEmail();
//        this.codeString = discount.getCode();
//    }


}
