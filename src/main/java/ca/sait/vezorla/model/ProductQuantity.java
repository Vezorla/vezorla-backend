package ca.sait.vezorla.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * Entity class for product quantity
 * @author		Minh Lam
 * @version		1.0 
 * */
@Data
public class ProductQuantity {
	
	private Product product;
	
	@Min(0)
	private long quantity;
	
	@Min(0)
	private long cost;
	
	@Min(0)
	private long price;
	
	private Inventory inventory;
	
	private InvoiceReport invoiceReport;
	
	public final float calcPercentMargin() {
		return 0.0f;
	}
}
