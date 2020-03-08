package ca.sait.vezorla.controller.util;

import ca.sait.vezorla.exception.InvalidInputException;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor
public class CustomerClientUtil {

    /**
     * Method to format money values into
     * Strings to display in front end.
     * @param amount the money value in cents
     * @return money value formatted as xx.xx.
     */
    public String formatAmount(long amount){
        long cents = amount % 100;
        long dollars = (amount - cents) / 100;

        String amountAsString;
        if(cents <= 9){
            amountAsString = 0 + "" +  cents;
        }
        else{
            amountAsString = "" + cents;
        }

        return dollars + "." + cents;
    }

    /**
     * Validate Phone numbers inputted into
     * the web application
     * @param phoneNumber
     * @throws InvalidInputException
     */
    public void validatePhoneNumber(String phoneNumber) throws InvalidInputException {
       Pattern pattern = Pattern.compile("^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9A-Z \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$");
       if(!pattern.matcher(phoneNumber).matches()){
           throw new InvalidInputException();
       }
    }

    public void validatePostalCode(String postalCode) throws InvalidInputException{
        Pattern pattern = Pattern.compile("^([A-Za-z]\\d[A-Za-z][-]?\\d[A-Za-z]\\d)");
        if(!pattern.matcher(postalCode).matches()){
            throw new InvalidInputException();
        }
    }
}
