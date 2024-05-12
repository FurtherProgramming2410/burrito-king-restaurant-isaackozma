//this class was made to apply the discount that a meal provides the user. 
//the class implements the kingitem interface as was a requirement.
//made it final as it should not be changed as stated in the requirements.
//
public class Meal implements KingItem {
	 private static final double DISCOUNT = 3.0;
	    private double price;

	    //constructor which takes all the prices for the items in a meal and minus the discount.
	    public Meal(double burritoPrice, double friesPrice, double sodaPrice) {
	        this.price = burritoPrice + friesPrice + sodaPrice - DISCOUNT;
	    }

	    @Override
	    public double getPrice() {
	        return this.price;
	    }

	    @Override
	    public String getName() {
	        return "Meal";
	    }
	}

