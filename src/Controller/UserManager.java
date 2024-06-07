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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import Interface.KingItem;
import Model.Meal;
import Model.Order;
import Model.User;
import util.Alerts;


//this class is responsible for managing user data and operations.
//It implements singleton pattern that ensures only one instance of usermanager through the whole program
public class UserManager {
	
	//This static variable holds the single Instance of the UserManager class
	//I have implemented this as its part of my Singleton design pattern
	//by doing this it ensures that there is only one instance of UserManager through my program.
	private static UserManager instance;
	
	
	//Here I have a hash map that stores the users objects.
	//I also have constrants that define the files which users and orders data is saved/loaded from
	//I users these files as I couldnt for the life of me get my program to save with a database,
	//without making major changes to my code, that I wasnt sure I could accomplish. 
	//the fries remain variable tracks how many fries are still in the warmer.
	//giving users the ability to have no wait time for fries.
	private Map<String, User> users = new HashMap<>();
    private static final String USERS_FILE = "data/users.ser";
    private static final String ORDERS_FILE = "data/orders.ser";
    private User currentUser;
    private int friesRemain = 0;

    
    //This private constructor prevents direct instantiation
    //Initializes the UserManager by creating the data directory and loading user and orders from files.
    private UserManager() {
        createDataDirectory();
        loadUsersFromFile();
        loadOrdersFromFile();
    }
    
