package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.PurchaseOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository to interact with the PurchaseOrder table
 */
@Repository
public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, Long> {

    /**
     * Find the most recent purchase odrer
     * @return ID of the latest PO
     * @author jjrr1717
     */
    @Query(value = "SELECT MAX(po.poNum) FROM PurchaseOrder po")
    int findLastPO();

}
