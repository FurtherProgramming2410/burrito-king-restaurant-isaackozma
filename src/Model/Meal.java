//this class was made to apply the discount that a meal provides the user. 
//the class implements the kingitem interface as was a requirement.
//made it final as it should not be changed as stated in the requirements.
//
package Model;

import Interface.KingItem;

//this class is for meals and has the discount applied
public class Meal implements KingItem {
	 private static final double DISCOUNT = 3.0;
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

