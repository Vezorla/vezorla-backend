package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Product class.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 * <p>
 * Entity class for product.
 *
 * @author Minh Lam
 * @version 1.0
 */
@Entity
@Data
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    /**
     * Lot product id.
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'product'
     */
    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    /**
     * Product name.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "name")
    private String name;

    /**
     * Product description.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "description")
    private String description;

    /**
     * Product sub description.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "subdescription")
    private String subdescription;

    /**
     * Product harvest time.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "harvest_time")
    private Date harvestTime;

    /**
     * Product main image.
     * <p>
     * Can be set to first, second, or third image.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    //	@NotNull
    @Column(name = "image_main")
    private Long imageMain;

    /**
     * Product first image.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_one")
    private Long imageOne;

    /**
     * Product second image.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_two")
    private Long imageTwo;

    /**
     * Product third image.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_three")
    private Long imageThree;

    /**
     * Product active boolean.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    /**
     * Product threshold.
     * <p>
     * Column cannot be null.
     * <p>
     * Number of product in database, must be greater than or
     * equal to 0.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Min(0)
    @Column(name = "threshhold")
    private int threshhold;

    /**
     * Product price.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "price")
    private String price;

    /**
     * Product old price.
     * <p>
     * Previous set price.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "old_price")
    private String oldPrice;

    /**
     * Product product.
     * <p>
     * Represents a foreign key in lotList.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Lot> lotList;

    /**
     * Product product.
     * <p>
     * Represents a foreign key in discounts.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Discount> discounts;

    /**
     * Product product.
     * <p>
     * Represents a foreign key in line items.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<LineItem> lineItems;
}
