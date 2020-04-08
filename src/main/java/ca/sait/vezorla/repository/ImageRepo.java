package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ImageRepo interface.
 *
 * Repository interfaces are used to interact the the database
 * via Spring (JPARepository) and its annotations.
 *
 * Repository to interact with the Image table.
 */
public interface ImageRepo extends JpaRepository<Image, Long> {

    /**
     * Finds the Image via name.
     *
     * @param name Name of the image to be searched for.
     * @return Image as an Optional list.
     */
    Optional<Image> findByName(String name);
}
