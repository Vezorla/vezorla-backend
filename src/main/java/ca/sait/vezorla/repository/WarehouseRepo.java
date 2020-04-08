package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Warehouse;
import org.springframework.stereotype.Repository;

/**
 * WarehouseRepo interface.
 *
 * Not implemented.
 *
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 *
 * Repository to interact with the Warehouse table.
 */
@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long>{

}
