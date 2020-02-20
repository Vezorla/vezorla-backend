package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Lot;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepo extends JpaRepository<Lot, String>{

}
