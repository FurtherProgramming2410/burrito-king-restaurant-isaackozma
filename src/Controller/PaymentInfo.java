package Controller;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


// this class is not complete yet, but it will be used for collecting payment info
public class PaymentInfo {
	
	
	//this method is to ensure the user enters the correct details of their car number
	//made it do its only digits and made sure there had to be 16 digits.
	public static boolean validateCardNumber(String cardNumber) {
		return cardNumber.matches("\\d{16}");
	}
	
	
	//this method is used to check the expiry date
	//have the DateTimeForrmatter to make sure its month then year
	//have the local date to make sure the card is within date, not expired already
	//try and catch used to chceck the date
	
	public static boolean validateExpiryDate(String expiryDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        try {
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            return expiry.isAfter(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
	

	//this method was made to check the cvv
	//made sure that its a digit and 3 digits long. 
	public static boolean validateCVV(String cvv) {
		return cvv.matches("\\d{3}");
	}

}
