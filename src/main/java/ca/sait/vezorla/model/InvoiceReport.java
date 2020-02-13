package ca.sait.vezorla.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceReport {
	List<ProductQuantity> productQuanList;
	
	public final BigDecimal calcTotalCost() {
		return null;
	}
	
	public final BigDecimal calcTotalPrice() {
		return null;
	}
	
	public final float calcTotalPercentMargin() {
		return 0.0f;
	}
}
