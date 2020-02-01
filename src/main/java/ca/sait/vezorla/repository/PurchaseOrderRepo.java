package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.PurchaseOrder;

public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, Long>{

}
