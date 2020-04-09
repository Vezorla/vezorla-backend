package ca.sait.vezorla.service;


import ca.sait.vezorla.exception.InvalidInputException;
import ca.sait.vezorla.exception.OutOfStockException;
import ca.sait.vezorla.exception.UnauthorizedException;
import ca.sait.vezorla.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * AdminServices interface.
 *
 * This interface outlines the services as it relates to
 * Accounts.
 *
 * This interface acts as the intermediary between the controllers
 * and the repositories.
 *
 * Admin services.
 */
public interface AdminServices {

    /**
     * Accepts a pending business order.
     *
     * @param invoice Invoice to be accepted.
     */
    void acceptBusinessOrder(Invoice invoice);

    /**
     * Creates a business account.
     *
     * @param account Account to be created.
     * @return Boolean true if created, false otherwise.
     */
    boolean createAccount(Account account);

    /**
     * Creates a discount.
     *
     * @param discount Discount to be created.
     * @return Boolean true if created, false otherwise.
     */
    boolean createDiscount(Discount discount);

    /**
     * Creates a report given specified parameters.
     *
     * @param type Report type.
     * @param start Report start date.
     * @param end Report end date.
     */
    void createReport(String type, Date start, Date end);

    /**
     * Decline a pending business order.
     *
     * @param id Invoice ID to be declined.
     */
    void declineBusinessOrder(Long id);

    /**
     * Exports the data from the database.
     *
     * @return boolean true if exported, false otherwise.
     */
    boolean exportData() throws MessagingException;

    /**
     * Returns all products in the database.
     *
     * @param mapper Mapper to be used to hold products.
     * @return ObjectNode Used to return all product information.
     */
    ObjectNode getAllProducts(ObjectMapper mapper);

    /**
     * Returns all products for PO in the database.
     *
     * @param mapper Mapper to be used to hold products PO.
     * @return ObjectNode Used to return all product PO information.
     */
    ObjectNode getAllProductsForPO(ObjectMapper mapper);

    /**
     * Creates a product in the database.
     *
     * @param product Product to be created.
     * @param session User's session.
     * @return Boolean true if created, false otherwise.
     * @throws InvalidInputException thrown if input is
     * considered invalid.
     */
    boolean createProduct(Product product, HttpSession session) throws InvalidInputException;

    /**
     * Updates a product in the database.
     *
     * @param product Product to be updated.
     * @param changed Product that has changed.
     * @return Boolean true if updated, false otherwise.
     */
    boolean updateProduct(Product product, Product changed);

    /**
     * Saves a product in the database.
     *
     * @param product Product to be saved.
     * @return Boolean true if saved, false otherwise.
     */
    Product saveProduct(Product product);

    /**
     * Gets all pending business orders from the database.
     *
     * @return List containing all pending Invoices.
     */
    List<Invoice> getPendingBusinessOrder();

    /**
     * Gets a pending business orders from the database
     * via id.
     *
     * @param id Business order id
     * @return Pending business order by id.
     */
    Invoice getPendingBusinessOrderById(Long id);

    /**
     * Saves the purchase order in the database.
     *
     * @param purchaseOrder Purchase order to be saved.
     * @return Saved purchase order.
     */
    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Receives a purchase order.
     *
     * @param body Purchase order information.
     * @return Boolean true if received, false otherwise.
     */
    boolean receivePurchaseOrder(String body);

    /**
     * Saves the lots to the database.
     *
     * @param lots Lots to be saved.
     * @param po Purchase order of the lot.
     * @return Boolean true if saved, false otherwise.
     */
    boolean saveLots(List<Lot> lots, PurchaseOrder po);

    /**
     * Gets an order to be viewed.
     *
     * @param id Order id to get.
     * @param mapper Object to contain order.
     * @return ObjectNode to contain order.
     */
    ObjectNode viewOrder(Long id, ObjectMapper mapper);

    /**
     * Gets an order to be viewed with admin
     * privileges.
     *
     * @param mapper Object to contain order.
     * @param session User session.
     * @return ObjectNode to contain order.
     */
    ObjectNode viewOrderHistoryAdmin(ObjectMapper mapper, HttpSession session);

    /**
     * Restore the database to a backup.
     *
     * @param file Backup file.
     * @return Boolean true if restored, false otherwise.
     * @throws OutOfStockException If a product in the restore is
     * out of stock.
     */
    boolean restoreBackup(MultipartFile file) throws OutOfStockException;

    /**
     * Creates a warehouse in the database.
     *
     * @param warehouse Warehouse to be created.
     * @return Boolean true if created, false otherwise.
     * @throws InvalidInputException If warehouse input is invalid.
     */
    boolean createWarehouse(Warehouse warehouse) throws InvalidInputException;

    /**
     * Compress the bytes of an image to persist into the database
     *
     * @param data image data in bytes
     * @return the compressed bytes of the image
     */
    byte[] compressBytes(byte[] data);

    /**
     * Decompress the bytes of an image to display
     *
     * @param data image in bytes
     * @return bytes of the image decompressed
     */
    byte[] decompressBytes(byte[] data);

    /**
     * Persist an image in the database
     *
     * @param image  image to persist
     * @param prodId product image to update
     * @param session User's session
     */
    void saveImage(Image image, Optional<Long> prodId, HttpSession session);

    /**
     * Gets an image from the database.
     *
     * @param id ID of the image
     * @return image
     */
    Optional<Image> getImage(Long id);

    /**
     * Gets all the warehouses from a PO.
     *
     * @param mapper Creates the custom JSON.
     * @return ObjectNode contains the custom JSON.
     */
    ObjectNode getAllWarehousesForPO(ObjectMapper mapper);

    /**
     * Gets the next PO number.
     *
     * @param mapper Creates the custom JSON.
     * @return ObjectNode contains the custom JSON.
     */
    ObjectNode getNextPONum(ObjectMapper mapper);

    /**
     * Method to return admin email to front end
     * for update admin account.
     *
     * @param session to get Account
     * @param mapper  for json
     * @return Object node for json
     * @throws UnauthorizedException email is invalid
     */
    ObjectNode getAdminEmail(HttpSession session, ObjectMapper mapper) throws UnauthorizedException;

    /**
     * Method to view all the clients.
     *
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     */
    ObjectNode viewAllClients(ObjectMapper mapper);

    /**
     * Method to view a client.
     *
     * @param email Client email
     * @param mapper to make the custom json
     * @return ObjectNode containing nodes for custom json
     */
    ObjectNode viewClient(String email, ObjectMapper mapper);
}
