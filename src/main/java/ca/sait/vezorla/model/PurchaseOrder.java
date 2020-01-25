package ca.sait.vezorla.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;

/**
 * Entity class for purchase order
 * @author		Minh Lam
 * @version		1.0
 * */

@Data
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
	@Id
	private int poNum;
	
	@NotNull
	private Date dateOrdered;
	
	private Date dateReceived;
	
	private boolean received;

	@OneToMany(mappedBy = "purchaseOrder")
	private List<Lot> lotList;
}
