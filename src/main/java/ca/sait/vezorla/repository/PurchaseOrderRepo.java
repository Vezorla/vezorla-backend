package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the PurchaseOrder table in the Vezorla database
 */
public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, Long> {
    //No code required
}
