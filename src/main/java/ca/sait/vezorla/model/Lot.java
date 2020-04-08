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
 *
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 *
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
 * Entity class for lot.
 *
 * @author		Minh Lam
 * @version		1.0
 */
@Entity
@Data
@Table(name = "lot")
@AllArgsConstructor
@NoArgsConstructor
public class Lot implements Serializable {

	/**
	 * Lot number.
	 *
	 * Annotated to be the Primary Key for the database
	 * table 'lot'
	 */
	@Id
	private String lotNum;

	/**
	 * Lot quantity.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@Column(name = "quantity")
	private int quantity;

	/**
	 * Lot cost.
	 *
	 * Column cannot be null.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@NotNull
	@Column(name = "cost")
	private long cost;

	/**
	 * Lot best before date.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@Column(name = "best_before")
	private Date bestBefore;

	/**
	 * Lot product.
	 *
	 * Mapped by a many-to-one relationship within
	 * the database.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private Product product;

	/**
	 * Lot purchase order.
	 *
	 * Mapped by a many-to-one relationship within
	 * the database.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private PurchaseOrder purchaseOrder;

	/**
	 * Lot line item list.
	 *
	 * Mapped by a one-to-many relationship within
	 * the database.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "lot")
	private List<LineItem> lineItemList = new ArrayList<LineItem>();

	/**
	 * Lot warehouse.
	 *
	 * Mapped by a many-to-one relationship within
	 * the database.
	 *
	 * Annotated to be a column within the database
	 * table 'lot'
	 */
	@JsonIgnore
	@ManyToOne
	private Warehouse warehouse;
}
