package Controller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Interface.KingItem;
import Model.Order;
import Model.User;


//this class is responsible for managing user data and operations.
//
public class UserManager {
	
	//this map stores users info
	private static Map<String, User> users = new HashMap<>();
	private static User currentUser;//addddddddddddddddddddddddddddddddddd
	
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
			currentUser = user;//dddddddddddddddddddddddddddddddddd
			return user;
		}
		
		return null;
	}
	
	// method to log user out. 
	//still needs to be finished correctly
	public static void logoutUser() {
        currentUser = null;
        System.out.println("User logged out.");
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
	 public static void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv, LocalDateTime orderPlacedTime) {
        if (PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate)
                && PaymentInfo.validateCVV(cvv)) {
            User user = users.get(username);
            if (user != null) {
                System.out.println("Order before placing: " + order);
                System.out.println("Order items count: " + order.getItems().size());// need to get rid of these logging statements when finalising!!!!!!!!!!
                for (KingItem item : order.getItems()) {
                    System.out.println("Item: " + item.getName() + " Price: " + item.getPrice());
                }
                order.setOrderPlacedTime(orderPlacedTime);
                order.setStatus("placed");
                user.addOrder(order);
                System.out.println("Payment success: " + username + " Order ID: " + order.getOrderID() + " Items: " + order.getItems().size());
            }
        } else {
            System.out.println("Invalid payment: " + username);
        }
    }

	//Method used to export orders.
	public static void exportOrders(String username, List<Order> orders, String filePath, boolean exportOrderId, boolean exportStatus,
    boolean exportPlacedTime, boolean exportCollectedTime, boolean exportTotalPrice, boolean exportItems) {
		User user = users.get(username);
			if (user != null && orders != null && !orders.isEmpty()) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
					
					// Writes CSV header
					List<String> headers = new ArrayList<>();
						if (exportOrderId) headers.add("OrderID");
						if (exportStatus) headers.add("Status");
						if (exportPlacedTime) headers.add("PlacedTime");
						if (exportCollectedTime) headers.add("CollectedTime");
						if (exportTotalPrice) headers.add("TotalPrice");
						if (exportItems) headers.add("Items");
						writer.write(String.join(",", headers));
						writer.newLine();
		
						// Writes order details
						for (Order order : orders) {
							List<String> values = new ArrayList<>();
							if (exportOrderId) values.add(String.valueOf(order.getOrderID()));
							if (exportStatus) values.add(order.getStatus());
							if (exportPlacedTime) values.add(order.getOrderPlacedTime() != null ? order.getOrderPlacedTime().toString() : "");
							if (exportCollectedTime) values.add(order.getOrderCollectedTime() != null ? order.getOrderCollectedTime().toString() : "");
							if (exportTotalPrice) values.add(String.valueOf(order.calculateTotal()));
							if (exportItems) values.add(String.valueOf(order.getItems().size()));
							writer.write(String.join(",", values));
							writer.newLine();
						}
						System.out.println("Orders exported successfully to " + filePath);
				} catch (IOException e) {
					System.out.println("Failed to export orders: " + e.getMessage());
				}
			}
		}
				
	
