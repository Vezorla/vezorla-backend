package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Cart;

public interface CartRepo extends JpaRepository<Cart, Long>{
	
}
