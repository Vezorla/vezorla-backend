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
 *
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 *  *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
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
     *
     * Annotated to be the Primary Key for the database
     * table 'warehouse'
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_num")
    private Long warehouseNum;

    /**
     * Warehouse address.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "address")
    @NotNull
    private String address;

    /**
     * Warehouse province.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "province")
    @NotNull
    private String province;

    /**
     * Warehouse city.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "city")
    @NotNull
    private String city;

    /**
     * Warehouse postal code.
     *
     * Column cannot be null.
     *
     * Postal code must follow specified regex.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @NotNull
    @Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * Warehouse phone number.
     *
     * Column cannot be null.
     *
     * Phone number must follow specified regex.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @NotNull
    @Pattern(regexp = "^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9A-Z \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Warehouse active boolean.
     *
     * Column cannot be null.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @Column(name = "active")
    @NotNull
    private boolean active;

    /**
     * Warehouse lots.
     *
     * Lots contained within the Warehouse.
     *
     * Mapped by a one-to-many relationship within
     * the database.
     *
     * Annotated to be a column within the database
     * table 'warehouse'
     */
    @OneToMany(mappedBy = "warehouse")
    private List<Lot> lots;

}
