package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Backup;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepo extends JpaRepository<Backup, Long>{

}
