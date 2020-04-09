package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Lot;
import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LotRepo interface.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the lots table.
 */
@Repository
public interface LotRepo extends JpaRepository<Lot, String> {

    /**
     * Find all lots with quantity more than 0.
     *
     * @param product product to find
     * @return list of lots
     * @author jjrr1717
     */
    @Query("FROM Lot l WHERE l.quantity > 0 AND l.product = :product")
    List<Lot> findAllLotsWithQuantity(@Param("product") Product product);
}
