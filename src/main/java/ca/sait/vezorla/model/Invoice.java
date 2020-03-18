package ca.sait.vezorla.model;


import javax.persistence.*;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for invoice
 * @author		Minh Lam
 * @version		1.0
 * */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_num")
	private Long invoiceNum;

	@ManyToOne
	@JoinColumn(name = "account_num")
	private Account account;
	
	@OneToMany(mappedBy = "invoice")
	private List<LineItem> lineItemList = new ArrayList<LineItem>();
	
	@NotNull
	@Column(name = "date")
	private Date date;


	/**
	 * I think we should get rid of this field
	 */
	@NotNull
	@Column(name = "shipped")
	private boolean shipped;

	@NotNull
	@Column(name = "pickup")
	private String pickup;

	@NotNull
	@Column(name = "shipping_cost")
	private long shippingCost;

	@Column(name = "subtotal")
	private long subtotal;

	@Column(name = "discount")
	private long discount;

	@Column(name = "taxes")
	private long taxes;

	@Column(name = "total")
	private long total;

	/**
	 * I think we should get delete this
	 * field.
	 */
	@NotNull
	@Column(name = "state")
	private boolean state;

	/**
	 * I think we should delete this
	 * field.
	 */
	@Column(name = "message")
	private String message;

	//constructor for temp invoice
	public Invoice(long shippingCost, long subtotal, long discount, long taxes, long total){
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
		this.discount = discount;
		this.taxes = taxes;
		this.total = total;
	}
}
