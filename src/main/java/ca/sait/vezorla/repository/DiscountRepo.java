package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Repository for the Discount table in the Vezorla database
 */
public interface DiscountRepo extends JpaRepository<Discount, Long> {
    /**
     * Find all valid discounts for the specified date
     * @param date date to find
     * @return List of discounts
     */
    List<Discount> findValidDiscounts(Date date);
}
