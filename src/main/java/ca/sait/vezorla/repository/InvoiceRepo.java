package ca.sait.vezorla.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ca.sait.vezorla.model.Invoice;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long>{

//	/**
//     * Change the state of the invoice
//     * @param id Invoice ID
//     * @param state new state of the invoice
//     * @return <code>true</code> if successful, <code>false</code> if not
//     */
//	@Modifying
//	@Query("UPDATE Invoice i SET i.state = ?2 WHERE i.invoiceNum = ?1")
//	int changeState(Long id, boolean state);
//
//	/**
//    * Find all invoices by the specified state
//    * @param state state to find
//    * @return list of all invoices specified by the state
//    */
////	@Query("SELECT i FROM Invoice i WHERE i.state = ?1") in case cant use the naming
//	List<Invoice> findByState(boolean state);
//
//	/**
//     * Find an invoice based on the ID and state
//     * @param id ID of the invoice
//     * @param state State of the invoice
//     * @return invoice
//     */
////    List<Invoice> findByIdAndState(Long id, boolean state);
//
//    /**
//     * Find all sales between the specified start and end dates
//     * @param startDate start date of the client sales
//     * @param endDate end date of the client sales
//     * @return List of sales between the start and end dates
//     */
//    @Query("SELECT i FROM Invoice i WHERE i.date > ?1 AND i.date < ?2")
//    List<Invoice> findAllSales(Date startDate, Date endDate);
//
//    /**
//     * Find the email of a specified invoice
//     * @param id invoice ID
//     * @return email in invoice
//     */
//    String findEmailByID(Long id);
    
    //=======Not sure yet==========
//    /**
//     * Find all invoices by the client's account ID
//     * @param id account ID
//     * @return list of invoices with the client's account ID
//     */
//    List<Invoice> findAllByAccountId(Long id);
//
//    /**
//     * Find invoices that match the invoice ID and the user's account ID
//     * @param id invoice ID
//     * @param accountID account ID
//     * @return invoice
//     */
//    Invoice findByIdAndAccountId(Long id, Long accountID);

}
