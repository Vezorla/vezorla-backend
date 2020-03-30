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
 * Entity class for product
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

    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "subdescription")
    private String subdescription;

    @Column(name = "harvest_time")
    private Date harvestTime;

    //	@NotNull
    @Column(name = "image_main")
    private String imageMain;

    @Column(name = "image_one")
    private String imageOne;

    @Column(name = "image_two")
    private String imageTwo;

    @Column(name = "image_three")
    private String imageThree;

    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @NotNull
    @Min(0)
    @Column(name = "threshhold")
    private int threshhold;

    @NotNull
    @Column(name = "price")
    private Long price;

    @Column(name = "old_price")
    private long oldPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Lot> lotList;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Discount> discounts;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<LineItem> lineItems;
}
