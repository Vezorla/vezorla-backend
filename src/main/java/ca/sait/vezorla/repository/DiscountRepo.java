package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * DiscountRepo interface.
 *
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 *
 * Repo class to interface with the discounts table.
 *
 * @author matthewjflee, jjrr1717
 */
@Repository
public interface DiscountRepo extends JpaRepository<Discount, String> {

    /**
     * Find all valid discounts for the specified date.
     *
     * Use custom query to find the valid discounts in the accounts_discount bridging table.
     *
     * @param date date to find
     * @param email Discount email
     * @return List of discounts
     * @author jjrr1717, matthewjflee
     */
    @Query("SELECT d.code, d.description, d.percent FROM Discount d " +
            "WHERE :date BETWEEN  d.startDate AND d.endDate " +
            "AND d.code NOT IN (SELECT ad.code FROM AccountDiscount ad WHERE :email = ad.email.email)")
    List<String> findValidDiscounts(@Param("date") Date date, @Param("email") String email);

    /**
     * Find the discount value by code.
     *
     * @param code discount code
     * @return find the discount value
     * @author jjrr1717
     */
    @Query("SELECT d.percent FROM Discount d " +
           "WHERE d.code = :code")
    String findDiscountPercent(@Param("code") String code);

    /**
     * Find the highlighted discount that will be
     * on home page banner.
     *
     * @return bannerMessage
     * @author jjrr1717
     */
    @Query("SELECT d.bannerMessage FROM Discount d WHERE d.isHighlighted = true")
    String findHighlightedDiscount();
}
