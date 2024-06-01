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
public class BurritoKingApp extends Application{//honestly dont like the name of the class, might change. Maybe just BurritoKing?
	
	//static variable to hold the primary stage application
	//when i tried to use primary stage instead of a variable to hold it
	//program ran into a multatude of errors
	private static Stage startingStage;
	
	//start method is the entry point of the javafx
	@Override
	public void start (Stage primaryStage) {
//		UserManager.loadUsersFromFile();//skeleton loss
		UserManager userManager = UserManager.getInstance();//skelton
        userManager.loadUsersFromFile();//skelton
		
		//as mentioned earlier setting primaryStage to startingStage
		//here ive set the title, i display the login form and making it visable.
		startingStage = primaryStage;
		startingStage.setTitle("Burrito King Restaurant - Login");
		showLogin();
		startingStage.show();
		
		startingStage.setOnCloseRequest((WindowEvent event) -> {
	        userManager.saveUsersToFile();// saves vip user is a vip
	        userManager.saveOrdersToFile();
	    });
	}
	
	//method for displaying the login screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showLogin() {
		startingStage.setTitle("Burrito King Restaurant - Login");
		Scene scene = new Scene(Login.createLogin(), 500, 500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the registration screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showRegistration() {
		startingStage.setTitle("Burrito King Restaurant - Registration");
		Scene scene = new Scene(Registration.createRegistration(),500,500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the dashboard screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showDashboard(User user) {
		startingStage.setTitle("Burrito King Restaurant - Dashboard");
		Scene scene = new Scene(Dashboard.createDashboard(user), 800, 800);
		startingStage.setScene(scene);
	}
	
	
	//displays the profile. 
	public static void showProfile(User user) {
		startingStage.setTitle("Burrito King Restaurant - Edit Profile");
		Scene scene = new Scene(Profile.createProfile(user), 500, 500);
		startingStage.setScene(scene);
	}
	
	//displays the order on the dashboard
	public static void showOrderOnDashBoard(User user) {
		startingStage.setTitle("Burrito King Restaurant - Place Order");
		Scene scene = new Scene(OrderOnDashBoard.createOrderPlacement(user), 800, 800);//might change the create name as maybe changing class name?
		startingStage.setScene(scene);
	}
	


	
	//launches the program
	public static void main(String[] args) {
		launch(args);
	}

}


