package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Warehouse;
import org.springframework.stereotype.Repository;

/**
 * Repository to interact with the Warehouse table
 */
@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long>{

}
