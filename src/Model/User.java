package Model;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	//here i store all the users data 
	//pretty simple just first & last name, username, password
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private List<Order> orders;
	
	//constructor used to initialise a new user with the details added
	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.orders = new ArrayList<>();
	}
	
	//method to get the first name
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	//method to get the last name
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	//method to get the username
	public String getUsername() {
		return username;
	}
	
	// this method is used to ensure the password matches what was given 
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	//getter method used to get list of orders from a user
	public List<Order> getOrders() {
		return orders;
	}
	
	//adds order to the list from a user
	public void addOrder(Order order) {
		this.orders.add(order);
	}

}
