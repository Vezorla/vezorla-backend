/**
 * Class that represents a discount for
 * Vezorla's web application.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "discount")
public class Discount implements Serializable {

    @Id
    private String code;

    @Column(name = "type")
    private String type;

    @Column(name = "percent")
    @Min(0)
    private float percent;

    @Column(name = "is_highlighted", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isHighlighted;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "minimum_order_value")
    private long minimumOrderValue;

    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @JsonIgnore
    @ManyToOne
    private Product product;

//    @ManyToMany(mappedBy = "discountList")
//    private List<Account> accountList;

    @OneToMany(mappedBy = "code")
    private List<AccountDiscount> accountDiscounts;

    public Discount(String code, String description, float percent){
        this.code = code;
        this.description = description;
        this.percent = percent;
    }


    public Discount(String code, AccountDiscount... accountDiscounts) {
        this.code = code;
        this.accountDiscounts = Arrays.asList(accountDiscounts);
    }
}