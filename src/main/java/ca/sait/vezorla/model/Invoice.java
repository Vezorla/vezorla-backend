package ca.sait.vezorla.model;


import javax.persistence.*;

import com.sun.istack.NotNull;

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
public class Invoice {
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
