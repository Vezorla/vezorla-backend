package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Products table in the Vezorla database
 */
public interface ProductRepo extends JpaRepository<Product, Long> {
//No code required here
}
