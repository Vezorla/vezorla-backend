package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository to interact with the Image table
 */
public interface ImageRepo extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
