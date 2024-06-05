package Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import Interface.KingItem;


//This class represents an order in the burrito king program
//Ive implemented Serializable to allow order to be serialized and saved
public class Order implements Serializable {
	
	//Serial version UID for serialization
	//the List stores items in the order
	//orderID is unique for each order
	private static final long serialVersionUID = 1L;
    private List<KingItem> items = new ArrayList<>();
    private int orderID;
    private String status;
    private LocalDateTime orderPlacedTime;
    private LocalDateTime orderCollectedTime;
    private double actualPaidAmount; 
    private double totalAmount;
//    private static int idCounter = 0;//not current being used, can get rid of
    
    
    //constructor to initialise a order with new ID and status of order
    //orderID generates a random number
    //status is setting the starting status
    //items is just initialising the items 
    public Order() {
    	this.orderID = generateOrderID();
        this.status = "new";
        this.items = new ArrayList<>();

    }
    
    
    //getter for the orderID
    public int getOrderID() {
    	return orderID;
    }

    //adds the number of kingitem objects to the order
    //I used a for loop to add the item to the items list.
    public void addItem(KingItem item, int quantity) {
    	for (int i = 0; i < quantity; i++) {
            this.items.add(item);
        }
    }

    //this method generates a random number for the ID
	//made it to 1000 as I didnt know how many i should make it? 
    private int generateOrderID() {
        return new Random().nextInt(1000);
    }
    
    //this method is used to find the total price
    //calculates the total cost by going through each food item that is within item
    public double calculateTotal() {
    	double total = 0;
        for (KingItem item : this.items) {
            total += item.getPrice();
        }
        return total;
    
    }

    //method is for the process of payments.
    //takes the credits value if credits used
    public double processPayment(double amountPaid, int creditsUsed) {
        double total = calculateTotal();
        double creditValue = creditsUsed / 100.0;
        double finalTotal = total - creditValue;
        double change = amountPaid - finalTotal;
        if (change >= 0) {
            return change;
        } else {
            return -1;
        }
    }
    
    //getter for the actual amount paid
    public double getActualPaidAmount() {
    	return actualPaidAmount;
    }
    
    //getter for the list of objects in the order.
    public List<KingItem> getItems() {
        return items;
    }
    
    //getter for the status
    public String getStatus() {
        return status;
    }
    
    //setter for the status
    public void setStatus(String status) {
        this.status = status;
    }
    
    //getter for the order time
    public LocalDateTime getOrderPlacedTime() {
        return orderPlacedTime;
    }
    
    // setter for the order time
    public void setOrderPlacedTime(LocalDateTime orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }
    
    //getter for the time which orders collected
    public LocalDateTime getOrderCollectedTime() {
        return orderCollectedTime;
    }
    
    //setter for the timme which order is collected
    public void setOrderCollectedTime(LocalDateTime orderCollectedTime) {
        this.orderCollectedTime = orderCollectedTime;
    }
    
    
    //setter for the totalAmount
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    //getter for the totalAmount
    public double getTotalAmount() {
        return totalAmount;
    }
    
   
    
    //override method to display the orders details
    //formats the placed and collected times and it includes all details about the order.
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedPlacedTime = orderPlacedTime != null ? orderPlacedTime.format(formatter) : "N/A";
        String formattedCollectedTime = orderCollectedTime != null ? orderCollectedTime.format(formatter) : "N/A";
        return "Order ID: " + orderID +
               "\nStatus: " + status +
               "\nPlaced Time: " + formattedPlacedTime +
               "\nCollected Time: " + formattedCollectedTime + 
               "\nTotal Price: $" + String.format("%.2f", totalAmount) + 
               "\nItems: " + items.size();
    }
}

