package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.AccountDiscount;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * AccountDiscountRepo class.
 * <p>
 * Repository classes are used to interact the the database
 * via Spring and its annotations.
 * <p>
 * Repo class to interface with the AccountDiscount
 * bridging table.
 *
 * @author matthewjflee, jjrr1717
 */
@Repository
public class AccountDiscountRepo {

    /**
     * EntityManager interacts with the persistence context.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Transactional annotation
     * <p>
     * Spring creates a proxy that implements the same interface(s)
     * as the class you're annotating.
     *
     * @param accountDiscount AccountDiscount to add to the
     *                        database.
     */
    @Transactional
    public void insertWithQuery(AccountDiscount accountDiscount) {
        entityManager.createNativeQuery("INSERT INTO account_discount (account_email, discount_code) VALUES (?,?)")
                .setParameter(1, accountDiscount.getEmail().getEmail())
                .setParameter(2, accountDiscount.getCode().getCode())
                .executeUpdate();
    }
}
