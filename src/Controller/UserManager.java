package Controller;
import java.util.HashMap;
import java.util.Map;

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
	
	
	 

}


