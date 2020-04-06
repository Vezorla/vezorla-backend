package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.loader.Loader.SELECT;

/**
 * Repository to interact with the Carts table
 */
@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    /**
     * Find a from the email
     * @param email account's email
     * @return Cart
     * @author matthewjflee
     */
    @Query("FROM Cart c WHERE c.account.email = :email " +
            "AND c.orderNum = (SELECT MAX(cc.orderNum) " +
            "FROM Cart cc WHERE cc.account.email = :email)")
    Cart findCartByAccountEmail(@Param("email") String email);

    /**
     * Find all the carts associated with an account
     * @param email user's email
     * @return list of all carts
     */
    @Query("FROM Cart c WHERE c.account.email = :email")
    List<Cart> findCartsByAccountEmail(@Param("email") String email);
}
