package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * WarehouseRepo interface.
 * <p>
 * Not implemented.
 * <p>
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 * <p>
 * Repository to interact with the Warehouse table.
 */
@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

}
