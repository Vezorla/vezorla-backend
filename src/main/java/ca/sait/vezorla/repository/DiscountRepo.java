package ca.sait.vezorla.repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Discount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, String>{
	
	/**
     * Find all valid discounts for the specified date
     * @param date date to find
     * @return List of discounts
//     */
//    @Query("SELECT code d FROM discounts d, account_discounts ad " +
//            "WHERE code d = code ad AND " +
//            "(start_date d < :date " +
//            "AND)")

    @Query("SELECT d.code FROM Discount d " +
            "WHERE :date BETWEEN  d.startDate AND d.endDate ")// +
            //"AND :email NOT IN (SELECT a.email FROM Discount dd JOIN Account a)")
    List<String> findValidDiscounts(@Param("date") Date date);//, @Param("email") String email);
}
