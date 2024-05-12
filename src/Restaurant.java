import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;


//this is my restaurant class
//it runs all the menus, handles all the orders, has the sales report, has the fries inventory 
//Is responsible for price updates

public class Restaurant {
	private Map<String, FoodItem> menu;
    private int friesRemain;


    // this is the constructor method for this class
    //I have made my menu as a hash map to store all the menu items.
    // ive initialised the price for all of my products
    // i have also set the starting value for how many "fries remain"
    public Restaurant() {

        this.menu = new HashMap<>();
        this.menu.put("burrito", new FoodItem("Burrito", 7, 0));
        this.menu.put("fries", new FoodItem("Fries", 4, 5));
        this.menu.put("soda", new FoodItem("Soda", 2.5, 0));
        this.friesRemain = 0;
    }

    
    // here i have made a method to take the order of a customer
    //Will update the count of the purchases
    //handles fries being cooked.
    //menu is in a loop so it is presented to the customer until they choose to stop it.
    public Order takeOrder() {
        Order order = new Order();
        Scanner scanner = new Scanner(System.in);
        int totalBurritos = 0; 
        int friesOrdered = 0; 
        int waitingTime = friesAvailability();
        
        //loop allows user to add as many items they want until they are finished by selecting no more
        while (true) {
            System.out.println("  1. Burrito\n  2. Fries\n  3. Soda\n  4. Meal\n  5. No more");
            System.out.print("Please select: ");
            String choice = scanner.nextLine();
            
            
            //option 1 is for burritos
            //takes the users input of how many burritos they want and updates the total.
            //adds it to the current order and also adds the wait time.
            if ("1".equals(choice)) {
            	int quantity = ensureCorrectInput(scanner, "How many burritos would you like to buy: ");
                totalBurritos += quantity; 
                order.addItem(menu.get("burrito"), quantity);
                
             // Option 2 is for fries
             //here i update the total fries, i also deduct the amount of remaining fries (if any)
             //Then i add the wait times, it is important to do this after cooking the fries
             //otherwise code will not function correctly at all. 
            } else if ("2".equals(choice)) {
            	int quantity = ensureCorrectInput(scanner, "How many fries would you like to buy: ");
            	friesOrdered += quantity;
            	cookFries(quantity);
            	int friesWaitingTime = calculateFriesWaitingTime(quantity);
            	waitingTime = Math.max(waitingTime, friesWaitingTime);
            	order.addItem(menu.get("fries"), quantity);

            	
            //Option 3 is for sodas
            // was by far the easiest!
            //takes users input for how many soads they want and adds it to the order
            // no wait time calculation was needed.
            } else if ("3".equals(choice)) {
            	int quantity = ensureCorrectInput(scanner, "How many sodas would you like to buy: ");;
                order.addItem(menu.get("soda"), quantity);

            //option 4 is for meals
            //adds all the items to the meal
            //puts one of each item in the meal
            //also cooks fries and which allows for there to be fries in warmer
            } else if ("4".equals(choice)) {
            	  int mealQuantity = ensureCorrectInput(scanner, "How many meals would you like to buy: ");
            	    totalBurritos += mealQuantity;
            	    friesOrdered += mealQuantity;
            	    cookFries(mealQuantity); 
            	    for (int i = 0; i < mealQuantity; i++) {
            	        order.addItem(new Meal(menu.get("burrito").getPrice(), menu.get("fries").getPrice(), menu.get("soda").getPrice()), 1);
            	    }
            
            //option five is just to have no more items, ends loop.
            }else if("5".equals(choice)) {
                break;
                
            //i've kept this here incase any other invalid choices are selected.
            } else {
                System.out.println("Invalid choice");
            }
        }
        
        //calls method from order class and adds up the sum. 
        double totalCost = order.calculateTotal();

        //calculates the max waiting time, depending on what is being ordered
        waitingTime = Math.max(waitingTime, calculateBurritosWaitingTime(totalBurritos));
     
        //takes the total waiting time and is used to show users the time itll take for the order.
        int totalWaitingTime = waitingTime;

        // this is just to show the user the waiting time, and cost of the order. 
        System.out.println("\nTotal for " + totalBurritos + " Burritos and " + friesOrdered + " Fries is $" + String.format("%.2f", totalCost));
        System.out.println("Waiting time for the order is: " + totalWaitingTime + " minutes");

        return order;

    }
    
