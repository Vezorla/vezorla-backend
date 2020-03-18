package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Lot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepo extends JpaRepository<Lot, String>{

    @Query("FROM Lot l WHERE l.quantity > 0 AND l.product = :product")
    public List<Lot> findAllLotsWithQuantity(@Param("product") Product product);

}
