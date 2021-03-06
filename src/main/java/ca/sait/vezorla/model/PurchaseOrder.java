package ca.sait.vezorla.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * PurchaseOrder class.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 * <p>
 * Entity class for purchase order.
 *
 * @author Minh Lam
 * @version 1.0
 */
@Data
@Entity
@Table(name = "purchase_order")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {

	/**
	 * PurchaseOrder number.
	 * <p>
	 * Annotated to be the Primary Key for the database
	 * table 'purchase_order'
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long poNum;

	/**
	 * PurchaseOrder date ordered.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'purchase_order'
	 */
	@NotNull
	private Date dateOrdered;

	/**
	 * PurchaseOrder date received.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'purchase_order'
	 */
	private Date dateReceived;

	/**
	 * PurchaseOrder received boolean.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'purchase_order'
	 */
	private boolean received;

	/**
	 * PurchaseOrder lot list.
	 * <p>
	 * Mapped by a one-to-many relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'purchase_order'
	 */
	@OneToMany(mappedBy = "purchaseOrder")
	private List<Lot> lotList;
}
