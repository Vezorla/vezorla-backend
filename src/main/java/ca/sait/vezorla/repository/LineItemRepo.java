package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Invoice;
import ca.sait.vezorla.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LineItemRepo interface.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the Accounts table.
 */
@Repository
public interface LineItemRepo extends JpaRepository<LineItem, Long> {

    /**
     * Find the line items in an invoice.
     *
     * @param invoice invoice
     * @return list of line items in the invoice
     * @author matthewjflee
     */
    @Query("FROM LineItem l WHERE l.invoice = :invoice")
    List<LineItem> findLineItemByInvoice(@Param("invoice") Invoice invoice);

    /**
     * Delete a line item from a cart.
     *
     * @param lineNum  line item ID
     * @param orderNum cart ID
     * @return the line item ID that was deleted
     * @author matthewjflee
     */
    @Modifying
    @Query("DELETE from LineItem li where li.lineNum = :lineNum AND li.cart.orderNum = :orderNum")
    int deleteLineItemByLineNumAndCart_OrderNum(Long lineNum, Long orderNum);

    /**
     * Query to find line items by order number.
     *
     * @param orderNum of order to obtain line items
     * @return a list of line items
     * @author jjrr1717
     */
    @Query("FROM LineItem l WHERE l.cart.orderNum = :orderNum")
    List<LineItem> findLineItemByOrderNum(@Param("orderNum") Long orderNum);
}
