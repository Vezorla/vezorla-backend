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
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
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
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'discount'
     */
    @Id
    private String code;

    /**
     * Discount type.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "type")
    private String type;

    /**
     * Discount percent.
     * <p>
     * Minimum value is zero.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "percent")
    @Min(0)
    private float percent;

    /**
     * Discount highlight boolean.
     * <p>
     * Determines if the discount is to be used.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "is_highlighted", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isHighlighted;

    /**
     * Discount banner message.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "banner_message")
    private String bannerMessage;

    /**
     * Discount description.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "description")
    private String description;

    /**
     * Discount start date.
     * <p>
     * Dates enforce activation and expiry times.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * Discount end date.
     * <p>
     * Dates enforce activation and expiry times.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * Discount minimum order value.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "minimum_order_value")
    private long minimumOrderValue;

    /**
     * Discount active boolean.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    /**
     * Discount product.
     * <p>
     * Product to have discount on.
     * <p>
     * Mapped by a many-to-one relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @JsonIgnore
    @ManyToOne
    private Product product;

    /**
     * Discount accountDiscounts.
     * <p>
     * Maps discount code as foreign key in AccountDiscount.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'discount'
     */
    @OneToMany(mappedBy = "code")
    private List<AccountDiscount> accountDiscounts;

    /**
     * Constructor for a Discount.
     *
     * @param code        Discount code.
     * @param description Description of the Discount.
     * @param percent     Percentage of the Discount.
     */
    public Discount(String code, String description, float percent) {
        this.code = code;
        this.description = description;
        this.percent = percent;
    }
}