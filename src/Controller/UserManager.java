package Controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Interface.KingItem;
import Model.Meal;
import Model.Order;
import Model.User;


//this class is responsible for managing user data and operations.
//
public class UserManager {
	
	private static UserManager instance;
	
	//this map stores users info
	private static Map<String, User> users = new HashMap<>();
    private static final String USERS_FILE = "data/users.ser";
    private static final String ORDERS_FILE = "data/orders.ser";
    private static User currentUser;
    private static int friesRemain = 0;

    
    private UserManager() {
//    static {
        createDataDirectory();
        loadUsersFromFile();
        loadOrdersFromFile();
    }
    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

//    private static void createDataDirectory() {
    private void createDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
//    public static void loadUsersFromFile() {
    public void loadUsersFromFile() {

        try (ObjectInputStream objectInputStream  = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Map<String, User> loadedUsers = (Map<String, User>) objectInputStream.readObject();
            for (Map.Entry<String, User> entry : loadedUsers.entrySet()) {
                users.putIfAbsent(entry.getKey(), entry.getValue());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found, starting with an empty user list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }


    
//    public static void saveUsersToFile() {
    public void saveUsersToFile() {
        System.out.println("Attempting to save users to file...");
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
        	objectOutputStream.writeObject(users);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    

    @SuppressWarnings("unchecked")
//    public static void loadOrdersFromFile() {
    public void loadOrdersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
            users = (Map<String, User>) objectInputStream.readObject(); // Cast to the correct type
            System.out.println("Orders loaded successfully for all users.");
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found, starting with an empty orders list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }
    
	// method that collects the order
	//sets the status to collected
	//sets the collection time as well
//	  public static void collectOrder(String username, int orderID, LocalDateTime collectTime) {
//	        User user = users.get(username);
//	        if (user != null) {
//	            Order order = user.getOrderById(orderID);
//	            if (order != null && "placed".equals(order.getStatus()) && isValidCollectionTime(order, collectTime)) {
//	                order.setStatus("collected");
//	                order.setOrderCollectedTime(collectTime);
//	                System.out.println("Order " + orderID + " collected.");
//	            } else {
//	                System.out.println("Invalid time to collect or order has been cancelled.");
//	                System.out.println("Order Status: " + (order != null ? order.getStatus() : "Order not found"));//need to get rid of these logging statements soon
//	                System.out.println("Collect Time: " + collectTime);
//	                if (order != null) {
//	                    System.out.println("Order Placed Time: " + order.getOrderPlacedTime());
//	                    System.out.println("Preparation Time (minutes): " + UserManager.calculatePreparationTime(order));
//	                    LocalDateTime minCollectTime = order.getOrderPlacedTime().plusMinutes(UserManager.calculatePreparationTime(order));
//	                    System.out.println("Minimum Collect Time: " + minCollectTime);
//	                }
//	            }
//	        }
//	    }
	
//	public static void collectOrder(String username, int orderID, LocalDateTime collectTime) {
    public void collectOrder(String username, int orderID, LocalDateTime collectTime) {
	    User user = users.get(username);
	    if (user != null) {
	        Order order = user.getOrderById(orderID);
	        if (order != null && "placed".equals(order.getStatus()) && isValidCollectionTime(order, collectTime)) {
	            order.setStatus("collected");
	            order.setOrderCollectedTime(collectTime);
	            System.out.println("Order " + orderID + " collected.");
	            saveOrdersToFile(); 
	        } else {
	            System.out.println("Invalid time to collect or order has been cancelled.");
	        }
	    }
	}


    
//	public static void saveOrdersToFile() {
    public void saveOrdersToFile() {
	    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
	    	objectOutputStream.writeObject(users); 
	        System.out.println("Orders saved successfully for all users.");
	    } catch (IOException e) {
	        System.out.println("Error saving orders: " + e.getMessage());
	    }
	}

//    public static boolean registerUser(String username, String password, String firstName, String lastName) {
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, firstName, lastName));
            saveUsersToFile();
            return true;
        }
        return false;
    }

