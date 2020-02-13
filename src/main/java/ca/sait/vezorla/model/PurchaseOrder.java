package ca.sait.vezorla.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for purchase order
 * @author		Minh Lam
 * @version		1.0
 * */

@Data
@Entity
@Table(name = "purchase_order")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
	@Id
	private Long poNum;
	
	@NotNull
	private Date dateOrdered;
	
	private Date dateReceived;
	
	private boolean received;

	@OneToMany(mappedBy = "purchaseOrder")
	private List<Lot> lotList;
}
