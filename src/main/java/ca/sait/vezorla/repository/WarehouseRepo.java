package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Warehouse table in the Vezorla database
 */
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    //No code required
}
