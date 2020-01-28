/**
 * Class that represents a discount for
 * Vezorla's web application.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Discount {

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
    private BigDecimal minimumOrderValue;

    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;
    
    @ManyToOne
    @JoinColumn(name = "code")
    private Product product;
}
