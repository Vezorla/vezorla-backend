package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Lot table in the Vezorla database
 */
public interface LotRepo extends JpaRepository<Lot, Long> {
    //No code required
}
