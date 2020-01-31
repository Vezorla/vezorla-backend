package ca.sait.vezorla.repository;

/**
 * Repository for the Mailing List table in the Vezorla database
 */
public interface MailingListRepo {
    /**
     * Find email that is currently subscribed to the mailing list
     * @param email Email to find
     * @return <code>true</code> if email exists, <code>false</code> if it does not
     */
    boolean findByEmail(String email);
}
