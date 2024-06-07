package Controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


// The paymentinfo class is responsible for validating pyament information.
//It validates the card number, the cards expiry date and the CVV number.
//This class is used in - UserManager, OrderOnDashBoard, 
public class PaymentInfo {
	
	
	//This method validates the card number.
	//I made card number a string and if the number is 16 digits long it returns as true.
	//I made it so the it has to be 16 digits and no use of letters or any other special keys.
	public static boolean validateCardNumber(String cardNumber) {
		return cardNumber.matches("\\d{16}");
	}
	
	
	//This method is used to check the expiry date
	//It was important that the date is valid at the time of purchase.
	//The use of DateFormatter was for the format of month/year
	public static boolean validateExpiryDate(String expiryDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        
        //here i parse the expiry date.
        //I then check if the expiry date is after the current date
        //for example at the time of writing this its 06/24
        //if user enters 06/24 it will be invalid and return as false.
        //design requirments did not specify to make an exact date so anything within the same month
        //of the order will return as false, this is common with most cards. 
        try {
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            return expiry.isAfter(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
	

	//This method is to validate the CVV number given by the user.
	//It straight forward, I made it so the user need to first enter numbers only
	//and for it to be 3 digits long.
	public static boolean validateCVV(String cvv) {
		return cvv.matches("\\d{3}");
	}

}
