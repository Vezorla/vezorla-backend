package ca.sait.vezorla.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Discount;

public interface DiscountRepo extends JpaRepository<Discount, String>{
	
	/**
     * Find all valid discounts for the specified date
     * @param date date to find
     * @return List of discounts
     */
    List<Discount> findValidDiscounts(Date date); 
}
