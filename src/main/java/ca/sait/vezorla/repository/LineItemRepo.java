package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Repository for the LineItem table in the Vezorla database
 */
public interface LineItemRepo extends JpaRepository<LineItem, Long> {
    /**
     * Find line item by the specified date
     * @param startDate start date
     * @param endDate end date
     * @return Line Item
     */
    LineItem findLineItemByDate(Date startDate, Date endDate);
}
