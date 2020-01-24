package ca.sait.vezorla.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Entity class for product quantity
 * @author		Minh Lam
 * @version		1.0 
 * */
@Data
public class ProductQuantity {
	
	private Product product;
	
	private int quantity;
	
	private BigDecimal cost;
	
	private BigDecimal price;
	
	private Inventory inventory;
	
	private InvoiceReport invoiceReport;
	
	public final float calcPercentMargin() {
		return 0.0f;
	}
}
