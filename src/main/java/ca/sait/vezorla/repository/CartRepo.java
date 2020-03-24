package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.loader.Loader.SELECT;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    @Query("FROM Cart c WHERE c.account.email = :email")
    List<Cart> findCartsByAccount_Email(@Param("email") String email);

//    @Query("FROM Cart c "  +
//            "WHERE c.account.email = :email " +
//            "AND c.orderNum = (SELECT MAX(cc.orderNum) FROM Cart cc " +
//            "GROUP BY cc.account.email HAVING cc. = :email")//")
    Cart findCartByAccount_Email(@Param("email") String email);


    @Query("select MAX(c.orderNum) from Cart c where c.account.email = :email")
    long findCartIdByAccount_Email(@Param("email") String email);
}

//FROM Cart c WHERE c.orderNum IN(SELECT MAX(c.orderNum) FROM Cart c group by accountNum