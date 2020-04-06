package ca.sait.vezorla.repository;

import ca.sait.vezorla.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to interact with the Invoice table
 */
@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    /**
     * Find all invoices associated the account
     * @param email user's email
     * @return list of invoices
     * @author matthewjflee
     */
    @Query("FROM Invoice i WHERE i.account.email = :email")
    List<Invoice> findAllByAccountEmail(@Param("email") String email);

    /**
     * Find the top products ordered
     * @return list of invoices with the top products
     * @author matthewjflee
     */
    List<Invoice> findTop50ByOrderByInvoiceNumDesc();
}
