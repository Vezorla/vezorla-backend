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
 *
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
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
     *
     * Annotated to be the Primary Key for the database
     * table 'product'
     */
    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    /**
     * Product name.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "name")
    private String name;

    /**
     * Product description.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "description")
    private String description;

    /**
     * Product sub description.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "subdescription")
    private String subdescription;

    /**
     * Product harvest time.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "harvest_time")
    private Date harvestTime;

    /**
     * Product main image.
     *
     * Can be set to first, second, or third image.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    //	@NotNull
    @Column(name = "image_main")
    private Long imageMain;

    /**
     * Product first image.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_one")
    private Long imageOne;

    /**
     * Product second image.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_two")
    private Long imageTwo;

    /**
     * Product third image.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "image_three")
    private Long imageThree;

    /**
     * Product active boolean.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    /**
     * Product threshold.
     *
     * Column cannot be null.
     *
     * Number of product in database, must be >= 0.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Min(0)
    @Column(name = "threshhold")
    private int threshhold;

    /**
     * Product price.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @NotNull
    @Column(name = "price")
    private String price;

    /**
     * Product old price.
     *
     * Previous set price.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @Column(name = "old_price")
    private String oldPrice;

    /**
     * Product product.
     *
     * Represents a foreign key in lotList.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Lot> lotList;

    /**
     * Product product.
     *
     * Represents a foreign key in discounts.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Discount> discounts;

    /**
     * Product product.
     *
     * Represents a foreign key in line items.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'product'
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<LineItem> lineItems;
}
