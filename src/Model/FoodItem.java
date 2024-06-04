package Model;


import java.io.Serializable;

import Interface.KingItem;

//this class is for all the food items within the restaurant.
//used for pricing, quantities and the naming
//implements the kingitem interface. 
//stores the name/price/quantity of the food item
public class FoodItem implements KingItem, Serializable{
	
	//A unique identifier for the class version used during deserialisation
	private static final long serialVersionUID = 1L;
	//Name, pirce and quantity of the fooditem
	private String name;
	private double price;
	private int quantity;
	 
	 //Constructor to initialize a food item with name, price and quantity
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
	 //method adds the specified qty to the current qty
	 public void updateQuantity(int quantity) {
		this.quantity += quantity;
	 }
    
	 //getter method for name
	 public String getName() {
		return name;
	 }
    
	 //getter method for price
	 public double getPrice() {
        return price;
	 }
    
	 //getter method for quantity
	 public int getQuantity() {
		return quantity;
	 }
    
	 //This method returns the name, price and qty of the food item
	 //Overiding the method to provide a string representation.
	 @Override
	 public String toString() {
       return name + ": $" + String.format("%.2f", price) + " (" + quantity + " available)";
	 }
}