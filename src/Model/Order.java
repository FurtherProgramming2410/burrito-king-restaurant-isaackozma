package Model;
import java.util.ArrayList;

import java.util.List;

import Interface.KingItem;


//in this class is where the order process is
public class Order {
	
	//changed from my original A1 submission to handle the interface and meals
	//holds objects of kingitem.
    private List<KingItem> items = new ArrayList<>();

    //adds the numer of kingitem objects to the order
    //I used a loop to add the item to the items list.
    public void addItem(KingItem item, int quantity) {
    	for (int i = 0; i < quantity; i++) {
            this.items.add(item);
        }
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
    public double processPayment(double amountPaid) {
        double total = calculateTotal();
        double change = amountPaid - total;
        if (change >= 0) {
            return change;
        } else {
            return -1; 
        }
    }
    
    //returns the list of objects to the order.
    public List<KingItem> getItems() {
        return items;
    }
}