package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Backup table in the Vezorla database
 */
public interface RestoreRepo extends JpaRepository<Backup, Long> {
    //No code required
}
