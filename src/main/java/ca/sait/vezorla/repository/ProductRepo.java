/**
 * ProductRepo a Repository for the Product with
 * the database.
 */
package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ProductRepo interface.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the Products table.
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    /**
     * Method to find the total quantity for a product.
     *
     * @param prod_num of the product
     * @return Integer, the quantity of the product
     * @author jjrr1717
     */
    @Query(value = "SELECT SUM(l.quantity) FROM Lot l WHERE l.product.prodId = :prod_num")
    Integer findTotalQuantity(@Param("prod_num") Long prod_num);

    /**
     * Method to find a product by its name
     *
     * @param productName of the product to find
     * @return Optional of the product found
     * @author matthewjflee
     */
    Optional<Product> findByName(String productName);

    /**
     * Method to find the product that has been
     * sold the most by Vezorla.
     *
     * @return long prod_id to find
     * @author jjrr1717
     */
    @Query(value = "SELECT prod_num FROM line_Item GROUP BY prod_num HAVING SUM(quantity) = (SELECT SUM(quantity) theSum FROM line_item GROUP BY prod_num ORDER BY theSum DESC LIMIT 1)", nativeQuery = true)
    long findTopProduct();
}
