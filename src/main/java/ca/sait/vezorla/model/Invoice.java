package ca.sait.vezorla.model;


import javax.persistence.*;

import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;
/**
 * Entity class for invoice
 * @author		Minh Lam
 * @version		1.0
 * */
@Data
@Entity
@Table(name = "invoice")
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_num")
	private int invoiceNum;

	@Column(name = "account")
	@ManyToOne
	@JoinColumn(name = "account_num")
	private Account account;
	
	@NotNull
	@Column(name = "date")
	private Date date;
	
	@NotNull
	@Column(name = "shipped")
	private boolean shipped;
	
	@NotNull
	@Column(name = "shipping_cost")
	private BigDecimal shippingCost;
	
	@NotNull
	@Column(name = "state")
	private boolean state;
	
	@Column(name = "message")
	private String message;
}