    //provides the access to the single instance of UserManager
    //Ensures its created in case it doesnt already exist.
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    //user managment methods
    
    
    //This method handles registering a new user. 
    //it takes the username, password, first & last name of the user
    //if the username is unique then registration will be sucessfull.
    //If the username already exist it will not be processed, this is shown in my registration class where it handles that.
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, firstName, lastName));
            saveUsersToFile();
            return true;
        }
        return false;
    }


    //This method handles loging the user in. 
    //the method takes the username and password.
    //if the password matches the username then the user will be logged in.
    //if the user enters nothing or the wrong password the user will get an alert
    //that is handled in my log in class.
    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }
    
    //method logs out the user
    //displays alert informing user.
    public void logoutUser() {
        currentUser = null;
        Alerts.infoMessage("Logged out", "User logged out.");
    }
    

    //gets the user by their username
    //returns the user if found
    //used for vip
    public User getUser(String username) {
		return users.get(username);
	}
    
    
    //this method allows for the user to update their profile, as in a profile that already exists
	//allows for the name and password to be updated. Not the username as it said not to in the assessment
    //after entering the details it saves the information to the user file
    //Alert to inform users that the update was successfull. 
    //uses info of the username, first & last name, and the password. 
    //it will set the new info when saved.
    public boolean updateUserProfile(String username, String firstName, String lastName, String password) {
    	User user = users.get(username);
	    if (user != null) {
	        user.setFirstName(firstName);
	        user.setLastName(lastName);
	        user.setPassword(password);
	        saveUsersToFile();
	        Alerts.infoMessage("Successful update", "Profile updated and saved for user: " + username);
	        return true;
	    }
	    Alerts.infoMessage("Wrong input", "Username not found" + username);
	    return false;
	}
  //user management methods
    
    
    
    //VIP methods
    
    //This method handles the user upgrading to VIP
    //if the username hasnt already been upgraded to vip and the email is vaild (which is handled in dashboard)
    //If everything is valid the user will upgrade and will save to the users file
    public boolean upgradeToVIP(String username, String email) {
        User user = users.get(username);
        if (user != null && email != null && !email.trim().isEmpty()) {
            user.setVIP(true);
            user.setEmail(email);
            saveUsersToFile();
            return true;
        }
        return false;
    }
    
    
    //this method handles the amount of credits a user has.
    //adds credit to the user. 
    //uses info from dashboard methods as well.
    public void addCredits(String username, int credits) {
		User user = users.get(username);
		if (user != null) {
			user.addCredits(credits);
		}
	}
   
    
    //this method handles the use of credits that a user has.
    //It will deduct the amount of credits that are wanted to be used by the user.
    //it takes the username and credits that are available and sees if its possible to be used.
    //if its all good then the credit will be used
    //otherwise the it will return false, aka not allowing for credits to be used.
    public boolean useCredits(String username, int creditsToUse) {
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
    
    //VIP methods
    
    
    //ordering methods
    
    //this method handles how placing an order work.
    //it takes the username, order, card details, time at which its placed and if any credits are used.
    //if all the details are valid it places the order and will save to both order and users file
    //I also have the credit system handled if the user is a VIP and using credits.
    //also takes the time at which the order was placed as the user enters that
    public void placeOrder(String username, Order order, String cardNumber, String expiryDate, String cvv, LocalDateTime orderPlacedTime, int creditsUsed) {
    	if (PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate)
                && PaymentInfo.validateCVV(cvv)) {
    		User user = users.get(username);
            if (user != null) {
                order.setOrderPlacedTime(orderPlacedTime);
                order.setStatus("placed");

                double totalAmount = order.calculateTotal();
                double finalAmount = totalAmount;

                //here i handle if the user is a vip
                //if they are and credits are used then i handle how it takes away from the credit they have
                //if they dont have enough credits then the user will be informed
                //it wont allow users to go negative in credit, user has to have enough.
                if (user.isVIP() && creditsUsed > 0) {
                    finalAmount -= (creditsUsed / 100.0);
                    if (finalAmount < 0) {
                        finalAmount = 0;
                    }
                    user.useCredits(creditsUsed);
                }
                order.setTotalAmount(finalAmount);
                user.addOrder(order);
                int creditsEarned = (int) finalAmount;
                user.addCredits(creditsEarned);
                saveOrdersToFile();
                saveUsersToFile();
            }
        } else {
            Alerts.errorMessage("Invalid Payment", "Invalid payment details provided.");
        }
    }
    
    //This method is for users collecting order.
    //Here i've made it so the username, orderID and the collection time are all needed for it to be successfull
    //The order needs to have a valid pickup time, otherwise an alert will appear.
    //Once the order is sucessfully picked up an alert will appear telling the user.
    //This will then be saved to the file and will now display in that the order is collected. 
    public void collectOrder(String username, int orderID, LocalDateTime collectTime) {
	    User user = users.get(username);
	    if (user != null) {
	        Order order = user.getOrderById(orderID);
	        if (order != null && "placed".equals(order.getStatus()) && isValidCollectionTime(order, collectTime)) {
	            order.setStatus("collected");
	            order.setOrderCollectedTime(collectTime);
	            Alerts.infoMessage("Orders Collected", "Order " + orderID + " collected.");
	            saveOrdersToFile(); 
	        } else {
	            Alerts.errorMessage("Invalid time", "Invalid time, cant collected at that time");
	        }
	    }
	}
    
    
    //method used to cancel the order
    //also change the status to cancelled
    //Takes the username and orderID 
    //saves data that the order has been changed to canceled.
    public void cancelOrder(String username, int orderID) {
 		User user = users.get(username);
 	    if (user != null) {
 	        Order order = user.getOrderById(orderID);
 	        if (order != null && "placed".equals(order.getStatus())) {
 	            order.setStatus("cancelled");
 	            saveOrdersToFile();
 	        } else {
 	            Alerts.errorMessage("Cannot cancel", "Order cannont be cancelled");
 	        }
 	    } else { 
 	        Alerts.errorMessage("Cannot cancel", "User not found");
 	    }
 	}
    
  //ordering methods
    
    
    //Order retrieval and exports methods
    
    //method used to find the order by its ID
    //it goes through all orders of a user an will return the order that matches the ID.
    private Order getOrderById(User user, int orderID) {
    	for (Order order: user.getOrders()) {
			if (order.getOrderID() == orderID) {
				return order;
			}
		}
		return null;
	}
    
    //This method is used to get the status of an order using the ID.
    //like multiple other methods this is using the order class.
    //if the order somehow is not found, will display order not found.
    public String getOrderStatus(int orderID, User user) {
        Order order = getOrderById(user, orderID);
        if (order != null) {
            return order.getStatus();
        }
        return "Order not found";
    }
    
    //This method is so the user can get all their previous orders.
    //i made it so its sorted with the time descending. 
    //simply returns it in a list formate
    public List<Order> getAllOrders(String username) {
        User user = users.get(username);
        if (user != null) {
            return user.getOrders().stream().sorted((order1, order2) -> order2.getOrderPlacedTime().compareTo(order1.getOrderPlacedTime())).collect(Collectors.toList());
        }
        return null;
    }
    
    
    //This method is used to export the information of orders to a CSV file.
    //This method is quite large as it takes all the information of an order and the user which placed it.
    //as specified by the assignemnt it allows for users to select what information they want on  the CSV
    //The GUI is slightly different on this page compared to others but it was the best way I could find to handle this information.
    //booleans are used to indicated whether to include the the specific information in the export.
    public void exportOrders(String username, List<Order> orders, String filePath, boolean exportOrderId, boolean exportStatus,
            boolean exportPlacedTime, boolean exportCollectedTime, boolean exportTotalPrice, boolean exportItems) {
    	//BufferedWrited used to write the CSV file.
		User user = users.get(username);
		if (user != null && orders != null && !orders.isEmpty()) {
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
				// here I write the CSV headers and use the array list
				//the listy holds the header names based on which exports are chosen
				List<String> csvHeaders = new ArrayList<>();
				if (exportOrderId) csvHeaders.add("OrderID");
				if (exportStatus) csvHeaders.add("Status");
				if (exportPlacedTime) csvHeaders.add("PlacedTime");
				if (exportCollectedTime) csvHeaders.add("CollectedTime");
				if (exportTotalPrice) csvHeaders.add("TotalPrice");
				if (exportItems) csvHeaders.add("Items");
				bufferedWriter.write(String.join(",", csvHeaders));
				bufferedWriter.newLine();
				
				// In this for loop the program writes the order details.
				//It cycles through the list and writes the corresponding values to the CSV file.
				for (Order order : orders) {
					List<String> csvValues = new ArrayList<>();
					if (exportOrderId) csvValues.add(String.valueOf(order.getOrderID()));
					if (exportStatus) csvValues.add(order.getStatus());
					if (exportPlacedTime) csvValues.add(order.getOrderPlacedTime() != null ? order.getOrderPlacedTime().toString() : "");
					if (exportCollectedTime) csvValues.add(order.getOrderCollectedTime() != null ? order.getOrderCollectedTime().toString() : "");
					if (exportTotalPrice) csvValues.add(String.valueOf(order.calculateTotal()));
					
					//Here i made it so it actually lists the items which are purchased.
					//I wasnt sure if it was just to have a count of items or the actaul items.
					//its a little messy but it does show what was actually order which i thought was more important.
					if (exportItems) {
						String itemNames = order.getItems().stream()
                            .map(KingItem::getName)
                            .collect(Collectors.joining(", "));
                        csvValues.add(itemNames);
					}
					bufferedWriter.write(String.join(",", csvValues)); 
					bufferedWriter.newLine();
				}
				//Alerts just to provide users info if it worked or not
				Alerts.infoMessage("Export success", "Orders exported successfully to " + filePath);
		} catch (IOException e) {
			Alerts.errorMessage("Failed to export orders", "Cannot export" + e.getMessage());
			}
		}
    }
    
 
   
    
  //Order retrieval and exports methods
    
    
    //Preptime methods
    
    //Method to see if collection time is ok based on the preptime
    //denies users the abiliy to pickup order before the order is prepared.
    public boolean isValidCollectionTime(Order order, LocalDateTime collectTime) {
  		LocalDateTime orderPlacedTime = order.getOrderPlacedTime();
  		int preperationTime = calculatePreparationTime(order);
  		return collectTime.isAfter(orderPlacedTime.plusMinutes(preperationTime));
  	}
      
  		
      //method used to calculate the preparation time for the order.
      //checks the burritos, fires and meals in the order to calculate the pretime
      
      // the preptime is in minutes are thats what was required.
      public int calculatePreparationTime(Order order) {
          int burritosCount = 0;
          int friesCount = 0;
          int mealsCount = 0;

          //for loop used to go through and add the items that were selected.
          //increased the count to get the proper value.
          for (KingItem item : order.getItems()) {
              if (item.getName().equalsIgnoreCase("burrito")) {
                  burritosCount++;
              } else if (item.getName().equalsIgnoreCase("fries")) {
                  friesCount++;
              }else if (item instanceof Meal) {
              	mealsCount++;
              }
          }
          
          //used to know how many burritos and fries there are
          //for wait time and if any fries are in the warmer.
          int totalBurritos = burritosCount + mealsCount;
          int totalFries = friesCount + mealsCount;

          //the wait time will be returned with whats the longest.
          //so if theres more wait for burritos than fries it will return that
          //out of the two times it will return which one is longer.
          //burritos take 9 min, two can be made at the same time.
          int burritosPrepTime = (int) Math.ceil(totalBurritos / 2.0) * 9;
          int friesPrepTime = calculateFriesWaitingTime(totalFries);
          return Math.max(burritosPrepTime, friesPrepTime);
      }
      
      
      //this method is used to calculate the waiting time of fries based on the number needed.
      //It keeps track of the fries that were previously order and that remain (in warmer)
      //fries take 8 min to make.
      private int calculateFriesWaitingTime(int friesNeeded) {
    	  int numberOfBatchesCooked = 0;
  	      int totalFriesRequired = friesNeeded - friesRemain;
  	      //here I have made the method calculate if fries are still in warmer or not
  	      //excessFriesFromLastBatch is like in warmer, just made more sense.
  	      if (totalFriesRequired > 0) {
  	    	  numberOfBatchesCooked = (int) Math.ceil((double) totalFriesRequired / 5);
  	    	  int excessFriesFromLastBatch = (numberOfBatchesCooked * 5) - totalFriesRequired;
  	    	  friesRemain = Math.min(excessFriesFromLastBatch, 5);
  	    	  } else {
  	    		  friesRemain -= friesNeeded;
  	    		  }
  	      return numberOfBatchesCooked * 8;
  	      }
      
    //Preptime methods
      
      
      
     //file handling methods
      

    //This method creates the data directory for users and orders files.
    //The method checks to make sure the file exists, and then makes one if it doesnt. 
    private void createDataDirectory() {
    	File dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
        	dataDirectory.mkdirs();
        }
    }

    //So this method loads the data from the user_file
    //I've used the ObjectInputStream to deserialize the data into the Map <String, User>
    //I have try and catch if there are any errors with reteiving the file
    //If the file isnt found, it will start an new empty list.
    //added suppress warnings to ignore warnings that occur while starting the program.
    @SuppressWarnings("unchecked")
    public void loadUsersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Map<String, User> loadedUsers = (Map<String, User>) objectInputStream.readObject();
            for (Map.Entry<String, User> entry : loadedUsers.entrySet()) {
                users.putIfAbsent(entry.getKey(), entry.getValue());
            }
        } catch (FileNotFoundException e) {
            Alerts.infoMessage("User file not found", "Starting with empty user list");
            
        } catch (IOException | ClassNotFoundException e) {
            Alerts.errorMessage("Error loading file", "Error loading users file" + e.getMessage());
        }
    }


    
    //This method saves the state of the user map to the user_file
    //It uses ObjectOutPutStream to  serialize the user data
    //If there is for some reason a error i have it so it will display a warning. 
    public void saveUsersToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            Alerts.infoMessage("Error saving user", "Error saving users " + e.getMessage());
        }
    }

    
    //This method loads the order data from order_file
    //It again uses objectinputstream to deserialize the data into the user map
    //Will have an alert if there are any issues.
    @SuppressWarnings("unchecked")
    public void loadOrdersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
            users = (Map<String, User>) objectInputStream.readObject(); 
        } catch (FileNotFoundException e) {
        	Alerts.infoMessage("Orders file not found", "Starting with an empty orders list." + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            Alerts.infoMessage("Error loading file", "Error loading file." + e.getMessage());
            
        }
    }
    

    //This method saves the state of the users map to the order_file
    //allows for the orders to be saved and therefore being displayed when restarting the program.
    //Again uses objectOuputStream to serialize the data.
    public void saveOrdersToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            objectOutputStream.writeObject(users);

	    } catch (IOException e) {
	    	Alerts.errorMessage("Error saving order", "Error saving order " + e.getMessage());
	    }
	}

     //this method saves the list of orders.
     //It serializes the list of orders and then writes it to the file.
     public void saveOrdersToFile(String filename, List<Order> orders) {
         try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
         	objectOutputStream.writeObject(orders);
         } catch (IOException e) {
        	 Alerts.infoMessage("Error saving order", "Error saving order " + e.getMessage());
         }
     }

     
     //this method loads the list of orders
     //it will deserializes the list from the file
     //using ObjectInputstream it deserializes the list and reads it from the file.
     @SuppressWarnings("unchecked")
     public List<Order> loadOrdersFromFile(String filename) {
         try (ObjectInputStream objectInputStream  = new ObjectInputStream(new FileInputStream(filename))) {
             return (List<Order>) objectInputStream.readObject();
         } catch (IOException | ClassNotFoundException e) {
             Alerts.infoMessage("Error Loading order", "Error saving order " + e.getMessage());
             return new ArrayList<>();
         }
     }
  
    
}



