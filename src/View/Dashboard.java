package View;
import Model.FoodItem;
import Model.Meal;
import Model.Order;
import Model.User;
import View.OrderOnDashBoard;
import Controller.UserManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controller.UserManager;
import Interface.KingItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.Alerts;

// this class is for the dashboard that is displayed after login. 
//it is the biggest class and is responsible for a multiple things
//it creates the view for the dashboard so users have a place to intereact and order from the program
//It serves as the main interface
public class Dashboard {
	
	//Here i have the main application referenced which is used to navigate between different views
	//The tempOrder object holds the items before the order is place
	//needed for the basket thats required in the assignment.
	//The observable list is used to track the items in the basket and is linked to list view which displays the basket items
	//I also have a label for collecting credits if the user is a vip
	private BurritoKingApp app;
    private Order tempOrder = new Order();
    private ObservableList<KingItem> basketItems = FXCollections.observableArrayList();
    private Label collectingCredits;

    //This constructor is used to take the main application instance as a parameter
    public Dashboard(BurritoKingApp app) {
        this.app = app;
    }
    
    
	//getter method for the tempOrder
    //returns the current tempOrder
	public Order getTempOrder() {
        return tempOrder;
    }

	//clears the temp order and basket items
	//resets the tempOrder object and clears basketItems list.
	 public void clearTempOrder() {
	    System.out.println("Clearing tempOrder: Order ID: " + tempOrder.getOrderID() + " Items: " + tempOrder.getItems().size());// get rid of when done
	    tempOrder = new Order();
	    basketItems.clear();
	    }
    
