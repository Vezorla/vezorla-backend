package ca.sait.vezorla.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ca.sait.vezorla.model.LineItem;

public interface LineItemRepo extends JpaRepository<LineItem, Long>{

//	=========Line item dont have date==========
//	/**
//     * Find line item by the specified date
//     * @param startDate start date
//     * @param endDate end date
//     * @return Line Item
//     */
//    LineItem findLineItemByDate(Date startDate, Date endDate);
}
