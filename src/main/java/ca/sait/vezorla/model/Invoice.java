package ca.sait.vezorla.model;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Invoice class.
 * <p>
 * Entity class for invoice.
 * <p>
 * This class models a table within the database. As such,
 * each variable represents a column and any methods within this
 * class manipulate these variables.
 * <p>
 * Constructors are overloaded to provide the functionality
 * needed within the application.
 *
 * @author Minh Lam
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

	/**
	 * Invoice number.
	 * <p>
	 * Annotated to be the Primary Key for the database
	 * table 'invoice'
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_num")
	private Long invoiceNum;

	/**
	 * Invoice account number.
	 * <p>
	 * Mapped by a many-to-one relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@ManyToOne
	@JoinColumn(name = "account_num")
	private Account account;

	/**
	 * Invoice line items.
	 * <p>
	 * Mapped by a one-to-many relationship within
	 * the database.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@OneToMany(mappedBy = "invoice")
	private List<LineItem> lineItemList;

	/**
	 * Invoice date.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@NotNull
	@Column(name = "date")
	private Date date;

	/**
	 * Invoice shipped boolean.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@NotNull
	@Column(name = "shipped")
	private boolean shipped;

	/**
	 * Invoice pickup boolean.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@NotNull
	@Column(name = "pickup")
	private boolean pickup;

	/**
	 * Invoice shipping cost boolean.
	 * <p>
	 * Column cannot be null.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@NotNull
	@Column(name = "shipping_cost")
	private long shippingCost;

	/**
	 * Invoice subtotal.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@Column(name = "subtotal")
	private long subtotal;

	/**
	 * Invoice discount.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@Column(name = "discount")
	private long discount;

	/**
	 * Invoice taxes.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@Column(name = "taxes")
	private long taxes;

	/**
	 * Invoice total.
	 * <p>
	 * Annotated to be a column within the database
	 * table 'invoice'
	 */
	@Column(name = "total")
	private long total;

	/**
	 * Constructor for Invoice.
	 *
	 * @param shippingCost Cost of shipping the order to the customer/client.
	 * @param subtotal     Total before taxes/discount.
	 * @param discount     Discount amount.
	 * @param taxes        Total taxes for the order.
	 * @param total        Compelte total for the order.
	 */
	//constructor for temp invoice
	public Invoice(long shippingCost, long subtotal, long discount, long taxes, long total) {
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
		this.discount = discount;
		this.taxes = taxes;
		this.total = total;
	}
}
