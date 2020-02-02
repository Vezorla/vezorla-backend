package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Warehouse;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long>{

}
