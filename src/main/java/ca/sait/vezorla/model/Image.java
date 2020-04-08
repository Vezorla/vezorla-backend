package ca.sait.vezorla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Image class.
 *
 * Represents a Product Image.
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
@Table(name = "image")
public class Image {

    /**
     * Image id.
     *
     * Annotated to be the Primary Key for the database
     * table 'image'
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Image name.
     *
     * Annotated to be a column within the database
     * table 'image'
     */
    @Column(name = "name")
    private String name;

    /**
     * Image type.
     *
     * Annotated to be a column within the database
     * table 'image'
     */
    @Column(name = "type")
    private String type;

    /**
     * Image picByte.
     *
     * Image bytes can have large lengths so we specify a value
     * which is more than the default length for picByte column.
     *
     * Annotated to be a column within the database
     * table 'image'
     */
    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "picByte", length = 10000000)
    private byte[] picByte;

    /**
     * Constructor for a Product Image within
     * the database.
     *
     * @param name Name of the Image.
     * @param type Type of Image.
     * @param picByte Number of bytes to store in the database.
     */
    public Image(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
