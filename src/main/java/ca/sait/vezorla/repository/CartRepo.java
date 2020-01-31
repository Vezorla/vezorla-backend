package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Cart;
import ca.sait.vezorla.model.LineItem;
import ca.sait.vezorla.model.Lot;
import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the Cart table in the Vezorla database
 */
public interface CartRepo extends JpaRepository<Cart, Long> {
    /**
     * Find all the line items for this cart
     * @return List of line items
     */
    List<LineItem> findAllLineItems();

    /**
     * Find all lots for the products in this cart
     * @return List of lots
     */
    List<Lot> findAllLots();

    /**
     * Find all products for the line items in this cart
     * @return List of products
     */
    List<Product> findAllProducts();
}
