/**
 * Class to represent a warehouse for Vezorla.
 * A Warehouse stores products.
 *
 * @author Jocelyn Wegen
 */
package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import javax.persistence.OneToMany;


@Entity
@Data
@Table(name = "warehouse")
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_num")
    private Long warehouseNum;

    @Column(name = "address")
    @NotNull
    private String address;

    @Column(name = "province")
    @NotNull
    private String province;

    @Column(name = "city")
    @NotNull
    private String city;

    @NotNull
    @Pattern(regexp = "^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)")
    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    @Pattern(regexp = "^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9A-Z \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "active")
    @NotNull
    private boolean active;

    @OneToMany(mappedBy = "warehouse")
    private List<Lot> lots;

}