    //Used to ensure the correct value is inputed
    //checks to make sure the user didnt enter nothing, otherwise program will crash.
    //will loop until a correct input is entered. 
    //ends up returning the correct input.
    private int ensureCorrectInput(Scanner scanner, String prompt) {
        int quantity = 0;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (!input.isEmpty() && input.matches("\\d+")) {
                quantity = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
        return quantity;
    }
    
    
    // this is the method used to calculate the waiting time for the burritos
    // it does this by taking the number of burrtios needed, comparing that to the cycle of which they can be cooked, two at a time
    //using the math.ceil
    //then calculates the number of cooking cycles itll take and given it takes 9 min for burritos to cook
    // it runs off that information.
    private int calculateBurritosWaitingTime(int burritosOrdered) {
        int numberCookingCycles = (int) Math.ceil((double) burritosOrdered / 2);
        return numberCookingCycles * 9; 
    }
    
    
    // so this method calclates the waiting time based on the previous orders.
    //if there are fries available then no wait time (yay!)
    //if none left then it will be an 8 minute wait.
    private int friesAvailability() {
        int friesRemaining = menu.get("fries").getQuantity();
        return friesRemaining > 0 ? 0 : 8; 
    }
    
    
    //this method calculates the wait time for fries based on how many batches need to be cooked
    //needed to make this seprate as when i tired to join it with friesavailabily it never worked as expected.
    private int calculateFriesWaitingTime(int friesNeeded) {
        return numberOfBatchesCooked * 8; 
    }
    private int numberOfBatchesCooked = 0; 

    
    
    //this method is responsible for caculating how many fries are needed beyond what the shop already has
    // it takes all the fries that are left in the warmer and sees how many more are needed.
    // then I made it so it can figure the number of batches it needs to make
    //if there were enough fries then it takes away from the fries that were left in the warmer and no wait time is added
    //needed to ensure that it does not exceed 5 being made in each batch
    private void cookFries(int friesNeeded) {
    	numberOfBatchesCooked = 0;
	    int totalFriesRequired = friesNeeded - friesRemain;
	    if (totalFriesRequired > 0) {
	        numberOfBatchesCooked = (int) Math.ceil((double) totalFriesRequired / 5);
	        int excessFriesFromLastBatch = (numberOfBatchesCooked * 5) - totalFriesRequired;
	        friesRemain = Math.min(excessFriesFromLastBatch, 5); 
	    } else {
	        friesRemain -= friesNeeded;
	    }
    }
    
    //simply returns the amount of fries that are remaining in the warming tray.
    public int getFriesRemain() {
        return friesRemain;
    }


    // this method is for updating the price on the burritos, fries and sodas.
    //displays the menu options to the users
    //so i used menuIdentifier to allow for options 1-3 to correspond correctly with the menu Hash map.
    // i didnt add any meal price change as that uses the price of the individual items.
    public void updatePrices() {
    	Scanner scanner = new Scanner(System.in);
        System.out.println("  1. Burrito\n  2. Fries\n  3. Soda\n  4. Exit");
        System.out.print("Please select the food item to update the price: ");
        String userChoice = scanner.nextLine();
        
        if ("1".equals(userChoice) || "2".equals(userChoice) || "3".equals(userChoice)) {
        	String itemKey = "";
            if ("1".equals(userChoice)) {
                itemKey = "burrito";
            } else if ("2".equals(userChoice)) {
                itemKey = "fries";
            } else if ("3".equals(userChoice)) {
                itemKey = "soda";
            }
            FoodItem item = menu.get(itemKey);
                
                //here i give the user the option to change the price and it will change to whatever the user states.
                //error statement used to ensure the user chooses a valid choice. 
                if (item != null) {
                    System.out.print("Please enter new price: ");
                    
                    double newPrice = correctPrice(scanner, "Please enter new price: ");
                    item.updatePrice(newPrice);
                    System.out.println("The unit price of " + item.getName() + " is updated to $" + newPrice);
                }            
        	}else if(!"4".equals(userChoice)){
                	System.out.println("Invalid choice");
            
                }
            }
    
    
    //I used this to make sure the price the user selects is correct
    //cant be anything other than a number, made it a double so the user can add cents. 
    private double correctPrice(Scanner scanner, String prompt) {
    	double price = 0.0;
    	while (true) {
    		System.out.println(prompt);
    		String input = scanner.nextLine();
    		
    		if(!input.isEmpty() && input.matches("-?\\d+(\\.\\d+)?")) {
    			price = Double.parseDouble(input);
    			break;
    		}else {
    			System.out.println("Invalid input, please enter a number value");
    		}
    	}
    	return price;
    }
    

    
    //this method shows the sales report to the user.
    // it also displays the unsold fries with  "friesRemain"
    //the method iterates through each "FoodItem" to determine the number of sale for each.
    //takes the total amount of money made from each item as well
    public void showSalesReport(List<Order> orders) {
    	
    	//got two hashmaps, one which maps the name and how many times it was sold
    	//another that maps the total dollar figure made by the item.
    	double totalSales = 0;
        System.out.println("\nUnsold Serves of Fries: " + friesRemain);
        System.out.println("\nTotal Sales:");
        Map<String, Integer> itemCounts = new HashMap<>();
        Map<String, Double> itemSales = new HashMap<>();

        //the outer loop goes over every order
        // the nested loop goes over all the kingitems in the order
        //i update the itemcounts and make sure it exist with the default value.
        //same thing with the itemsales
        //also if its a meal i made it so the prices are added individually. 
        //then itemsales at the end updates the total dollar amount made from all the items.
        for (Order order : orders) {
            for (KingItem orderedItem : order.getItems()) {
                String itemName = orderedItem.getName().toLowerCase();
                itemCounts.putIfAbsent(itemName, 0);
                itemCounts.put(itemName, itemCounts.get(itemName) + 1);
                itemSales.putIfAbsent(itemName, 0.0);
                double dollarsMade = orderedItem.getPrice();
                if (orderedItem instanceof Meal) {
                	dollarsMade = menu.get("burrito").getPrice() + menu.get("fries").getPrice() + menu.get("soda").getPrice();
                }
                itemSales.put(itemName, itemSales.get(itemName) + dollarsMade);
            }
        }

       
        //loop goes over item names within the menu keyset
        //finds the total of items sold and the money made
        //it then prints out the amount sold and the amount made
        //also made it so if nothing is sold that it will just display 0
        for (String itemName : menu.keySet()) {
            Integer soldQuantity = itemCounts.getOrDefault(itemName, 0);
            Double dollarsMade = itemSales.getOrDefault(itemName, 0.0); 
            System.out.println(itemName + "s: " + soldQuantity + " $" + String.format("%.2f", dollarsMade));
            totalSales += dollarsMade;
        }

        // had to add this because of meals
        //basically just adds meals to the display if any were sold.
        //wont display if none were sold
        //based on information given in A1 i dont need to show it if nothing was sold... i think
        if (itemCounts.containsKey("meal")) {
            int mealCount = itemCounts.get("meal");
            double mealSales = itemSales.get("meal");
            System.out.println("Meals: " + mealCount + " $" + String.format("%.2f", mealSales));
            totalSales += mealSales;
        }

        System.out.println("----------------");
        System.out.println("Total: $" + String.format("%.2f", totalSales));
        System.out.println("----------------");
    }
    
      
    //here the array list is used to keep track of all the orders
    //the main loop runs to display the main menu.
    // the loop will run until user is finished.
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        List<Order> orders = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
        	System.out.println("================================");
        	System.out.println("Burrito King");
        	System.out.println("================================");
            System.out.println("  a) Order\n  b) Show sales report\n  c) Update prices\n  d) Exit\n");
            System.out.print("Please select: ");
            String choice = scanner.nextLine().toLowerCase();
            
            //for a the payment section is taken care of within
            //the payment process will repeat until the correct amount of money is entered.
            //inside i put the ability to ensure the input cannot be empty. 
            //prints how many fries will be left for the next order. 
            if ("a".equals(choice)) {
                Order order = restaurant.takeOrder();
                orders.add(order);
                double totalCost = order.calculateTotal();
                System.out.println("\nTotal for the order is $" + String.format("%.2f", totalCost));
                while (true) {
                    System.out.print("Please enter money: ");
                    String input = scanner.nextLine();
                    if (!input.isEmpty() && input.matches("-?\\d+(\\.\\d+)?")) {
                        double amountPaid = Double.parseDouble(input);
                        double change = order.processPayment(amountPaid);
                        
                        if (change >= 0) {
                            System.out.println("Change returned $" + String.format("%.2f", change));
                            System.out.println(restaurant.getFriesRemain() + " serves of fries will be left for the next order.");
                            break; 
                        } else {
                            System.out.println("Sorry, that’s not enough to pay for the order.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid numeric value.");
                    }
                }
            } else if ("b".equals(choice)) {
                restaurant.showSalesReport(orders);
            } else if ("c".equals(choice)) {
                restaurant.updatePrices();
            } else if ("d".equals(choice)) {
                System.out.println("Bye Bye.");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        scanner.close();

    }
}