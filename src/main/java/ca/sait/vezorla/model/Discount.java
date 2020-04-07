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
import java.util.Date;
import java.util.List;

/**
 * Discount class.
 *
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
 * @author matthewjflee, jjrr1717
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "discount")
public class Discount implements Serializable {

    /**
     * Discount code.
     *
     * Annotated to be the Primary Key for the database
     * table 'discount'
     */
    @Id
    private String code;

    /**
     * Discount type.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "type")
    private String type;

    /**
     * Discount percent.
     *
     * Minimum value is zero.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "percent")
    @Min(0)
    private float percent;

    /**
     * Discount highlight boolean.
     *
     * Determines if the discount is to be used.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "is_highlighted", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isHighlighted;

    /**
     * Discount banner message.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name="banner_message")
    private String bannerMessage;

    /**
     * Discount description.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "description")
    private String description;

    /**
     * Discount start date.
     *
     * Dates enforce activation and expiry times.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * Discount end date.
     *
     * Dates enforce activation and expiry times.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * Discount minimum order value.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "minimum_order_value")
    private long minimumOrderValue;

    /**
     * Discount active boolean.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    /**
     * Discount product.
     *
     * Product to have discount on.
     *
     * Mapped by a many-to-one relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @JsonIgnore
    @ManyToOne
    private Product product;

    /**
     * Discount accountDiscounts.
     *
     * Maps discount code as foreign key in AccountDiscount.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'discount'
     */
    @OneToMany(mappedBy = "code")
    private List<AccountDiscount> accountDiscounts;

    /**
     * Constructor for a Discount.
     *
     * @param code Discount code.
     * @param description Description of the Discount.
     * @param percent Percentage of the Discount.
     */
    public Discount(String code, String description, float percent){
        this.code = code;
        this.description = description;
        this.percent = percent;
    }
}