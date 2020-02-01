package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{

}
