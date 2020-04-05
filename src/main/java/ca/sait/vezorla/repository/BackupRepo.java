package ca.sait.vezorla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepo extends JpaRepository<Backup, Long>{

}
