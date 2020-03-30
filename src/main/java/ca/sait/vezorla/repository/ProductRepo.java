package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>{

//    @Query(value = "SELECT SUM(l.quantity) FROM Lot l WHERE l.product.prodId = :prod_num")
    @Query(value = "SELECT SUM(l.quantity) FROM Lot l WHERE l.product.prodId = :prod_num")
    Integer findTotalQuantity(@Param("prod_num") Long prod_num);

    Optional<Product> findByName(String productName);
}
