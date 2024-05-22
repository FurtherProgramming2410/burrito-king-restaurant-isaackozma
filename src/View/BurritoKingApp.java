package View;
import Model.User;
import View.OrderOnDashBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;


//starting point of the program
public class BurritoKingApp extends Application{
	
	//static variable to hold the primary stage application
	//when i tried to use primary stage instead of a variable to hold it
	//program ran into a multatude of errors
	private static Stage startingStage;
	
	//start method is the entry point of the javafx
	@Override
	public void start (Stage primaryStage) {
		
		//as mentioned earlier setting primaryStage to startingStage
		//here ive set the title, i display the login form and making it visable.
		startingStage = primaryStage;
		startingStage.setTitle("Burrito King Restaurant");
		showLogin();
		startingStage.show();
	}
	
	//method for displaying the login screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showLogin() {
		Scene scene = new Scene(Login.createLogin(), 500, 500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the registration screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showRegistration() {
		Scene scene = new Scene(Registration.createRegistration(),500,500);
		startingStage.setScene(scene);
	}
	
	//method for displaying the registration screen
	//making it the primary stage as well
	//made it just a default size, may change in future 
	public static void showDashboard(User user) {
		Scene scene = new Scene(Dashboard.createDashboard(user), 800, 800);
		startingStage.setScene(scene);
	}
	
	
	//displays the profile. 
	public static void showProfile(User user) {
		Scene scene = new Scene(Profile.createProfile(user), 500, 500);
		startingStage.setScene(scene);
	}
	
	//displays the order on the dashboard
	public static void showOrderOnDashBoard(User user) {
		Scene scene = new Scene(OrderOnDashBoard.createOrderPlacement(user), 800, 800);
		startingStage.setScene(scene);
	}
	


	
	
	public static void main(String[] args) {
		launch(args);
	}

}
