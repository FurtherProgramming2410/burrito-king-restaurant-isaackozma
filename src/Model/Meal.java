package Model;

import java.io.Serializable;

import Interface.KingItem;

//this class is for meals and has the discount applied
//this class was made to apply the discount that a meal provides the user. 
//the class implements the kingitem interface as was a requirement.
//made it final as it should not be changed as stated in the requirements.
public class Meal implements KingItem, Serializable {
	private static final long serialVersionUID  = 1L;
	
	//Constant value for the discount applied to meals
	private static final double DISCOUNT = 3.0;
	
	// price of a meal after the discount is applied
	private double price;

	    //constructor which takes all the prices for the items in a meal and minus the discount.
	    public Meal(double burritoPrice, double friesPrice, double sodaPrice) {
	        this.price = burritoPrice + friesPrice + sodaPrice - DISCOUNT;
	    }

	    //Method to get the price of the meal
	    @Override
	    public double getPrice() {
	        return this.price;
	    }

	    //method to get the name of the meal
	    @Override
	    public String getName() {
	        return "Meal";
	    }
	}

