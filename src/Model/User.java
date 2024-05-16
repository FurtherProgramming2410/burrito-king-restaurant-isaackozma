package Model;

public class User {
	
	//here i store all the users data 
	//pretty simple just first & last name, username, password
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	
	
	//constructor used to initialise a new user with the details added
	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	//method to get the first name
	public String getFirstName() {
		return firstName;
	}
	
	//method to get the last name
	public String getLastName() {
		return lastName;
	}
	
	//method to get the username
	public String getUsername() {
		return username;
	}
	
	// this method is used to ensure the password matches what was given 
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

}
