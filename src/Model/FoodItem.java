//this class is for all the food items within the restaurant.
//used for pricing, quantities and the naming
//implements the kingitem interface. 
//stores the name/price/quantity of the food item
package Model;


import Interface.KingItem;

public class FoodItem implements KingItem{
	 private String name;
	 private double price;
	 private int quantity;
	 
	 //sets all the food items
	    public FoodItem(String name, double price, int quantity) {
	        this.name = name;
	        this.price = price;
	        this.quantity = quantity;
	    }
	    
	    //method to update the price and take a new price
	    public void updatePrice(double newPrice) {
	        this.price = newPrice;
	    }
	    
	    //method to update the quantity
	    public void updateQuantity(int quantity) {
	        this.quantity += quantity;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    public double getPrice() {
	        return price;
	    }
	    public int getQuantity() {
	        return quantity;
	    }
	    
	    //this override is to overrides the method from its superclass
	    @Override
	    public String toString() {
	        return name + ": $" + String.format("%.2f", price) + " (" + quantity + " available)";
}
}