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

public class Dashboard {
	
	private static Order tempOrder = new Order();
    private static ObservableList<KingItem> basketItems = FXCollections.observableArrayList();

	//gets the temp order
	public static Order getTempOrder() {
        return tempOrder;
    }

	//clears the temp order
	 public static void clearTempOrder() {
	        System.out.println("Clearing tempOrder: Order ID: " + tempOrder.getOrderID() + " Items: " + tempOrder.getItems().size());
	        tempOrder = new Order();
	        basketItems.clear();
	    }
    
	 
	 private static Label collectingCredits;
    
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
		Label displayName = new Label("Burrito King, Welcome: " + user.getFirstName() + " " + user.getLastName());
        pane.add(displayName, 1, 0);
        
        collectingCredits = new Label();
        if (user.isVIP()) {
        	updateCollectingCredits(user); 
            HBox creditsBox = new HBox(collectingCredits);
            creditsBox.setAlignment(Pos.CENTER);
            pane.add(creditsBox, 1, 1);
        }
        
        Button viewOrdersBtn = new Button("View your Orders");
        viewOrdersBtn.setOnAction(e -> {
            System.out.println("Attempting to view orders for user: " + user.getUsername() + ", User hash: " + user.hashCode());
            System.out.println("Orders count for user: " + user.getOrders().size());

            ListView<String> orderListView = new ListView<>();
            ObservableList<String> orders = FXCollections.observableArrayList();
            if (user.getOrders().isEmpty()) {
                System.out.println("No orders found for user: " + user.getUsername());
                orders.add("No orders available.");
            } else {
                for (Order order : user.getOrders()) {
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
	    
	    Button upgradeToVIPBtn = new Button("Upgrade to VIP");
//        upgradeToVIPBtn.setOnAction(e -> emailForVIP(user));
	    upgradeToVIPBtn.setOnAction(e -> handleUpgradeToVIP(user));
        


        pane.add(upgradeToVIPBtn,  2,1);

	    
        
        
      //button for editing profile    
  		Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setOnAction(e -> BurritoKingApp.showProfile(user));//maybe change showProfile to edit profile? might make it more confusing tho>?
        pane.add(editProfileBtn, 3, 1);
        
        HBox topButtons = new HBox(10);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.getChildren().addAll(displayName, viewOrdersBtn, upgradeToVIPBtn, editProfileBtn);
        pane.add(topButtons, 0, 0, 2, 1);
        
        
        
        
		HBox userFoodChoice = usersChoiceOfFood(user);
		pane.add(userFoodChoice,  0,2, 2, 1);
		
		Label basketLabel = new Label("Shopping Basket:");
		pane.add(basketLabel, 0, 3, 2, 1);
		
		//displays the list of active orders
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
        
        HBox basketControls = createBasketControls(basketListView);
	    pane.add(basketControls, 0,5,2,1);
	    
	    Button payForOrderBtn = new Button("Pay for Order");
        payForOrderBtn.setOnAction(e -> {
	        if (!tempOrder.getItems().isEmpty()) {
	        	confirmationMessage(user);///
	            System.out.println("Placing new order for user: " + user.getUsername());
//	            BurritoKingApp.showOrderOnDashBoard(user);/// these two worked before i added my stuff to show user how much they are paying
	        } else {
	        	Alerts.errorMessage("Add Items", "Add items to your basket before paying.");
//	            System.out.println("Add items to your basket before placing an order.");
	        }
	    });
        HBox payForOrderBox = new HBox();
        payForOrderBox.setAlignment(Pos.CENTER);
        payForOrderBox.getChildren().add(payForOrderBtn);

        pane.add(payForOrderBox, 0, 6, 2, 1);

	    
		
		VBox actionButtonArea = orderManagementSection(user);
	    pane.add(actionButtonArea, 0, 7, 2, 1); 
	    
	    
	    

	    return pane;
	    
	   
		
	}
	
	
	private static void updateCollectingCredits(User user) {
		collectingCredits.setText("VIP Credits: " + user.getCredits());
	}
	
	private static void handleUpgradeToVIP(User user) {
        if (user.isVIP()) {
        	Alerts.infoMessage("VIP Status", "You are already a VIP!");
        } else {
            emailForVIP(user);
        }
    }
	
	
	
	private static boolean isValidEmail(String email) {
	    String emailConfirm = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    Pattern pattern = Pattern.compile(emailConfirm);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}

	private static void emailForVIP(User user) {
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
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
            String email = emailField.getText();
            if (isValidEmail(email)) {
                UserManager userManager = UserManager.getInstance();
                if (userManager.upgradeToVIP(user.getUsername(), email)) {
                    Alerts.infoMessage("VIP Upgrade", "You have successfully upgraded to VIP! Please log out and then back in to access exclusive features.");
                    stage.close();
            
//            if (UserManager.upgradeToVIP(user.getUsername(), email)) {///// orginal before skeleton 
//            UserManager userManager = UserManager.getInstance();
//            if (userManager.upgradeToVIP(user.getUsername(), email)) {
//            	Alerts.infoMessage("VIP Upgrade", "You have successfully upgraded to VIP! Please log out and then back in to access exclusive features");
//                stage.close();
                } else {
                    Alerts.errorMessage("Error", "Failed to upgrade to VIP. Please enter a valid email.");
                }
            } else {
                Alerts.errorMessage("Invalid Email", "Please enter a valid email address.");
            }
        });
        pane.add(confirmBtn, 0, 2, 2, 1);

        Scene scene = new Scene(pane, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Upgrade to VIP");
        stage.show();
    }
	
	

	
	private static void placeNewOrder(User user) { /// have to check if this even works???
	    if (!tempOrder.getItems().isEmpty()) {
	        System.out.println("Placing new order for user: " + user.getUsername() + ", Order ID: " + tempOrder.getOrderID());
	        user.addOrder(tempOrder);
	        System.out.println("Order added to user: " + user.getUsername() + ", Order ID: " + tempOrder.getOrderID());
	        clearTempOrder();
	        BurritoKingApp.showDashboard(user);
	    } else {
	    	Alerts.errorMessage("Add Items", "Add items to your basket before placing an order.");
//	        System.out.println("Add items to your basket before placing an order.");
	    }
	}
	
	//this method is to add items to the order. 
	//adds to order based on name an quantity
	//handles if users enter invalid inputs
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

	private static void addMealsToOrder(Order order, String quantityStr) {// check if this even works...
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
	
	
	//method for the food choice.
	//creates the layot for the foodchoice
	 private static HBox usersChoiceOfFood(User user) {
		 HBox hbox = new HBox(10);
		    hbox.setPadding(new Insets(20, 0, 20, 0));
		    hbox.setAlignment(Pos.CENTER);

		    Label burritoLabel = new Label("Burritos - $7");
		    TextField burritoQty = new TextField("0");
		    burritoQty.setMaxWidth(50);

		    Label friesLabel = new Label("Fries - $4");
		    TextField friesQty = new TextField("0");
		    friesQty.setMaxWidth(50);

		    Label sodaLabel = new Label("Soda - $2.5");
		    TextField sodaQty = new TextField("0");
		    sodaQty.setMaxWidth(50);

		    hbox.getChildren().addAll(burritoLabel, burritoQty, friesLabel, friesQty, sodaLabel, sodaQty);
		    
		    TextField mealQty = null;
		    if (user.isVIP()) {
		    	Label mealLabel = new Label("Meal - $10");
		        mealQty = new TextField("0");
		        mealQty.setMaxWidth(50);
		        hbox.getChildren().addAll(mealLabel, mealQty);
		    }

		    //adding the items to the order
		    TextField finalMealQty = mealQty;
		    
		    
		    Button addToOrder = new Button("Add to Basket:");
		    addToOrder.setOnAction(e -> {
		    	try {
		    	int burritoCount = Integer.parseInt(burritoQty.getText());// these were added for the basket to have an alert... maybe wrong
		        int friesCount = Integer.parseInt(friesQty.getText());
		        int sodaCount = Integer.parseInt(sodaQty.getText());
		        int mealCount = user.isVIP() && finalMealQty != null ? Integer.parseInt(finalMealQty.getText()) : 0;

		        if (burritoCount < 1 && friesCount < 1 && sodaCount < 1 && mealCount < 1) {
		            Alerts.errorMessage("Input Error", "Please enter at least one item.");
		            return;
		        }
		        
		        addItemsToOrder(tempOrder, "Burrito", burritoQty.getText());/// usally this was the starting code!
		        addItemsToOrder(tempOrder, "Fries", friesQty.getText());
		        addItemsToOrder(tempOrder, "Soda", sodaQty.getText());
		        if (user.isVIP() && finalMealQty != null) {
		            addItemsToOrder(tempOrder, "Meal", finalMealQty.getText());
		        }
		        
		        basketItems.clear();
		        basketItems.addAll(tempOrder.getItems());
		        System.out.println("Added to basket: Burritos: " + burritoQty.getText() + ", Fries: " + friesQty.getText() + ", Sodas: " + sodaQty.getText());// change to alert
		        if (user.isVIP() && finalMealQty != null) {
		            System.out.println("Meals: " + finalMealQty.getText());
		        }
		        System.out.println("Current tempOrder items: " + tempOrder.getItems().size());
		    } catch (NumberFormatException ex) {
		    	Alerts.errorMessage("Input Error", "Please only enter numbers");
		    }
		        
		        
		    });

		    hbox.getChildren().add(addToOrder);
//		    hbox.getChildren().addAll(burritoLabel, burritoQty, friesLabel, friesQty, sodaLabel, sodaQty, mealLabel, mealQty, addToOrder);
		    return hbox;
		}
	
	 //displays that there has been an error at some stage.

	
	 
	 
	 
	////////////////////new version currently testing!
	
	private static VBox orderManagementSection(User user) {
	    VBox vbox = new VBox(10);
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setPadding(new Insets(10, 10, 10, 10));

	    // Collect Order Section
	    HBox collectOrderBox = createCollectOrderBox(user);
	    vbox.getChildren().add(collectOrderBox);

	    // Cancel Order Section
	    HBox cancelOrderBox = createCancelOrderBox(user);
	    vbox.getChildren().add(cancelOrderBox);

	    // Export Orders Section
	    Button exportOrdersBtn = createExportOrdersButton(user);
	    vbox.getChildren().add(exportOrdersBtn);

	    // Logout Button
	    Button logoutBtn = createLogoutButton();
	    vbox.getChildren().add(logoutBtn);

	    return vbox;
	}
	
	
	private static HBox createCollectOrderBox(User user) {
	    HBox collectOrderBox = new HBox(10);
	    Label collectOrderLabel = new Label("Enter Order ID to Collect:");
	    TextField collectOrderIDField = new TextField();
	    collectOrderIDField.setMaxWidth(100);

	    Label collectTimeLabel = new Label("Collection Time (HH:mm) 24hr time");
	    TextField collectTimeField = new TextField();
	    collectTimeField.setMaxWidth(100);

	    Button collectOrderBtn = new Button("Collect Order");
	    collectOrderBtn.setOnAction(e -> handleCollectOrder(user, collectOrderIDField, collectTimeField));
	    collectOrderBox.getChildren().addAll(collectOrderLabel, collectOrderIDField, collectTimeLabel, collectTimeField, collectOrderBtn);

	    return collectOrderBox;
	}
	
	private static void handleCollectOrder(User user, TextField collectOrderIDField, TextField collectTimeField) {
	    String orderIDStr = collectOrderIDField.getText();
	    String collectTimeStr = collectTimeField.getText();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

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

	        Order orderToCollect = user.getOrderById(orderID);
	        if (orderToCollect != null) {
	            UserManager userManager = UserManager.getInstance();
	            LocalDateTime minCollectTime = orderToCollect.getOrderPlacedTime().plusMinutes(userManager.calculatePreparationTime(orderToCollect));

	            if (collectTime.isAfter(minCollectTime) && !orderToCollect.getStatus().equals("cancelled")) {
	                userManager.collectOrder(user.getUsername(), orderID, collectTime);
	                BurritoKingApp.showDashboard(user);
	            } else {
	            	Alerts.errorMessage("Collect Order Error", "Invalid collection time or order has been cancelled.");
	            }
	        } else {
	        	Alerts.errorMessage("Collect Order Error", "Order not found.");
	        }
	    } catch (NumberFormatException | DateTimeParseException ex) {
	    	Alerts.errorMessage("Collect Order Error", "Invalid input. Please check the Order ID and time format.");
	    }
	}
	
	private static HBox createCancelOrderBox(User user) {
	    HBox cancelOrderBox = new HBox(10);
	    cancelOrderBox.setAlignment(Pos.CENTER_RIGHT);
	    Label cancelOrderLabel = new Label("Enter Order ID to cancel:");
	    TextField cancelOrderIDField = new TextField();
	    cancelOrderIDField.setMaxWidth(100);

	    Button cancelOrderBtn = new Button("Cancel Order");
	    cancelOrderBtn.setOnAction(e -> handleCancelOrder(user, cancelOrderIDField));
	    cancelOrderBox.getChildren().addAll(cancelOrderLabel, cancelOrderIDField, cancelOrderBtn);

	    return cancelOrderBox;
	}
	
	
	private static void handleCancelOrder(User user, TextField cancelOrderIDField) {
	    String orderIDStr = cancelOrderIDField.getText();

	    if (orderIDStr.isEmpty()) {
	    	Alerts.errorMessage("Cancel Order Error", "Order ID must be filled out.");
	        return;
	    }

	    try {
	        int orderID = Integer.parseInt(orderIDStr);
	        Order orderToCancel = user.getOrderById(orderID);

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
	
	private static Button createExportOrdersButton(User user) {
	    Button exportOrdersBtn = new Button("Export Orders");
	    exportOrdersBtn.setOnAction(e -> handleExportOrders(user));
	    return exportOrdersBtn;
	}
	
	private static void handleExportOrders(User user) {
	    ListView<Order> ordersListView = new ListView<>(FXCollections.observableArrayList(user.getOrders()));
	    ordersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

	    Button exportBtn = new Button("Export Selected Orders");
	    exportBtn.setOnAction(ev -> {
	        List<Order> selectedOrders = ordersListView.getSelectionModel().getSelectedItems();
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Save Orders");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	        File file = fileChooser.showSaveDialog(new Stage());
	        if (file != null) {
	            UserManager userManager = UserManager.getInstance();
	            userManager.exportOrders(user.getUsername(), selectedOrders, file.getAbsolutePath(),
	                    exportOrderId.isSelected(), exportStatus.isSelected(), exportPlacedTime.isSelected(),
	                    exportCollectedTime.isSelected(), exportTotalPrice.isSelected(), exportItems.isSelected());
	        }
	    });

	    VBox exportBox = new VBox(10, ordersListView, exportOrderId, exportStatus, exportPlacedTime,
	            exportCollectedTime, exportTotalPrice, exportItems, exportBtn);
	    exportBox.setPadding(new Insets(10));
	    Scene exportScene = new Scene(exportBox, 300, 400);
	    Stage exportStage = new Stage();
	    exportStage.setTitle("Export Orders");
	    exportStage.setScene(exportScene);
	    exportStage.show();
	}
	
	private static Button createLogoutButton() {
	    Button logoutBtn = new Button("Log Out");
	    logoutBtn.setOnAction(e -> {
	        UserManager userManager = UserManager.getInstance();
	        userManager.logoutUser();
	        BurritoKingApp.showLogin();
	    });
	    return logoutBtn;
	}
	
	////////////////////////////////new verison currently testing
	
	
	///////////////////////////////// attempt of making breaking up the long as  method

	// this method creates an action button to view orders
	//also creates the layout for these buttons
//	private static VBox createActionButtons(User user) {//dont like this name change later
//	    VBox vbox = new VBox(10);
//	    vbox.setAlignment(Pos.CENTER);
//	    vbox.setPadding(new Insets(10, 10, 10, 10));
//
//	    
//		//this method is to make a new order. 
//		
//		
//		
//        
//        //for collection of order
//	    HBox collectOrderBox = new HBox(10);
//        Label collectOrderLabel = new Label("Enter Order ID to Collect:");
//        TextField collectOrderIDField = new TextField();
//        collectOrderIDField.setMaxWidth(100);
//
//        //for collection of order
//        Label collectTimeLabel = new Label("Collection Time (HH:mm) 24hr time");
//        TextField collectTimeField = new TextField();
//        collectTimeField.setMaxWidth(100);
//			
//		
//		
//		//button for collection of order
//		Button collectOrderBtn = new Button("Collect Order"); // maybe add setPromptText ??
//		
//		collectOrderBtn.setOnAction(e -> {
//			//retrives the values for the ID and collection time
//		    String orderIDStr = collectOrderIDField.getText();
//		    String collectTimeStr = collectTimeField.getText();
//		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//
//		    //used if the entry they made is blank
//		    if (orderIDStr.isEmpty() || collectTimeStr.isEmpty()) {
//		        showAlert("Collect Order Error", "Order ID and collection time must be filled out.");
//		        return;
//		    }
//
//		    try {
//		        int orderID = Integer.parseInt(orderIDStr);
//		        // Check if the collect time string is in the correct format
//		        String[] timeParts = collectTimeStr.split(":");
//		        if (timeParts.length != 2) {
//		            throw new DateTimeParseException("Invalid time format", collectTimeStr, 0);
//		        }
//
//		        //parse the hour and minutes from the input
//		        int hour = Integer.parseInt(timeParts[0]);
//		        int minute = Integer.parseInt(timeParts[1]);
//		        LocalDateTime collectTime = LocalDateTime.now().withHour(hour).withMinute(minute);
//
//		        
//		        //retrieve the order to be collected based on the ID
//		        Order orderToCollect = user.getOrderById(orderID);
//		        if (orderToCollect != null) {
//		        	
//		        	//calculates the min time to collect the order based on pretime
////		            LocalDateTime minCollectTime = orderToCollect.getOrderPlacedTime().plusMinutes(UserManager.calculatePreparationTime(orderToCollect)); /// original code before skeleton 
//		        	UserManager userManager = UserManager.getInstance();// skeleton
//                    LocalDateTime minCollectTime = orderToCollect.getOrderPlacedTime().plusMinutes(userManager.calculatePreparationTime(orderToCollect));//skeleton
//		            
//		            //checks if the collection time is valid based on preptime
//		            if (collectTime.isAfter(minCollectTime) && !orderToCollect.getStatus().equals("cancelled")) {
//		            	
//		            	//collects the order if everything is ok
////		                UserManager.collectOrder(user.getUsername(), orderID, collectTime);//got rid of skeleton 
//		            	userManager.collectOrder(user.getUsername(), orderID, collectTime);//skeleton
//		                BurritoKingApp.showDashboard(user);
//		                
//		                //shows error if collection time is invalid or they cancelled the order already
//		            } else {
//		                showAlert("Collect Order Error", "Invalid collection time or order has been cancelled.");
//		            }
//		            
//		            // shown if the order ID is not valid/found
//		        } else {
//		            showAlert("Collect Order Error", "Order not found.");
//		        }
//		        
//		        //shown if user puts in an invalid input
//		    } catch (NumberFormatException | DateTimeParseException ex) {
//		        showAlert("Collect Order Error", "Invalid input. Please check the Order ID and time format.");
//		    }
//		});
//		
//
//		 collectOrderBox.getChildren().addAll(collectOrderLabel, collectOrderIDField, collectTimeLabel, collectTimeField, collectOrderBtn);
//		 vbox.getChildren().add(collectOrderBox);
//
//		 HBox cancelOrderBox = new HBox(10);
//		 cancelOrderBox.setAlignment(Pos.CENTER_RIGHT);
//		 Label cancelOrderLabel = new Label ("Enter Order ID to cancel:");
//		 TextField cancelOrderIDField = new TextField();
//		 cancelOrderIDField.setMaxWidth(100);
//		 
//		 //button to cancel
//		 Button cancelOrderBtn = new Button("Cancel Order");
//		 cancelOrderBtn.setOnAction(e -> {
//			 
//			 //gets the order ID entered
//			    String orderIDStr = cancelOrderIDField.getText();
//
//			    //checks if the ID input field is empty
//			    if (orderIDStr.isEmpty()) {
//			        showAlert("Cancel Order Error", "Order ID must be filled out.");
//			        return;
//			    }
//
//			    try {
//			    	
//			    	//parse the order ID into an int
//			    	//finds the order ID thats going to be cancelled
//			        int orderID = Integer.parseInt(orderIDStr);
//			        Order orderToCancel = user.getOrderById(orderID);
//			        
//			        //cancels the order if its found as the status is under placed.
//			        if (orderToCancel != null && orderToCancel.getStatus().equals("placed")) {
////			            UserManager.cancelOrder(user.getUsername(), orderID); // skeleton loss
//			        	UserManager userManager = UserManager.getInstance();// skeleton 
//	                    userManager.cancelOrder(user.getUsername(), orderID);// skeleton
//			            showAlert("Cancel Order", "Order " + orderID + " has been successfully cancelled.");
//			        } else {
//			        	//shown if the order cant be cancelled or doesnt exist
//			            showAlert("Cancel Order Error", "Order cannot be cancelled or does not exist.");
//			        }
//			    } catch (NumberFormatException ex) {
//			    	
//			    	//shown if theres an issue with the input
//			        showAlert("Cancel Order Error", "Invalid Order ID format. Please enter a valid number.");
//			    }
//			});
//		 
//
//		 
//		 cancelOrderBox.getChildren().addAll(cancelOrderLabel, cancelOrderIDField, cancelOrderBtn);
//		 vbox.getChildren().add(cancelOrderBox);
//		    
//		    //button to export the orders
//		    Button exportOrdersBtn = new Button("Export Orders");
//		    
//		    //creates a list to display the users selections.
//		    exportOrdersBtn.setOnAction(e -> {
//		        ListView<Order> ordersListView = new ListView<>(FXCollections.observableArrayList(user.getOrders()));
//		        ordersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		        ordersListView.setCellFactory(param -> new ListCell<Order>() {
//		            @Override
//		            protected void updateItem(Order order, boolean empty) {
//		                super.updateItem(order, empty);
//		                if (empty || order == null) {
//		                    setText(null);
//		                } else {
//		                    setText("Order ID: " + order.getOrderID() + " - " + order.getStatus());
//		                }
//		            }
//		        });
//
//		        // Add checkboxes for selecting information to export
//		        CheckBox exportOrderId = new CheckBox("Order ID");
//		        exportOrderId.setSelected(true);
//		        CheckBox exportStatus = new CheckBox("Status");
//		        exportStatus.setSelected(true);
//		        CheckBox exportPlacedTime = new CheckBox("Placed Time");
//		        exportPlacedTime.setSelected(true);
//		        CheckBox exportCollectedTime = new CheckBox("Collected Time");
//		        exportCollectedTime.setSelected(true);
//		        CheckBox exportTotalPrice = new CheckBox("Total Price");
//		        exportTotalPrice.setSelected(true);
//		        CheckBox exportItems = new CheckBox("Items");
//		        exportItems.setSelected(true);
//
//		        //button to export the order.
//		        Button exportBtn = new Button("Export Selected Orders");
//		        exportBtn.setOnAction(ev -> {
//		        	
//		        	//grabs everything from the secltions in the list to export
//		            List<Order> selectedOrders = ordersListView.getSelectionModel().getSelectedItems();
//		            FileChooser fileChooser = new FileChooser();
//		            fileChooser.setTitle("Save Orders");
//		            
//		            //makes the export a csv
//		            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
//		            File file = fileChooser.showSaveDialog(new Stage());
//		            if (file != null) {
//		            	//export the things that were selected
////		                UserManager.exportOrders(user.getUsername(), selectedOrders, file.getAbsolutePath(),
////		                        exportOrderId.isSelected(), exportStatus.isSelected(), exportPlacedTime.isSelected(),
////		                        exportCollectedTime.isSelected(), exportTotalPrice.isSelected(), exportItems.isSelected()); // got rid of skeleton
//		            	UserManager userManager = UserManager.getInstance();
//	                    userManager.exportOrders(user.getUsername(), selectedOrders, file.getAbsolutePath(),//skeleton
//	                            exportOrderId.isSelected(), exportStatus.isSelected(), exportPlacedTime.isSelected(),//skeleton
//	                            exportCollectedTime.isSelected(), exportTotalPrice.isSelected(), exportItems.isSelected());//skeleton
//		            }
//		        });
//
//		        VBox exportBox = new VBox(10, ordersListView, exportOrderId, exportStatus, exportPlacedTime,
//		                exportCollectedTime, exportTotalPrice, exportItems, exportBtn);
//		        exportBox.setPadding(new Insets(10));
//		        Scene exportScene = new Scene(exportBox, 300, 400);
//		        Stage exportStage = new Stage();
//		        exportStage.setTitle("Export Orders");
//		        exportStage.setScene(exportScene);
//		        exportStage.show();
//		    });
//
//		    vbox.getChildren().add(exportOrdersBtn);
//		    
//		    
//		    
//		    
//		    
//		   
//
//		    //button to logout
//		    //clears the session and takes user back to login screen
//		    Button logoutBtn = new Button("Log Out");
//		    logoutBtn.setOnAction(e -> {
////		        UserManager.logoutUser(); // skeleton loss
//		    	UserManager userManager = UserManager.getInstance();//skelton
//	            userManager.logoutUser();//skeleton
//		        BurritoKingApp.showLogin();
//		    });
//		    vbox.getChildren().add(logoutBtn);
//		 
//
//		    return vbox;
//		}
	
///////////////////////////////// attempt of making breaking up the long as  method
	 
	private static VBox createOrdersDisplay(User user) { /// I dont need this method
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
	
	
	
	private static void confirmationMessage(User user) {
        double totalPrice = tempOrder.calculateTotal();
//        int waitingTime = calculateWaitingTime(tempOrder);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText("Please confirm your order");
        alert.setContentText(String.format("Total Price: $%.2f\n", totalPrice));

        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType goBackButton = new ButtonType("Go back");
        alert.getButtonTypes().setAll(confirmButton, goBackButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            BurritoKingApp.showOrderOnDashBoard(user);
        } 
    }
	 
	 //used to hold the basket controls
	private static HBox createBasketControls(ListView<KingItem> basketListView) {
	    HBox hbox = new HBox(10);
	    hbox.setAlignment(Pos.CENTER);
	    hbox.setPadding(new Insets(10, 0, 10, 0));

	    //button to remove the item selected from the basket
	    Button removeItemBtn = new Button("Remove Selected Item");
	    removeItemBtn.setOnAction(e -> {
	    	
	    	//used to select the item
	        KingItem selectedItem = basketListView.getSelectionModel().getSelectedItem();
	        if (selectedItem != null) {
	        	
	        	//removes the item from the basket/tempOrder
	            tempOrder.getItems().remove(selectedItem);
	            basketItems.remove(selectedItem);
	            Alerts.infoMessage("Item Removed", "Item(s) has been removed.");
	        }
	    });
	        
	        
	    	//Button for updating the qunatity of the selected item.
	        Button updateQuantityBtn = new Button("Update Quantity");
	        updateQuantityBtn.setOnAction(e -> {
	        	
	        	//used to get the item from the basket view
	            KingItem selectedItem = basketListView.getSelectionModel().getSelectedItem();
	            
	            //here i created an area to add the new quantity and update the temp order
	            if (selectedItem != null) {
	                Stage quantityStage = new Stage();
	                VBox quantityBox = new VBox(10);
	                quantityBox.setAlignment(Pos.CENTER);
	                quantityBox.setPadding(new Insets(10));
	                TextField quantityField = new TextField();
	                quantityField.setMaxWidth(50);
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
	 
	 
	
	 


}