	//Here ive created a dashboard layout
	 //The layout is defined using GirdPane and has multiple child nodes
	public GridPane createDashboard(User user) {
		GridPane pane = new GridPane(); 
		
		//I here set the alignment of the grid.
		//i set the vertical and horizontal gaps
		//Also set the padding 
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		
		//Here i display the users first and last name next to the title of the program
		//it is set at the top of the page.
		Label displayName = new Label("Burrito King, Welcome: " + user.getFirstName() + " " + user.getLastName());
        pane.add(displayName, 1, 0);
        
        //If the user is a VIP then their credits will be displayed here.
        //I figured it was the best way to do this.
        collectingCredits = new Label();
        if (user.isVIP()) {
        	updateCollectingCredits(user); 
            HBox creditsBox = new HBox(collectingCredits);
            creditsBox.setAlignment(Pos.CENTER);
            pane.add(creditsBox, 1, 1);
        }
        
        //Here is the button for viewing orders 
        Button viewOrdersBtn = new Button("View your Orders");
        viewOrdersBtn.setOnAction(e -> {
        	System.out.println("Attempting to view orders for user: " + user.getUsername() + ", User hash: " + user.hashCode()); //get rid of
        	System.out.println("Orders count for user: " + user.getOrders().size()); // get rid of
        	
        	//here the list is being displayed as an observable array
        	//allowing users to see their info from previous orders.
			ListView<String> orderListView = new ListView<>();
			ObservableList<String> orders = FXCollections.observableArrayList();
			orders.clear();
			
			//Here i have it so the user is informed if there are no orders to display.
			//I also have it so each order is added to the order list.
			if (user.getOrders().isEmpty()) {
			    System.out.println("No orders found for user: " + user.getUsername());
			orders.add("No orders available.");
			} else {
			    for (Order order : user.getOrders()) {
			        System.out.println("Adding order: " + order); //get rid of
			        orders.add(order.toString());
			    }
			}
			orderListView.setItems(orders);
			Stage orderStage = new Stage();
			VBox orderBox = new VBox(orderListView);
			orderBox.setPadding(new Insets(10));
			Scene orderScene = new Scene(orderBox, 300, 400);
			orderStage.setScene(orderScene);
			orderStage.setTitle("Your Orders");
			orderStage.show();

        });
	    
        //here is just the button for upgrading to VIP.
        //when clicked it will either show the user an area to enter their email, or
        //it will display an alert saying they are already a VIP
	    Button upgradeToVIPBtn = new Button("Upgrade to VIP");
	    upgradeToVIPBtn.setOnAction(e -> handleUpgradeToVIP(user));
        pane.add(upgradeToVIPBtn,  2,1);

	    //this is the button for edit profile
        //when clicked it takes user to the edit profile screen
  		Button editProfileBtn = new Button("Edit Profile");
  		editProfileBtn.setOnAction(e -> app.showProfile(user));
        pane.add(editProfileBtn, 3, 1);
        
        //This Hbox is to hold the "top buttons" which are display name, view orders, upgrade to VIP and edit profile.
        HBox topButtons = new HBox(10);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.getChildren().addAll(displayName, viewOrdersBtn, upgradeToVIPBtn, editProfileBtn);
        pane.add(topButtons, 0, 0, 2, 1);
        
        
        
        //This Hbox is for the area where users choose what food items they want
		HBox userFoodChoice = usersChoiceOfFood(user);
		pane.add(userFoodChoice,  0,2, 2, 1);
		
		//this label is for the shopping basket
		Label basketLabel = new Label("Shopping Basket:");
		pane.add(basketLabel, 0, 3, 2, 1);
		
		//Here is the listview for the basket
		//it allows for the shopping basket to be viewed
	    ListView<KingItem> basketListView = new ListView<>(basketItems);
        basketListView.setCellFactory(param -> new javafx.scene.control.ListCell<KingItem>() {
            @Override
            protected void updateItem(KingItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + ": $" + item.getPrice());
                }
            }
        });
        basketListView.setPrefHeight(150);
        pane.add(basketListView, 0, 4, 2, 1);
        
        //Here is the Hbox for the buttons which can manage the basket
        //which are remove item and update quantity.
        HBox basketControls = createBasketControls(basketListView);
	    pane.add(basketControls, 0,5,2,1);
	    
	    //Here is the button for the pay for order
	    //if there are items in the basket it will take the user to payment section
	    //otherwise it will display an alert to the user so they add items.
	    Button payForOrderBtn = new Button("Pay for Order");
        payForOrderBtn.setOnAction(e -> {
	        if (!tempOrder.getItems().isEmpty()) {
	        	confirmationMessage(user);///
	            System.out.println("Placing new order for user: " + user.getUsername());// get rid of
	        } else {
	        	Alerts.errorMessage("Add Items", "Add items to your basket before paying.");
	        }
	    });
        HBox payForOrderBox = new HBox();
        payForOrderBox.setAlignment(Pos.CENTER);
        payForOrderBox.getChildren().add(payForOrderBtn);
        pane.add(payForOrderBox, 0, 6, 2, 1);

		//This Vbox is for the ordermanagment section of the dashboard
		VBox actionButtonArea = orderManagementSection(user);
	    pane.add(actionButtonArea, 0, 7, 2, 1);
	    return pane;
	}
	
	//This method updates the VIP credits for the user
	//It retrieves the credits a user has an displays them on the label
	private void updateCollectingCredits(User user) {
		collectingCredits.setText("VIP Credits: " + user.getCredits());
	}
	
	//This method handles upgrading to VIP
	//as previously mentioned if the user is already a VIP then they will get a alert
	//Otherwise they get itll take them to get vaild email
	private void handleUpgradeToVIP(User user) {
        if (user.isVIP()) {
        	Alerts.infoMessage("VIP Status", "You are already a VIP!");
        } else {
            emailForVIP(user);
        }
    }
	
	
	//This method validates the email using a regular expression pattern
	//To make it simple the email has to have an @ symbol 
	//and can only contain valid inputs
	//pattern and matcher used to ensure the emailCOnfirm is correct.
	private boolean isValidEmail(String email) {
	    String emailConfirm = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    Pattern pattern = Pattern.compile(emailConfirm);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}

	//This method is for the user to enter their email
	//It tells the user if they want to sign up for promos 
	//also tells users how to enter a valid email
	//The method sets up a new window for this.
	private void emailForVIP(User user) {
        Stage stage = new Stage();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        // Heading for email input
        Label emailHeading = new Label("Would you like to receive promotion information via email?\nPlease add letters and/or numbers before an after an @ to\n"
        		+ "make it a valid email! ");
        pane.add(emailHeading, 0, 0, 2, 1);

        // Text field for email input
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        pane.add(emailField, 0, 1, 2, 1);

        // Confirm button
        //when clicked the email will be checked and if its valid user will get a pop up displaying this
        //if there is an issue then the user will be told this
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
            String email = emailField.getText();
            if (isValidEmail(email)) {
                UserManager userManager = UserManager.getInstance();
                if (userManager.upgradeToVIP(user.getUsername(), email)) {
                    Alerts.infoMessage("VIP Upgrade", "You have successfully upgraded to VIP! Please log out and then back in to access exclusive features.");
                    stage.close();
                } else {
                    Alerts.errorMessage("Error", "Failed to upgrade to VIP. Please enter a valid email.");
                }
            } else {
                Alerts.errorMessage("Invalid Email", "Please enter a valid email address.");
            }
        });
        pane.add(confirmBtn, 0, 2, 2, 1);

        //sets up the scene for the email input
        Scene scene = new Scene(pane, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Upgrade to VIP");
        stage.show();
    }
	
	
	//This method hanldes the collection of an order
	//it validates the input fields for orderID and collection time
	//and it checks if the collection time is valid.
	//It updates the status to collected if the conditions are met
	private void handleCollectOrder(User user, TextField collectOrderIDField, TextField collectTimeField) {
	    String orderIDStr = collectOrderIDField.getText();
	    String collectTimeStr = collectTimeField.getText();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    
	    //Here ive added an if and an alert to check if  the textfeilds are empty.
	    //If they are the alert will appear.
	    if (orderIDStr.isEmpty() || collectTimeStr.isEmpty()) {
	        Alerts.errorMessage("Collect Order Error", "Order ID and collection time must be filled out.");
	        return;
	    }
	    try {
	        int orderID = Integer.parseInt(orderIDStr);
	        String[] timeSplit = collectTimeStr.split(":");
	        if (timeSplit.length != 2) {
	            throw new DateTimeParseException("Invalid time format", collectTimeStr, 0);
	        }

	        int hour = Integer.parseInt(timeSplit[0]);
	        int minute = Integer.parseInt(timeSplit[1]);
	        LocalDateTime collectTime = LocalDateTime.now().withHour(hour).withMinute(minute);

	        //Here i have made it so the order is retrieved by the orderID
	        Order orderToCollect = user.getOrderById(orderID);
	        if (orderToCollect != null) {
	            UserManager userManager = UserManager.getInstance();
	            LocalDateTime minCollectTime = orderToCollect.getOrderPlacedTime().plusMinutes(userManager.calculatePreparationTime(orderToCollect));
	            
	            //Here the program check if the collection time the user has inputed it possible
	            //also ensures the order hasnt been cancelled
	            if (collectTime.isAfter(minCollectTime) && !orderToCollect.getStatus().equals("cancelled")) {
	                userManager.collectOrder(user.getUsername(), orderID, collectTime);
	                app.showDashboard(user);
	                
	                //if the order has been cancelled the following alert will pop up.
	            } else {
	            	Alerts.errorMessage("Collect Order Error", "Invalid collection time or order has been cancelled.");
	            }
	        } else {
	        	Alerts.errorMessage("Collect Order Error", "Order not found.");
	        }
	        //if user puts in an invalid input this alert will appear. 
	    } catch (NumberFormatException | DateTimeParseException ex) {
	    	Alerts.errorMessage("Collect Order Error", "Invalid input. Please check the Order ID and time format.");
	    }
	}
	
	//This method handles the cancelation of an order.
	//It will ensure the order can be canceled by checking it exists and hasnt been picked up yet.
	private void handleCancelOrder(User user, TextField cancelOrderIDField) {
	    String orderIDStr = cancelOrderIDField.getText();
	    
	    //Checks if order ID is empty, if so displays alert
	    if (orderIDStr.isEmpty()) {
	    	Alerts.errorMessage("Cancel Order Error", "Order ID must be filled out.");
	        return;
	    }

	    try {
	        int orderID = Integer.parseInt(orderIDStr);
	        Order orderToCancel = user.getOrderById(orderID);

	        //here the program checks if the order is placed and in the status of placed
	        //if so then the order can be canceled
	        //otherwise alerts will appear telling the user what the issue may be.
	        if (orderToCancel != null && orderToCancel.getStatus().equals("placed")) {
	            UserManager userManager = UserManager.getInstance();
	            userManager.cancelOrder(user.getUsername(), orderID);
	            Alerts.infoMessage("Cancel Order", "Order" + orderID + " has been successfully cancelled.");
	        } else {
	            Alerts.errorMessage("Cancel Order Error", "Order cannot be cancelled or does not exist.");
	        }
	    } catch (NumberFormatException ex) {
	    	Alerts.errorMessage("Cancel Order Error", "Invalid Order ID format. Please enter a valid number.");
	    }
	}
	
	//This message appears when the user go to pay for order
	//It allows the user to see the total price and allows them to confirm or go back
	//Was required to add as part of the assessment.
	private void confirmationMessage(User user) {
		
		//here the total price is calculated of the tempOrder
		//allowing for the user to know how much the order will be.
		//I made it an alert because it give the ability to confirm or go back.
        double totalPrice = tempOrder.calculateTotal();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText("Please confirm your order");
        alert.setContentText(String.format("Total Price: $%.2f\n", totalPrice));

        //Here are the buttons that appear
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType goBackButton = new ButtonType("Go back");
        alert.getButtonTypes().setAll(confirmButton, goBackButton);

        //if the user confirms they are shown the placement screen
        //if they go back then the alert disappears. 
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
        	app.showOrderOnDashBoard(user);
        } 
    }
	
	
	/////////////////////////Food selection area
	
	//This method is used to create the UI for selecting food.
	//Its a HBox layout and displays all items and gives users the ability to add the qty of each.
	private HBox usersChoiceOfFood(User user) {
		HBox hbox = new HBox(10);
	    hbox.setPadding(new Insets(20, 0, 20, 0));
	    hbox.setAlignment(Pos.CENTER);

	    //label and textfield for burritos
	    Label burritoLabel = new Label("Burritos - $7");
	    TextField burritoQty = new TextField("0");
	    burritoQty.setMaxWidth(50);

	    //label and textfield for fries
	    Label friesLabel = new Label("Fries - $4");
	    TextField friesQty = new TextField("0");
	    friesQty.setMaxWidth(50);

	    //label and textfield for soda
	    Label sodaLabel = new Label("Soda - $2.5");
	    TextField sodaQty = new TextField("0");
	    sodaQty.setMaxWidth(50);
	    hbox.getChildren().addAll(burritoLabel, burritoQty, friesLabel, friesQty, sodaLabel, sodaQty);
	    
	    //Here is the textfield and label for meals
	    //the difference for this is that it only appears if the user is a vip
	    TextField mealQty = null;
	    if (user.isVIP()) {
	    	Label mealLabel = new Label("Meal - $10.5");
	        mealQty = new TextField("0");
	        mealQty.setMaxWidth(50);
	        hbox.getChildren().addAll(mealLabel, mealQty);
	    }
	    TextField finalMealQty = mealQty;
	    
	    //Here is the button for users to add their items to the basket
	    //try catch surrounds
	    Button addToOrder = new Button("Add to Basket:");
	    addToOrder.setOnAction(e -> {
	    	try {
	    		//here i parse the quantites of each item
	    		//again meal only works if it fufils the criteria of being a VIP
	    		int burritoCount = Integer.parseInt(burritoQty.getText());
	    		int friesCount = Integer.parseInt(friesQty.getText());
	    		int sodaCount = Integer.parseInt(sodaQty.getText());
	    		int mealCount = user.isVIP() && finalMealQty != null ? Integer.parseInt(finalMealQty.getText()) : 0;

	    		//Here I made it so that there has to be at least one of the any item for something to be added to the basket
	    		//I've done this so there wouldnt be any unforseen errors.
	    		if (burritoCount < 1 && friesCount < 1 && sodaCount < 1 && mealCount < 1) {
	    			Alerts.errorMessage("Input Error", "Please enter at least one item.");
	    			return;
	        }
		        //Here the items get added to the tempOrder as they are going in the basket 
	    		//again i have it so if the user is a VIP then it can add a meal
		        addItemsToOrder(tempOrder, "Burrito", burritoQty.getText());
		        addItemsToOrder(tempOrder, "Fries", friesQty.getText());
		        addItemsToOrder(tempOrder, "Soda", sodaQty.getText());
		        if (user.isVIP() && finalMealQty != null) {
		            addItemsToOrder(tempOrder, "Meal", finalMealQty.getText());
		        }
		        
		        //Here i made it so the basket list updates
		        basketItems.clear();
		        basketItems.addAll(tempOrder.getItems());
		        System.out.println("Added to basket: Burritos: " + burritoQty.getText() + ", Fries: " + friesQty.getText() + ", Sodas: " + sodaQty.getText());// change to alert
		        if (user.isVIP() && finalMealQty != null) {
		            System.out.println("Meals: " + finalMealQty.getText());
		        }
		        System.out.println("Current tempOrder items: " + tempOrder.getItems().size());
		        
		        //if the user tries to add something other then a number then this alert will appear
	    	} catch (NumberFormatException ex) {
	    		Alerts.errorMessage("Input Error", "Please only enter numbers");
	    	}    
	    });

	    hbox.getChildren().add(addToOrder);
	    return hbox;
	}
	
	
	//This method is for handling adding items to the order
	//adds the item and how many to the order
	//it handles any errors that may appear
	private static void addItemsToOrder(Order order, String itemName, String quantityStr) {
	    try {
	        int quantity = Integer.parseInt(quantityStr);
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
	                case "Meal":
	                    order.addItem(new Meal(7, 4, 2.5), 1);
	                    break;
	            }
	        }
	        
	        System.out.println("Added " + quantity + " " + itemName + "(s) to order ID: " + order.getOrderID());//change to alert
	    } catch (NumberFormatException e) {
	    	Alerts.errorMessage("Input Error", "Please enter a valid number for the quantity.");
	    }
	}
	
	//This method creates the controls for managing the shopping basket
	//It holds the code for removing the selected items and updating quantity.
	private HBox createBasketControls(ListView<KingItem> basketListView) {
	    HBox hbox = new HBox(10);
	    hbox.setAlignment(Pos.CENTER);
	    hbox.setPadding(new Insets(10, 0, 10, 0));

	    //button to remove the item selected from the basket
	    Button removeItemBtn = new Button("Remove Selected Item");
	    removeItemBtn.setOnAction(e -> {
	    	
	    	//Here the program gets the selected item form the listview
	        KingItem selectedItem = basketListView.getSelectionModel().getSelectedItem();
	        if (selectedItem != null) {
	        	//removes the item from the basket/tempOrder
	            tempOrder.getItems().remove(selectedItem);
	            basketItems.remove(selectedItem);
	            Alerts.infoMessage("Item Removed", "Item(s) has been removed.");
	        }
	    });
	    //Here the button for Update quantity is created
	    Button updateQuantityBtn = new Button("Update Quantity");
	    updateQuantityBtn.setOnAction(e -> {
	    	//Here the program selects the item form the listview 
	    	KingItem selectedItem = basketListView.getSelectionModel().getSelectedItem();
	            
            //here i created an area to add the new quantity and update the temp order
	    	//textfield is created to the user can enter how many items they want
            if (selectedItem != null) {
                Stage quantityStage = new Stage();
                VBox quantityBox = new VBox(10);
                quantityBox.setAlignment(Pos.CENTER);
                quantityBox.setPadding(new Insets(10));
                TextField quantityField = new TextField();
                quantityField.setMaxWidth(50);
                
                //button to confirm the change.
                Button confirmBtn = new Button("Confirm");
                confirmBtn.setOnAction(ev -> {
                	
                	//parse the new quantity and updates the tempOrder/basket
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    tempOrder.getItems().remove(selectedItem);
                    for (int i = 0; i < newQuantity; i++) {
                        tempOrder.addItem(selectedItem, 1);
                    }
                    basketItems.clear();
                    basketItems.addAll(tempOrder.getItems());
                    quantityStage.close();
                    Alerts.infoMessage("Quantity Updated", "Quantity updated.");
                });
                
                //The textfield and confirm button is added to the VBox
                //creates a new window for the user to enter this info
                quantityBox.getChildren().addAll(new Label("Enter new quantity:"), quantityField, confirmBtn);
                Scene quantityScene = new Scene(quantityBox, 300, 200);
                quantityStage.setScene(quantityScene);
                quantityStage.setTitle("Update Quantity");
                quantityStage.show();
            }
        });

        hbox.getChildren().addAll(removeItemBtn, updateQuantityBtn);
        return hbox;
    }

	//This method handles the the collect/cancel, the export order  and the logout.
	//lays them out in a VBox
	private VBox orderManagementSection(User user) {
		VBox vbox = new VBox(10);
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setPadding(new Insets(10, 10, 10, 10));

	    //Here is the box for collecting orders
	    HBox collectOrderBox = createCollectOrderBox(user);
	    vbox.getChildren().add(collectOrderBox);

	    //Here is the box for canceling orders
	    HBox cancelOrderBox = createCancelOrderBox(user);
	    vbox.getChildren().add(cancelOrderBox);

	    //Here is the button for exporting orders
	    Button exportOrdersBtn = createExportOrdersButton(user);
	    vbox.getChildren().add(exportOrdersBtn);

	    //Here is the logout button
	    Button logoutBtn = createLogoutButton();
	    vbox.getChildren().add(logoutBtn);
	    return vbox;
	}
	
	//This method is to set up the interface of collecting an order
	private HBox createCollectOrderBox(User user) {
		//here is the label and textfield for the order ID
	    HBox collectOrderBox = new HBox(10);
	    Label collectOrderLabel = new Label("Enter Order ID to Collect:");
	    TextField collectOrderIDField = new TextField();
	    collectOrderIDField.setMaxWidth(100);
	    
	    //here is the label and textField for the collection time
	    Label collectTimeLabel = new Label("Collection Time (HH:mm) 24hr time");
	    TextField collectTimeField = new TextField();
	    collectTimeField.setMaxWidth(100);
	    
	    //this is the button used to collect the order
	    Button collectOrderBtn = new Button("Collect Order");
	    collectOrderBtn.setOnAction(e -> handleCollectOrder(user, collectOrderIDField, collectTimeField));
	    collectOrderBox.getChildren().addAll(collectOrderLabel, collectOrderIDField, collectTimeLabel, collectTimeField, collectOrderBtn);
	    return collectOrderBox;
	}
	
	//This method sets up the UI for canceling anorder
	//I have a label and textfield which the user can enter the ID
	private HBox createCancelOrderBox(User user) {
		HBox cancelOrderBox = new HBox(10);
	    cancelOrderBox.setAlignment(Pos.CENTER_RIGHT);
	    Label cancelOrderLabel = new Label("Enter Order ID to cancel:");
	    TextField cancelOrderIDField = new TextField();
	    cancelOrderIDField.setMaxWidth(100);

	    //button that cancels order
	    //check if everything is valid in the handcancelorder method
	    Button cancelOrderBtn = new Button("Cancel Order");
	    cancelOrderBtn.setOnAction(e -> handleCancelOrder(user, cancelOrderIDField));
	    cancelOrderBox.getChildren().addAll(cancelOrderLabel, cancelOrderIDField, cancelOrderBtn);
	    return cancelOrderBox;
	}
	
	
	
	//this method creates a button for exporting orders.
	private Button createExportOrdersButton(User user) {
	    Button exportOrdersBtn = new Button("Export Orders");
	    exportOrdersBtn.setOnAction(e -> handleExportOrders(user));
	    return exportOrdersBtn;
	}
	
	//this method handles the export of orders to a csv file
	//when the user clicks on the export button the user will be given a new window that
	//give users the ability to select what they want to export and where to.
	private void handleExportOrders(User user) {
		//Here a list view is creted that displays all orders made by the user
		//it displays them by the order ID.
		//The SelectionMode.MULTIPLE allows for the user to select multiple.
	    ListView<Order> ordersListView = new ListView<>(FXCollections.observableArrayList(user.getOrders()));
	    ordersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    
	    //Here the program sets a custom cell factory to display tje order ID and status
	    ordersListView.setCellFactory(param -> new ListCell<Order>() {
	        @Override
	        protected void updateItem(Order order, boolean empty) {
	            super.updateItem(order, empty);
	            if (empty || order == null) {
	                setText(null);
	            } else {
	                setText("Order ID: " + order.getOrderID() + " - " + order.getStatus());
	            }
	        }
	    });

	    //Here the checkboxes are created
	    //this allows for users to choose what fields they want to export.
	    CheckBox exportOrderId = new CheckBox("Order ID");
	    exportOrderId.setSelected(true);
	    CheckBox exportStatus = new CheckBox("Status");
	    exportStatus.setSelected(true);
	    CheckBox exportPlacedTime = new CheckBox("Placed Time");
	    exportPlacedTime.setSelected(true);
	    CheckBox exportCollectedTime = new CheckBox("Collected Time");
	    exportCollectedTime.setSelected(true);
	    CheckBox exportTotalPrice = new CheckBox("Total Price");
	    exportTotalPrice.setSelected(true);
	    CheckBox exportItems = new CheckBox("Items");
	    exportItems.setSelected(true);
	    
	    //Here the button to export the selected order an fields is created.
	    Button exportBtn = new Button("Export Selected Orders");
	    exportBtn.setOnAction(ev -> {
	    	//Here the program gets the selected order from the listview
	        List<Order> selectedOrders = ordersListView.getSelectionModel().getSelectedItems();
	        
	        //It then sets up a filechooser to select the export file location
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Save Orders");
	        
	        //Here the program ensures its a CSV file it gets exported to. 
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	        File file = fileChooser.showSaveDialog(new Stage());
	        
	        //then at the end it exports the order/s.
	        if (file != null) {
	            UserManager userManager = UserManager.getInstance();
	            userManager.exportOrders(user.getUsername(), selectedOrders, file.getAbsolutePath(),
	            exportOrderId.isSelected(), exportStatus.isSelected(), exportPlacedTime.isSelected(),
	            exportCollectedTime.isSelected(), exportTotalPrice.isSelected(), exportItems.isSelected());
	        }
	    });
	    
	    //here is the Vbox setup for the export function.
	    VBox exportBox = new VBox(10, ordersListView, exportOrderId, exportStatus, exportPlacedTime,
	            exportCollectedTime, exportTotalPrice, exportItems, exportBtn);
	    exportBox.setPadding(new Insets(10));
	    Scene exportScene = new Scene(exportBox, 300, 400);
	    Stage exportStage = new Stage();
	    exportStage.setTitle("Export Orders");
	    exportStage.setScene(exportScene);
	    exportStage.show();
	}
	
	//This method is for the logout button.
	//When logout is clicked it takes the user back to login screen. 
	private Button createLogoutButton() {
	    Button logoutBtn = new Button("Log Out");
	    logoutBtn.setOnAction(e -> {
	        UserManager userManager = UserManager.getInstance();
	        userManager.logoutUser();
	        app.showLogin();
	    });
	    return logoutBtn;
	}
	
	
	 
	private VBox createOrdersDisplay(User user) { /// I dont need this method
		VBox vbox = new VBox(10);
	    vbox.setPadding(new Insets(20, 0, 20, 0));
	    vbox.setAlignment(Pos.CENTER);

	    Label ordersLabel = new Label("Your Orders:");
	    ListView<Order> ordersListView = new ListView<>();
	    ObservableList<Order> ordersList = FXCollections.observableArrayList(user.getOrders());
	    
	    //I dont think this is needed now that i fixed the date issue 
	    LocalDateTime fallbackDate = LocalDateTime.now();
	    for (Order order : ordersList) {
	        if (order.getOrderPlacedTime() == null) {
	            order.setOrderPlacedTime(fallbackDate);//////////////////////////////////////////////////////////////////Remove maybe???
	        }
	    }

	    ordersList.sort(Comparator.comparing(Order::getOrderPlacedTime, Comparator.nullsLast(Comparator.reverseOrder())));
	    
	    //sets the orders sorted in the Listview
	    ordersListView.setItems(ordersList);
	    ordersListView.setCellFactory(param -> new ListCell<Order>() {
	        @Override
	        protected void updateItem(Order order, boolean empty) {
	            super.updateItem(order, empty);
	            if (empty || order == null) {
	                setText(null);
	            } else {
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");/////////////////////// need to change the format now!
	                String formattedPlacedTime = order.getOrderPlacedTime() != null ? order.getOrderPlacedTime().format(formatter) : "N/A";
	                String formattedCollectedTime = order.getOrderCollectedTime() != null ? order.getOrderCollectedTime().format(formatter) : "N/A";
	                setText(String.format("Order ID: %d\nStatus: %s\nPlaced Time: %s\nCollected Time: %s\nTotal Price: $%.2f\nItems: %d", 
	                        order.getOrderID(), order.getStatus(), formattedPlacedTime, formattedCollectedTime, order.calculateTotal(), order.getItems().size()));
	            }
	        }
	    });
	    vbox.getChildren().addAll(ordersLabel, ordersListView);

	    return vbox;
    }
		////////////////////////////// need to check if this even works???
	
	private void addMealsToOrder(Order order, String quantityStr) {// check if this even works...
	    try {
	        int quantity = Integer.parseInt(quantityStr);
	        for (int i = 0; i < quantity; i++) {
	            order.addItem(new Meal(7, 4, 2.5), 1);
	        }
	        System.out.println("Added " + quantity + " meal(s) to order ID: " + order.getOrderID());
	    } catch (NumberFormatException e) {
	    	Alerts.errorMessage("Input Error", "Please enter a valid number for the quantity.");
	    }
	}

}

