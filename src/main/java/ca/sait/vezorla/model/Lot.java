package ca.sait.vezorla.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;

/**
 * Entity class for lot
 * @author		Minh Lam
 * @version		1.0
 * */

@Entity
@Data
@Table(name = "lot")
public class Lot {

	@Id
	private String lotNum;
	
	@Column(name = "quantity")
	private int quantity;
	
	@NotNull
	@Column(name = "cost")
	private Currency cost;
	
	@NotNull
	@Column(name = "best_before")
	private Date bestBefore;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private PurchaseOrder purchaseOrder;
	
	@OneToMany
	private List<LineItem> lineItemList = new ArrayList<LineItem>();
	
	@ManyToOne
	private Warehouse warehouse;
}
