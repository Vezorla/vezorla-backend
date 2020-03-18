package ca.sait.vezorla.model;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for lot
 * @author		Minh Lam
 * @version		1.0
 * */

@Entity
@Data
@Table(name = "lot")
@AllArgsConstructor
@NoArgsConstructor
public class Lot implements Serializable {

	@Id
	private Long lotNum;
	
	@Column(name = "quantity")
	private int quantity;
	
	@NotNull
	@Column(name = "cost")
	private long cost;
	

	@Column(name = "best_before")
	private Date bestBefore;

	@JsonIgnore
	@ManyToOne
	private Product product;

	@JsonIgnore
	@ManyToOne
	private PurchaseOrder purchaseOrder;

	@JsonIgnore
	@OneToMany(mappedBy = "lot")
	private List<LineItem> lineItemList = new ArrayList<LineItem>();

	@JsonIgnore
	@ManyToOne
	private Warehouse warehouse;
}
