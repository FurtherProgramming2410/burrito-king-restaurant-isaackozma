package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//user class is used to represent the user. 
//stores their details and orders.
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//here i store all the users data 
	//pretty simple just first & last name, username, password, orders.
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private List<Order> orders;
	
	private Order currentOrder;
	
	
	private boolean isVIP;
	private int credits;
	private String email;
	
	//constructor used to initialise a new user with the details added
	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.orders = new ArrayList<>();
		
		this.isVIP = false;
		this.credits = 0;
	}
	
	
	//method to get the current order. 
	public Order getCurrentOrder() {//might need to change this method before final 
		if(currentOrder == null) {
			currentOrder = new Order();
			
		}
		return currentOrder;
	}
	
	//finalizes the order by setting it to null
	public void finalizeOrder() {
		this.currentOrder =null;
	}
	
	// getter for the first name
	public String getFirstName() {
		return firstName;
	}
	
	//setter for the first name
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	//getter for the last name
	public String getLastName() {
		return lastName;
	}
	
	//setter for the last name
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	//getter for the username
	public String getUsername() {
		return username;
	}
	
	// this method is used to ensure the password matches what was given 
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	//setter for the password
	public void setPassword(String password) {
		this.password = password;
	}
	
	//getter method used to get list of orders from a user
	public List<Order> getOrders() {
		return orders;
	}
	
	public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
	
	//adds order to the list from a user
	public void addOrder(Order order) {
		if (!orders.contains(order)) {
	        this.orders.add(order);//////////////////////////////////changed
	    }
	}
	
	/////////////////////////////// vip stuff
	public int getCredits() {
		return credits;
	}
	
	public void setCredits(int credits) {
        this.credits = credits;
    }
	
	public boolean isVIP() {
		return isVIP;
	}
	
	public void setVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
        this.email = email;
    }
	
	public void upgradeToVIP(String email) {
		this.isVIP = true;
		this.email = email;

	}
	public void addCredits(int credits) {
		this.credits += credits;
	}
	
	
	
	public void deductCredits(int credits) {
	    if (this.credits >= credits) {
	        this.credits -= credits;
	    }
	}
	
	public boolean useCredits(int credits) {
		if (this.credits >= credits) {
			this.credits -= credits;
			return true;
		}
		return false;
	}
	////////////////////////////////////////vip stuff
	
	
	
	//method used to get an order by its ID
	public Order getOrderById(int orderId) {
		for (Order order : orders) {
			if (order.getOrderID() == orderId) {/// this is confusing, it works but maybe change, leave to end as its not needed, to many orders lol 
				return order;
			}
		}
		return null;
	}

}