//    public static User loginUser(String username, String password) {
    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

	
	
    
    
	
	
	
	// this method is used to validate the payment details
		//tells users if it worked or not
		//when payments is successful then the order will be added to the list
//		 public static void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv, LocalDateTime orderPlacedTime, int creditsUsed) {
//	        if (PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate)
//	                && PaymentInfo.validateCVV(cvv)) {
//	            User user = users.get(username);
//	            if (user != null) {
//	                System.out.println("Order before placing: " + order);
//	                System.out.println("Order items count: " + order.getItems().size());// need to get rid of these logging statements when finalising!!!!!!!!!!
//	                for (KingItem item : order.getItems()) {
//	                    System.out.println("Item: " + item.getName() + " Price: " + item.getPrice());
//	                }
//	                order.setOrderPlacedTime(orderPlacedTime);
//	                order.setStatus("placed");
//	                
//	                double totalAmount = order.calculateTotal();
//	                double finalAmount = totalAmount;
//	                
//	                if(user.isVIP()) {
//	                	finalAmount = totalAmount -(creditsUsed / 100.0);//for credits
//	                	user.addCredits((int) finalAmount);//for credits
//	                	user.setCredits(user.getCredits()- creditsUsed);//for credits
//	                }
//	                
//	                order.setTotalAmount(finalAmount);//for credits
//	                
//	                user.addOrder(order);
//	                System.out.println("Payment success: " + username + " Order ID: " + order.getOrderID() + " Items: " + order.getItems().size());
//	            }
//	        } else {
//	            System.out.println("Invalid payment: " + username);
//	        }
//	    }
    
    
//    public static void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv, LocalDateTime orderPlacedTime, int creditsUsed) {
    public void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv, LocalDateTime orderPlacedTime, int creditsUsed) {
    	System.out.println("Attempting to place order...");
    	if (PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate)
                && PaymentInfo.validateCVV(cvv)) {
            User user = users.get(username);
            if (user != null) {
                order.setOrderPlacedTime(orderPlacedTime);
                order.setStatus("placed");

                double totalAmount = order.calculateTotal();
                double finalAmount = totalAmount;

                if (user.isVIP()) {
                    finalAmount = totalAmount - (creditsUsed / 100.0);
                    user.addCredits((int) finalAmount);
                    user.setCredits(user.getCredits() - creditsUsed);
                }

                order.setTotalAmount(finalAmount);
                user.addOrder(order);
                System.out.println("Order placed successfully for user: " + username + ". Order ID: " + order.getOrderID());
                saveOrdersToFile(); 
            }
        } else {
            System.out.println("Invalid payment details provided.");
        }
    }
	


	
	
	
	////////////////////////////////////////////// vip stufff
//	public static boolean upgradeToVIP(String username, String email) {
    public boolean upgradeToVIP(String username, String email) {
        User user = users.get(username);
        if (user != null && email != null && !email.trim().isEmpty()) {//// might have to change this when changing requirments for email?
            user.setVIP(true);
            user.setEmail(email);
            return true;
        }
        return false;
    }
	
//	public static void addCredits(String username, int credits) {
    public void addCredits(String username, int credits) {
		User user = users.get(username);
		if (user != null) {
			user.addCredits(credits);
		}
	}
	
//	public static boolean useCredits(String username, int creditsToUse) {
    public boolean useCredits(String username, int creditsToUse) {// idk if i like the name credits to use? maybe creditsavalible?
	    User user = users.get(username);
	    if (user != null) {
	        int userCredits = user.getCredits();
	        if (creditsToUse <= userCredits) {
	            user.setCredits(userCredits - creditsToUse);
	            return true;
	        }
	    }
	    return false;
	}
	
	///////////////////////////////vip stuff
	
//	public static User getUser(String username) {/// added with vip stuff, might get rid of
    public User getUser(String username) {
		return users.get(username);
	}
	
	//this method is to ensure the login is correct with the username and password
	
	
	// method to log user out. 
	//still needs to be finished correctly
//	public static void logoutUser() {
    public void logoutUser() {
        currentUser = null;
        System.out.println("User logged out.");// change to an alert
    }

	
	//this method allows for the user to update their profile, as in a profile that already exists
	//allows for the name and password to be updated. Not the username as it said not to in the assessment