//	public static void exportOrders(String username, List<Order> orders, String filePath) {
//	    User user = users.get(username);
//	    if (user != null && orders != null && !orders.isEmpty()) {
//	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//	            // Write CSV header
//	            writer.write("OrderID,Status,PlacedTime,CollectedTime,TotalPrice,Items");
//	            writer.newLine();
//	            
//	            // Write order details
//	            for (Order order : orders) {
//	                writer.write(order.getOrderID() + "," + order.getStatus() + "," + 
//	                             (order.getOrderPlacedTime() != null ? order.getOrderPlacedTime() : "") + "," + 
//	                             (order.getOrderCollectedTime() != null ? order.getOrderCollectedTime() : "") + "," + 
//	                             order.calculateTotal() + "," + order.getItems().size());
//	                writer.newLine();
//	            }
//	            System.out.println("Orders exported successfully to " + filePath);
//	        } catch (IOException e) {
//	            System.out.println("Failed to export orders: " + e.getMessage());
//	        }
//	    }
//	}
	
	
	//method used to cancel the order
	// also change the status to cancelled
	public static void cancelOrder(String username, int orderID) {
		
		User user = users.get(username);
	    if (user != null) {
	        Order order = user.getOrderById(orderID);
	        if (order != null && "placed".equals(order.getStatus())) {
	            order.setStatus("cancelled");
	            System.out.println("Order " + orderID + " cancelled.");
	        } else {
	            System.out.println("Order cannot be cancelled.");
	        }
	    } else {
	        System.out.println("User not found.");
	    }
	}
	
	// method that collects the order
	//sets the status to collected
	//sets the collection time as well
	  public static void collectOrder(String username, int orderID, LocalDateTime collectTime) {
	        User user = users.get(username);
	        if (user != null) {
	            Order order = user.getOrderById(orderID);
	            if (order != null && "placed".equals(order.getStatus()) && isValidCollectionTime(order, collectTime)) {
	                order.setStatus("collected");
	                order.setOrderCollectedTime(collectTime);
	                System.out.println("Order " + orderID + " collected.");
	            } else {
	                System.out.println("Invalid time to collect or order has been cancelled.");
	                System.out.println("Order Status: " + (order != null ? order.getStatus() : "Order not found"));//need to get rid of these logging statements soon
	                System.out.println("Collect Time: " + collectTime);
	                if (order != null) {
	                    System.out.println("Order Placed Time: " + order.getOrderPlacedTime());
	                    System.out.println("Preparation Time (minutes): " + UserManager.calculatePreparationTime(order));
	                    LocalDateTime minCollectTime = order.getOrderPlacedTime().plusMinutes(UserManager.calculatePreparationTime(order));
	                    System.out.println("Minimum Collect Time: " + minCollectTime);
	                }
	            }
	        }
	    }
	
	  //method used to find the order by its ID
	private static Order getOrderById(User user, int orderID) {
		for (Order order: user.getOrders()) {
			if (order.getOrderID() == orderID) {
				return order;
			}
		}
		return null;
	}
	
	//Method to see if collection time is ok based on the preptime
	//denies users the abiliy to pickup order before the order is prepared.
	public static boolean isValidCollectionTime(Order order,  LocalDateTime collectTime) {
		LocalDateTime orderPlacedTime = order.getOrderPlacedTime();
		int preperationTime = calculatePreparationTime(order);
		return collectTime.isAfter(orderPlacedTime.plusMinutes(preperationTime));
	}
	
	//method used to calculate the preparation time for the order.
	//checks the burritos and fries.
	public static int calculatePreparationTime(Order order) {
        int burritosCount = 0;
        int friesCount = 0;

        for (KingItem item : order.getItems()) {
            if (item.getName().equalsIgnoreCase("burrito")) {
                burritosCount++;
            } else if (item.getName().equalsIgnoreCase("fries")) {
                friesCount++;
            }
        }

        int burritosPrepTime = (int) Math.ceil(burritosCount / 2.0) * 9;
        int friesPrepTime = 0;
        if (friesCount > 0) {
            friesPrepTime = ((friesCount - 1) / 5 + 1) * 8;
        }
        return Math.max(burritosPrepTime, friesPrepTime);
    }
	
	//method  to get tje status or an order by its ID
	public static String getOrderStatus(int orderID, User user) {
        Order order = getOrderById(user, orderID);
        if (order != null) {
            return order.getStatus();
        }
        return "Order not found";
    }
	
	//This method is so the user can get all their previous orders.
	//i made it so its sorted with the time descending. 
	public static List<Order> getAllOrders(String username) {
        User user = users.get(username);
        if (user != null) {
            return user.getOrders().stream().sorted((o1, o2) -> o2.getOrderPlacedTime().compareTo(o1.getOrderPlacedTime())).collect(Collectors.toList());
        }
        return null;
    }

	
    public static void exportOrders(String username, List<Integer> orderIDs, String filePath, List<String> fields) {
        User user = users.get(username);
        if (user != null) {
            List<Order> orderToExport = user.getOrders().stream().filter(order -> orderIDs.contains(order.getOrderID())).collect(Collectors.toList());
        }
    }
}

