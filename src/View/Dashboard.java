package View;
import Model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// this class is for the dashboard that is displayed after login. 
//it is incomplete at this stage.
public class Dashboard {
	
	
	//Here I've made the dahsboard as a GridPane
	//I've got the layout below as well
	//its not finished but its a basic layout that can be easily changed in the future
	public static GridPane createDashboard(User user) {
		GridPane pane = new GridPane();
		//I here set the alignment of the grid.
		//i set the vertical and horizontal gaps
		//Also set the padding 
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		
		//here I have the program display the the users first name and last name
		//have not added the the active order as of yet.
		//not sure if i will use VBox or HBox at this moment
		Label displayName = new Label(user.getFirstName() +" " + user.getLastName());
		pane.add(displayName, 0, 0, 2, 1);
		
		VBox userFoodChoice = createuserFoodChoice();
		pane.add(userFoodChoice,  0,1, 2, 1);
		
		VBox actionButtonArea = createActionButtons();
	    pane.add(actionButtonArea, 0, 2, 2, 1); 

		
		
		return pane;
		
	}
	
	private static VBox createuserFoodChoice() {
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets (20, 0, 20, 0));
		vbox.setAlignment(Pos.CENTER);
		
		//here i created a label for the burritos 
		//i also created an area for the user to enter how many they would like
		Label burritoLabel = new Label("Burritos - $7");
		TextField burritoQty = new TextField("0");
		burritoQty.setMaxWidth(50);
		
		//here i created a label for the fries 
		//i also created an area for the user to enter how many they would like
		Label friesLabel = new Label("Fries - $4");
		TextField friesQty = new TextField("0");
		friesQty.setMaxWidth(50);
		
		//here i created a label for the Soda 
		//i also created an area for the user to enter how many they would like
		Label sodaLabel = new Label("Soda - $2.5");
		TextField sodaQty = new TextField("0");
		sodaQty.setMaxWidth(50);
		
		Label mealLabel = new Label("Meal - $15");
		TextField mealQty = new TextField("0");
		mealQty.setMaxWidth(50);
		
		Button addToOrder = new Button("Add to order:");
		addToOrder.setOnAction(e -> {
			System.out.println("Added to basket: Burritos: " + burritoQty.getText() + ", Fries: " + friesQty.getText() + ", Sodas: " + sodaQty.getText() + ", Meals: " + mealQty.getText());
		});
		
		vbox.getChildren().addAll(burritoLabel, burritoQty, friesLabel, friesQty, sodaLabel, sodaQty, mealLabel, mealQty, addToOrder);
		return vbox;
		
	}
	
	 private static VBox createActionButtons() {
		 VBox vbox = new VBox(10);
		 vbox.setAlignment(Pos.CENTER);
		 vbox.setPadding(new Insets(10, 0, 10, 0));
		 
		//here I have not finished what these buttons will do on the action, ive only
		//got it to print a message. 
		Button viewOrdersBtn = new Button("View your Orders");
		viewOrdersBtn.setOnAction(e -> System.out.println("View Orders"));
		vbox.getChildren().add(viewOrdersBtn);
			
			
		
			
		//same goes for this button, nothing is complete yet.
		Button newOrderBtn = new Button("Place new order");
		newOrderBtn.setOnAction(e -> System.out.println("New Order"));
		vbox.getChildren().add(newOrderBtn);
			
		
		return vbox;
		 
	 }

}
