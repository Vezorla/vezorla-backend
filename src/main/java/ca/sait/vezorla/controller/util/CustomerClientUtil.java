package ca.sait.vezorla.controller.util;

import ca.sait.vezorla.exception.InvalidInputException;

import java.util.regex.Pattern;

/**
 * CustomerClientUtil class.
 *
 * This class outlines the utilities used by the CustomerClient class.
 * All validation checks implemented in this class.
 */
public class CustomerClientUtil {

    /**
     * Method to format money values into
     * Strings to display in front end.
     *
     * @param amount the money value in cents
     * @return money value formatted as xx.xx.
     */
    public String formatAmount(long amount) {
        long cents = amount % 100;
        long dollars = (amount - cents) / 100;

        String amountAsString;
        if (cents <= 9) {
            amountAsString = 0 + "" + cents;
        } else {
            amountAsString = "" + cents;
        }

        return dollars + "." + cents;
    }

    /**
     * Validate Phone numbers inputted into
     * the web application
     *
     * @param phoneNumber Phone number to validate.
     * @throws InvalidInputException If the phone number does
     * not match the specified regex.
     */
    public void validatePhoneNumber(String phoneNumber) throws InvalidInputException {
        Pattern pattern = Pattern.compile("^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9A-Z \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$");
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new InvalidInputException();
        }
    }

    /**
     * Validate Postal code entered into the
     * web application.
     *
     * @param postalCode Postal code to be validated.
     * @throws InvalidInputException If the postal code does
     * not match the specified regex.
     */
    public void validatePostalCode(String postalCode) throws InvalidInputException {
        Pattern pattern = Pattern.compile("^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)");
        if (!pattern.matcher(postalCode).matches()) {
            throw new InvalidInputException();
        }
    }

    /**
     * Validate the email address entered into the
     * web application.
     *
     * @param email Email to be verified.
     * @return boolean True if valid, false otherwise.
     * @throws InvalidInputException If the email does
     * not match the specified regex.
     */
    public boolean validateEmail(String email) throws InvalidInputException {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidInputException();
        }

        return true;
    }
}