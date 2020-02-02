package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sait.vezorla.model.Backup;

public interface BackupRepo extends JpaRepository<Backup, Long>{

}