//	public static boolean updateUserProfile(String username, String firstName, String lastName, String password) {
    public boolean updateUserProfile(String username, String firstName, String lastName, String password) {
	    System.out.println("Attempting to update profile for user: " + username);
	    User user = users.get(username);
	    if (user != null) {
	        user.setFirstName(firstName);
	        user.setLastName(lastName);
	        user.setPassword(password);
	        saveUsersToFile(); 
	        System.out.println("Profile updated and saved for user: " + username);//change to alert
	        return true;
	    }
	    System.out.println("User not found: " + username);//change to alert 
	    return false;
	}

	
	
	

	//Method used to export orders.
//	public static void exportOrders(String username, List<Order> orders, String filePath, boolean exportOrderId, boolean exportStatus,
//    boolean exportPlacedTime, boolean exportCollectedTime, boolean exportTotalPrice, boolean exportItems) {
    public void exportOrders(String username, List<Order> orders, String filePath, boolean exportOrderId, boolean exportStatus,
            boolean exportPlacedTime, boolean exportCollectedTime, boolean exportTotalPrice, boolean exportItems) {
		User user = users.get(username);
			if (user != null && orders != null && !orders.isEmpty()) {
				try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
					
					// Writes CSV header
					List<String> csvHeaders = new ArrayList<>();
						if (exportOrderId) csvHeaders.add("OrderID");
						if (exportStatus) csvHeaders.add("Status");
						if (exportPlacedTime) csvHeaders.add("PlacedTime");
						if (exportCollectedTime) csvHeaders.add("CollectedTime");
						if (exportTotalPrice) csvHeaders.add("TotalPrice");
						if (exportItems) csvHeaders.add("Items");
						bufferedWriter.write(String.join(",", csvHeaders));
						bufferedWriter.newLine();
		
						// Writes order details
						for (Order order : orders) {
							List<String> csvValues = new ArrayList<>();
							if (exportOrderId) csvValues.add(String.valueOf(order.getOrderID()));
							if (exportStatus) csvValues.add(order.getStatus());
							if (exportPlacedTime) csvValues.add(order.getOrderPlacedTime() != null ? order.getOrderPlacedTime().toString() : "");
							if (exportCollectedTime) csvValues.add(order.getOrderCollectedTime() != null ? order.getOrderCollectedTime().toString() : "");
							if (exportTotalPrice) csvValues.add(String.valueOf(order.calculateTotal()));
//							if (exportItems) values.add(String.valueOf(order.getItems().size()));// this is for just number of items
							if (exportItems) {
		                        String itemNames = order.getItems().stream()
		                            .map(KingItem::getName)
		                            .collect(Collectors.joining(", "));
		                        csvValues.add(itemNames);
							}// this one handles the names of items, kinda wacky but works!
							bufferedWriter.write(String.join(",", csvValues));
							bufferedWriter.newLine();
						}
						System.out.println("Orders exported successfully to " + filePath);
				} catch (IOException e) {
					System.out.println("Failed to export orders: " + e.getMessage());
				}
			}
		}
				
	

	
	
	//method used to cancel the order
	// also change the status to cancelled
//	public static void cancelOrder(String username, int orderID) {
    public void cancelOrder(String username, int orderID) {
		
		User user = users.get(username);
	    if (user != null) {
	        Order order = user.getOrderById(orderID);
	        if (order != null && "placed".equals(order.getStatus())) {
	            order.setStatus("cancelled");
	            saveOrdersToFile();
	            System.out.println("Order " + orderID + " cancelled.");// have to change to alert
	        } else {
	            System.out.println("Order cannot be cancelled.");
	        }
	    } else {
	        System.out.println("User not found.");
	    }
	}
	

	
	  //method used to find the order by its ID
//	private static Order getOrderById(User user, int orderID) {
    private Order getOrderById(User user, int orderID) {
		for (Order order: user.getOrders()) {
			if (order.getOrderID() == orderID) {
				return order;
			}
		}
		return null;
	}
	
	//Method to see if collection time is ok based on the preptime
	//denies users the abiliy to pickup order before the order is prepared.
