package Controller;
import java.util.HashMap;
import java.util.Map;

import Model.Order;
import Model.User;

// so this class honestly is just to help with the database.
// the data base is not ready and i havent created it yet but i needed to test my program
//i predict this class will not be used in coming weeks. 
public class UserManager {
	
	//this map stores users info
	private static Map<String, User> users = new HashMap<>();
	
	//this method basically just ensures user name and password are unique
	//otherwise the program will not allow the creation of the account
	public static boolean registerUser(String username, String password, String firstName, String lastName) {
		if(!users.containsKey(username)) {
			users.put(username, new User (username, password, firstName, lastName));
			return true; 
		}
		return false;
		
	}
	
	//this method is to ensure the login is correct with the username and password
	public static User loginUser(String username, String password) {
		User user = users.get(username);
		if (user != null && user.checkPassword(password)) {
			return user;
		}
		
		return null;
	}
	
	//this method allows for the user to update their profile, as in a profile that already exists
	//allows for the name and password to be updated. Not the username as it said not to in the assessment
	public static boolean updateUserProfile(String username, String firstName, String lastName, String password) {
		User user = users.get(username);
		if (user != null) {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(password);
			return true;
		}
		return false;
	}
	
	
	// this method is used to validate the payment details
	//tells users if it worked or not
	//when payments is successful then the order will be added to the list
	public static void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv) {
		if (PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate)
				&& PaymentInfo.validateCVV(cvv)) {
			User user = users.get(username);
			if(user != null) {
				user.addOrder(order);
				System.out.println("Payment success:" +username);
			}
			 
		}else {
			System.out.println("Invalid payment: " +username);
		}
		
	}
	
	//will be for canceling an order, have not gotten to it yet
	public static void cancelOrder(String username, int orderID) {
		
	}
	
	//
	
	 

}


