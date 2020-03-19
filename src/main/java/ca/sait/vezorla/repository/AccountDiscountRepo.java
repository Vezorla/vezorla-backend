package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.AccountDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Repo class to interface with the AccountDiscount
 * bridging table.
 *
 */
@Repository
public class AccountDiscountRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertWithQuery(AccountDiscount accountDiscount) {
        entityManager.createNativeQuery("INSERT INTO account_discount (account_email, discount_code) VALUES (?,?)")
                .setParameter(1, accountDiscount.getEmail().getEmail())
                .setParameter(2, accountDiscount.getCode().getCode())
                .executeUpdate();
    }
}