package View;
import Model.FoodItem;
import Model.Meal;
import Model.Order;
import Model.User;
import Controller.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this class is for the dashboard that is displayed after login. 
//it is incomplete at this stage.
public class Dashboard {
	
	private static Order tempOrder = new Order();
	
	public static Order getTempOrder() {
        return tempOrder;
    }

    public static void clearTempOrder() {
        tempOrder = new Order();
    }
	
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
		
		VBox userFoodChoice = createUserFoodChoice();
		pane.add(userFoodChoice,  0,1, 2, 1);
		
		VBox actionButtonArea = createActionButtons(user);
	    pane.add(actionButtonArea, 0, 2, 2, 1); 
	    
	    //displays the list of active orders
	    ListView<String> activeOrdersList = new ListView<>();
	    ObservableList<String> orders = FXCollections.observableArrayList();
	    for (Order order : user.getOrders()) {
	    	orders.add(order.toString());
	    }
	    
	    activeOrdersList.getItems().addAll("Previous orders will go here");
	    pane.add(activeOrdersList, 0, 3, 2, 1);

		
		
		return pane;
		
	}
	
	//method for the food choice.
	private static VBox createUserFoodChoice() {
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
		
		
		// ability to add items to the orders
		Button addToOrder = new Button("Add to order:");
		addToOrder.setOnAction(e -> {
			 tempOrder = new Order(); 
	            addItemsToOrder(tempOrder, "Burrito", Integer.parseInt(burritoQty.getText()));
	            addItemsToOrder(tempOrder, "Fries", Integer.parseInt(friesQty.getText()));
	            addItemsToOrder(tempOrder, "Soda", Integer.parseInt(sodaQty.getText()));
	            addMealsToOrder(tempOrder, Integer.parseInt(mealQty.getText()));
	            System.out.println("Added to basket: Burritos: " + burritoQty.getText() + ", Fries: " + friesQty.getText() + ", Sodas: " + sodaQty.getText() + ", Meals: " + mealQty.getText());
	        });
		
		//adds labels and text feilds.
		vbox.getChildren().addAll(burritoLabel, burritoQty, friesLabel, friesQty, sodaLabel, sodaQty, mealLabel, mealQty, addToOrder);
		return vbox;
		
	}
	
	//this method is to add items to the order. 
	////////////////////////////
	private static void addItemsToOrder(Order order, String itemName, int quantity) {
        for (int i = 0; i < quantity; i++) {
            switch (itemName) {
                case "Burrito":
                    order.addItem(new FoodItem("Burrito", 7, 1), 1);
                    break;
                case "Fries":
                    order.addItem(new FoodItem("Fries", 4, 1), 1);
                    break;
                case "Soda":
                    order.addItem(new FoodItem("Soda", 2.5, 1), 1);
                    break;
            }
        }
    }
	
	private static void addMealsToOrder(Order order, int quantity) {
        for (int i = 0; i < quantity; i++) {
            order.addItem(new Meal(7, 4, 2.5), 1);
        }
    }

	// this method creates an action button to view orders
	 private static VBox createActionButtons(User user) {
		 VBox vbox = new VBox(10);
		 vbox.setAlignment(Pos.CENTER);
		 vbox.setPadding(new Insets(10, 0, 10, 0));
		Button viewOrdersBtn = new Button("View your Orders");
		
		viewOrdersBtn.setOnAction(e -> {
			ListView<String> orderListView = new ListView<>();
			ObservableList<String> orders = FXCollections.observableArrayList();
			for (Order order : user.getOrders()) {
				orders.add(order.toString());
			}
			orderListView.setItems(orders);
			Stage orderStage = new Stage();
			VBox orderBox = new VBox(orderListView);
			orderBox.setPadding(new Insets(10));
			Scene orderScene = new Scene (orderBox, 300, 400);
			orderStage.setScene(orderScene);
			orderStage.setTitle("your orders");
			orderStage.show();
		});
		vbox.getChildren().add(viewOrdersBtn);
			
			
		
			
		//this method is to make a new order. 
		Button newOrderBtn = new Button("Place new order");
		newOrderBtn.setOnAction(e -> {
            if (!tempOrder.getItems().isEmpty()) {
                user.addOrder(tempOrder);
                tempOrder = new Order();
                BurritoKingApp.showOrderOnDashBoard(user);
            } else {
                System.out.println("Add items to your basket before placing an order.");
            }
        });
        vbox.getChildren().add(newOrderBtn);
		
		Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setOnAction(e -> BurritoKingApp.showProfile(user));
        vbox.getChildren().add(editProfileBtn);
			
		
		return vbox;
		 
	 }

}