//	public static boolean isValidCollectionTime(Order order,  LocalDateTime collectTime) {
    
    public boolean isValidCollectionTime(Order order, LocalDateTime collectTime) {
		LocalDateTime orderPlacedTime = order.getOrderPlacedTime();
		int preperationTime = calculatePreparationTime(order);
		return collectTime.isAfter(orderPlacedTime.plusMinutes(preperationTime));
	}
	
	//method used to calculate the preparation time for the order.
	//checks the burritos and fries.
//	public static int calculatePreparationTime(Order order) {
    public int calculatePreparationTime(Order order) {
        int burritosCount = 0;
        int friesCount = 0;
        int mealsCount = 0;

        for (KingItem item : order.getItems()) {
            if (item.getName().equalsIgnoreCase("burrito")) {
                burritosCount++;
            } else if (item.getName().equalsIgnoreCase("fries")) {
                friesCount++;
            }else if (item instanceof Meal) {
            	mealsCount++;
            }
        }
        
        int totalBurritos = burritosCount + mealsCount;
        int totalFries = friesCount + mealsCount;

        int burritosPrepTime = (int) Math.ceil(totalBurritos / 2.0) * 9; // changed
        int friesPrepTime = calculateFriesWaitingTime(totalFries);
//        int friesPrepTime = 0;
//        if (totalFries > 0) {
//            friesPrepTime = ((totalFries - 1) / 5 + 1) * 8;
//        }
        return Math.max(burritosPrepTime, friesPrepTime);
    }
	
//	 private static int calculateFriesWaitingTime(int friesNeeded) {
    private int calculateFriesWaitingTime(int friesNeeded) {
	        int numberOfBatchesCooked = 0;
	        int totalFriesRequired = friesNeeded - friesRemain;
	        if (totalFriesRequired > 0) {
	            numberOfBatchesCooked = (int) Math.ceil((double) totalFriesRequired / 5);
	            int excessFriesFromLastBatch = (numberOfBatchesCooked * 5) - totalFriesRequired;
	            friesRemain = Math.min(excessFriesFromLastBatch, 5);
	        } else {
	            friesRemain -= friesNeeded;
	        }
	        return numberOfBatchesCooked * 8;
	    }
	
	//method  to get tje status or an order by its ID
//	public static String getOrderStatus(int orderID, User user) {
    public String getOrderStatus(int orderID, User user) {
        Order order = getOrderById(user, orderID);
        if (order != null) {
            return order.getStatus();
        }
        return "Order not found";//??????????????
    }
	
	//This method is so the user can get all their previous orders.
	//i made it so its sorted with the time descending. 
//	public static List<Order> getAllOrders(String username) {
    public List<Order> getAllOrders(String username) {
        User user = users.get(username);
        if (user != null) {
            return user.getOrders().stream().sorted((order1, order2) -> order2.getOrderPlacedTime().compareTo(order1.getOrderPlacedTime())).collect(Collectors.toList());
        }
        return null;
    }

	
//    public static void exportOrders(String username, List<Integer> orderIDs, String filePath, List<String> fields) {
    public void exportOrders(String username, List<Integer> orderIDs, String filePath, List<String> fields) {
        User user = users.get(username);
        if (user != null) {
            List<Order> orderToExport = user.getOrders().stream().filter(order -> orderIDs.contains(order.getOrderID())).collect(Collectors.toList());
        }
    }
    
//    public static void saveOrdersToFile(String filename, List<Order> orders) {
    public void saveOrdersToFile(String filename, List<Order> orders) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
        	objectOutputStream.writeObject(orders);
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
//	public static List<Order> loadOrdersFromFile(String filename) {
    public List<Order> loadOrdersFromFile(String filename) {
        try (ObjectInputStream objectInputStream  = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Order>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
//    private static void ensureDirectoryExists(String filename) {
    private void ensureDirectoryExists(String filename) {
        Path path = Paths.get(filename).getParent();
        if (path != null && !Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("Error creating directories: " + e.getMessage());
            }
        }
    }
    
    
}



