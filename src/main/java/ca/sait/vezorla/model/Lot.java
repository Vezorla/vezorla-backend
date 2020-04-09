package ca.sait.vezorla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Lot class.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 * <p>
 * Entity class for lot.
 *
 * @author Minh Lam
 * @version 1.0
 */
@Entity
@Data
@Table(name = "lot")
@AllArgsConstructor
@NoArgsConstructor
public class Lot implements Serializable {

	/**
	 * Lot number.
	 * <p>
	 * Annotated to be the Primary Key for the database
	 * table 'lot'
	 */
	@Id
	private String lotNum;

	/**
	 * Lot quantity.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@Column(name = "quantity")
	private int quantity;

	/**
	 * Lot cost.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@NotNull
	@Column(name = "cost")
	private long cost;

	/**
	 * Lot best before date.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@Column(name = "best_before")
	private Date bestBefore;

	/**
	 * Lot product.
	 * <p>
	 * Mapped by a many-to-one relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private Product product;

	/**
	 * Lot purchase order.
	 * <p>
	 * Mapped by a many-to-one relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private PurchaseOrder purchaseOrder;

	/**
	 * Lot line item list.
	 * <p>
	 * Mapped by a one-to-many relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "lot")
	private List<LineItem> lineItemList = new ArrayList<LineItem>();

	/**
	 * Lot warehouse.
	 * <p>
	 * Mapped by a many-to-one relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private Warehouse warehouse;
}
