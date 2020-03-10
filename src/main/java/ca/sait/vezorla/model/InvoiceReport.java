package ca.sait.vezorla.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceReport {
	List<ProductQuantity> productQuanList;
	
	public final long calcTotalCost() {
		return 0;
	}
	
	public final long calcTotalPrice() {
		return 0;
	}
	
	public final float calcTotalPercentMargin() {
		return 0.0f;
	}
}
