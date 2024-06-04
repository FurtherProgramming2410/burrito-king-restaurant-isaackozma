package View;
import Controller.UserManager;
import Model.User;
import View.OrderOnDashBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;


//starting point of the program
//It is the main class for the program.
public class BurritoKingApp extends Application{
	
	//static variable to hold the primary stage application
	//Using static variable to avoid multiple instances.
	//instances used to manage and switch between different screens. 
	private static Stage startingStage;
    private Dashboard dashboard;
    private Profile profile;
    private OrderOnDashBoard orderOnDashBoard;
	
	//start method is the entry point of the javafx
    //gets the instacne of Usermanager thats using the Singleton pattern
    //it loads the users data from the the file.
    //I assign the primary stage to the static variable to startingstage
    //I initalise the dashboard and orderondashboard instances
	@Override
	public void start (Stage primaryStage) {
		UserManager userManager = UserManager.getInstance();
        userManager.loadUsersFromFile();
        startingStage = primaryStage;
        dashboard = new Dashboard(this);
        orderOnDashBoard = new OrderOnDashBoard(this);
        
        //I call the title so users know where they are
        //and i take the user to the login screen.
        //i make this the primary stage
        startingStage.setTitle("Burrito King Restaurant - Login");
        showLogin();
        startingStage.show();
        
        //here I save the users data and orders when the application is closed
        //event handler used.
        startingStage.setOnCloseRequest(event -> {
            userManager.saveUsersToFile();
            userManager.saveOrdersToFile();
        });
	}
	
	//method for displaying the login screen
	//making it the primary stage as well
	//made it this size because i felt login screens are genrally a bit smaller than the normal dashboard.
	//Figured its a simple intro to the program.
	public void showLogin() {
		startingStage.setTitle("Burrito King Restaurant - Login");
		Scene scene = new Scene(new Login(this).createLogin(), 500, 500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the registration screen
	//making it the primary stage as well
	//made it the same size as the login
	//set the title as well.
	public void showRegistration() {
		startingStage.setTitle("Burrito King Restaurant - Registration");
        Scene scene = new Scene(new Registration(this).createRegistration(), 500, 500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the dashboard screen
	//making it the primary stage as well
	//the size is larger and fits everything needed well without it looking cramped.
	//didnt want to make it to big with lots of uncessary white space
	//Set the title as well.
	public  void showDashboard(User user) {
		startingStage.setTitle("Burrito King Restaurant - Dashboard");
		Scene scene = new Scene(dashboard.createDashboard(user), 800, 800);
		startingStage.setScene(scene);
	}
	
	
	//This method is for displaying the profile screen
	//the profile screen is accessed when users edit profile
	//title is set
	//takes the user to their profile section, not a random one.
	public  void showProfile(User user) {
		startingStage.setTitle("Burrito King Restaurant - Edit Profile");
        Scene scene = new Scene(new Profile(this).createProfile(user), 500, 500);
        startingStage.setScene(scene);
	}
	
	//This method is for displaying the order payment screen.
	//set the primary stage
	//the size of the screen i feel is apporpriate
	//title tell user where they are as well
	public  void showOrderOnDashBoard(User user) {
		startingStage.setTitle("Burrito King Restaurant - Place Order");
		Scene scene = new Scene(orderOnDashBoard.createOrderPlacement(user), 800, 800);
		startingStage.setScene(scene);
	}
	


	
	//Main method launches the program
	public static void main(String[] args) {
		launch(args);
	}
	
	//getter for the dashboard instance
	//returns the instance for use in the program
	public Dashboard getDashboard() {
        return dashboard;
    }
	
	//getter for the orderondashboard instance
	//Returns the instance for use in the program.
	public OrderOnDashBoard getOrderOnDashBoard() {
        return orderOnDashBoard;
    }

}


