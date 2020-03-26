package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineItemRepo extends JpaRepository<LineItem, Long> {

    //	=========Line item dont have date==========
//	/**
//     * Find line item by the specified date
//     * @param startDate start date
//     * @param endDate end date
//     * @return Line Item
//     */
//    LineItem findLineItemByDate(Date startDate, Date endDate);
    @Query("FROM LineItem l WHERE l.invoice = :invoice")
    List<LineItem> findLineItemByInvoice(@Param("invoice") Invoice invoice);

    @Modifying
    @Query("DELETE from LineItem li where li.lineNum = :lineNum AND li.cart.orderNum = :orderNum")
    int deleteLineItemByLineNumAndCart_OrderNum(Long lineNum, Long orderNum);
}
