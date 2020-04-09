package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Warehouse class.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 * <p>
 * Class to represent a warehouse for Vezorla.
 * A Warehouse stores products.
 *
 * @author jjrr1717, matthewjflee
 */
@Entity
@Data
@Table(name = "warehouse")
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    /**
     * Warehouse number.
     * <p>
     * Annotated to be the Primary Key for the database
     * table 'warehouse'
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_num")
    private Long warehouseNum;

    /**
     * Warehouse address.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "address")
    @NotNull
    private String address;

    /**
     * Warehouse province.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "province")
    @NotNull
    private String province;

    /**
     * Warehouse city.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "city")
    @NotNull
    private String city;

    /**
     * Warehouse postal code.
     * <p>
     * Column cannot be null.
     * <p>
     * Postal code must follow specified regex.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @NotNull
    @Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * Warehouse phone number.
     * <p>
     * Column cannot be null.
     * <p>
     * Phone number must follow specified regex.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @NotNull
    @Pattern(regexp = "^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9A-Z \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Warehouse active boolean.
     * <p>
     * Column cannot be null.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "active")
    @NotNull
    private boolean active;

    /**
     * Warehouse lots.
     * <p>
     * Lots contained within the Warehouse.
     * <p>
     * Mapped by a one-to-many relationship within
     * the database.
     * <p>
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @OneToMany(mappedBy = "warehouse")
    private List<Lot> lots;

}
