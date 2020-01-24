package ca.sait.vezorla.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * Entity class for inventory
 * @author		Minh Lam
 * @version		1.0
 * */
@Data
public class Inventory {
	private List<ProductQuantity> productQuantityList = new ArrayList<ProductQuantity>();
}
