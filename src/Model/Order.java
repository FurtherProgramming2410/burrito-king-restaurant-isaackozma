package Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import Interface.KingItem;


//in this class is where the order process is
public class Order implements Serializable {
	
	//changed from my original A1 submission to handle the interface and meals
	//holds objects of kingitem.
	private static final long serialVersionUID = 1L;
    private List<KingItem> items = new ArrayList<>();
    private int orderID;
    private String status;
    private LocalDateTime orderPlacedTime;
    private LocalDateTime orderCollectedTime;
    private double actualPaidAmount; //change this name!!!!!!!!!!!!!
    
    private double totalAmount;// for credits
    
    private static int idCounter = 0;
    
    
    //constructor to initialise a order with new ID and status of order
    //orderID generates a random number
    //status is setting the starting status
    //items is just initialising the items 
    public Order() {
    	this.orderID = generateOrderID();
        this.status = "new";
        this.items = new ArrayList<>();
    	
//    	this.orderID = ++idCounter;
//    	this.status = "new";
    }
    
    
    //getter for the orderID
    public int getOrderID() {
    	return orderID;
    }

    //adds the numer of kingitem objects to the order
    //I used a loop to add the item to the items list.
    public void addItem(KingItem item, int quantity) {
    	for (int i = 0; i < quantity; i++) {
            this.items.add(item);
        }
    }

    //generates a number for the ID
    private int generateOrderID() {
        return new Random().nextInt(1000);// explain why 1000 was the limit, makes sense?
    }
    
    
    //calculates the total cost by going through each food item that is within item
    //returns the cost as a double
    public double calculateTotal() {
    	double total = 0;
        for (KingItem item : this.items) {
            total += item.getPrice();
        }
        return total;
    
    }

    //method is for the process of payments.
    //calculates the cost of the order then subtracts the amount paid
    //will be able to tell if its enough and then give change if needed.
    //also added and else which will tell the user if the amount given is not enough.

    
    public double processPayment(double amountPaid, int creditsUsed) {// added credits as well ,need to test to see if it works correctly
        double total = calculateTotal();
        double creditValue = creditsUsed / 100.0;//once finalised credit system, change from value, maybe credit amount, check with userManager so i dont get confused
        double finalTotal = total - creditValue;
        double change = amountPaid - finalTotal;
        if (change >= 0) {
            return change;
        } else {
            return -1;
        }
    }
    
    
    public double getActualPaidAmount() {
    	return actualPaidAmount;
    }
    
    //returns the list of objects to the order.
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
    
    
    	///////////////////////for credits
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    ////////////for credits
    
    //override method to display the orders details
    //order could not print accurately without it
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//need to change format now that it finally works correctly
        String formattedPlacedTime = orderPlacedTime != null ? orderPlacedTime.format(formatter) : "N/A";
        String formattedCollectedTime = orderCollectedTime != null ? orderCollectedTime.format(formatter) : "N/A";
        return "Order ID: " + orderID +
               "\nStatus: " + status +
               "\nPlaced Time: " + formattedPlacedTime +
               "\nCollected Time: " + formattedCollectedTime + 
//               "\nTotal Price: $" + String.format("%.2f", calculateTotal()) +
               "\nTotal Price: $" + String.format("%.2f", totalAmount) + // changed to accomidate the credits taking off the total cost!!
               "\nItems: " + items.size();
    }
}

