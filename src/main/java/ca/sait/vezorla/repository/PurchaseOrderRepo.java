package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * PurchaseOrderRepo interface.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the PurchaseOrder table.
 */
@Repository
public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, Long> {

    /**
     * Find the most recent purchase order.
     *
     * @return ID of the latest PO
     * @author jjrr1717
     */
    @Query(value = "SELECT MAX(po.poNum) FROM PurchaseOrder po")
    int findLastPO();

}
