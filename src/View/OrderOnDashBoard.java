package View;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Controller.PaymentInfo;
import Controller.UserManager;
import Interface.KingItem;
import Model.FoodItem;
import Model.Order;
import Model.Meal;
import Model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

//This class is used for the layout and functionality for placing an order on the dahsboard
public class OrderOnDashBoard {
	
	
	
	//Creates the layout
	//creates the buttons and labels for all
	public static GridPane createOrderPlacement(User user) {
		GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        Label cardNumberLabel = new Label("Please enter a fake Credit Card Number:");
        TextField cardNumberField = new TextField();
        cardNumberField.setMaxWidth(150);

        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setMaxWidth(50);

        Label cvvLabel = new Label("CVV:");
        TextField cvvField = new TextField();
        cvvField.setMaxWidth(50);

        Label fakeTimeLabel = new Label("Fake Time (HH:mm):");
        TextField fakeTimeField = new TextField();
        fakeTimeField.setMaxWidth(100);
        
        Label creditsLabel = new Label("Credits to use:"); ////////////////// vip
        TextField creditField = new TextField();
        creditField.setMaxWidth(100);
        
        if(!user.isVIP()) {
        	creditsLabel.setVisible(false);
        	creditField.setVisible(false);
        }

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setOnAction(e -> {
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();
            String fakeTimeStr = fakeTimeField.getText();
            String creditsStr = user.isVIP() ? creditField.getText() : "0";//////////////////// vip

            //checks to see if any of the fields are empy
            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || fakeTimeStr.isEmpty() || (user.isVIP() && creditsStr.isEmpty())) {// added credit stuff here for vip
                showAlert("Order Error", "All fields must be filled out.");
                return;
            }

            
            try {
            	//parse the fake time input
                String[] timeParts = fakeTimeStr.split(":");
                if (timeParts.length != 2) {
                    throw new DateTimeParseException("Invalid time format", fakeTimeStr, 0);
                }

                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                LocalDateTime fakeTime = LocalDateTime.now().withHour(hour).withMinute(minute);

                //gets the info from the temp order from the dahsboard.
                Order tempOrder = Dashboard.getTempOrder();
                System.out.println("Placing order ID: " + tempOrder.getOrderID() + " with items: " + tempOrder.getItems().size());

                for (KingItem item : tempOrder.getItems()) {
                    System.out.println("Item: " + item.getName() + " Price: " + item.getPrice());
                }


                
                if (validateOrder(cardNumber, expiryDate, cvv)) {
                    int credits = Integer.parseInt(creditsStr);
                    if (user.isVIP() && credits > 0) {
                        boolean success = UserManager.useCredits(user.getUsername(), credits);
                        if (!success) {
                            showAlert("Order Error", "Failed to use credits. Please check your credits balance.");
                            return;
                        }
                    }
//                    boolean success = UserManager.useCredits(user.getUsername(), credits);
//                    if (success) {
                        UserManager.placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, user.isVIP() ? Integer.parseInt(creditsStr) : 0);
                        System.out.println("Order placed: " + tempOrder);
                        int preparationTime = UserManager.calculatePreparationTime(tempOrder);
                        showAlert("Order Successful", "Your order has been placed successfully. Preparation time: " + preparationTime + " minutes.");
                        Dashboard.clearTempOrder();
                        BurritoKingApp.showDashboard(user);
                    } else {
                        showAlert("Order Error", "Failed to use credits. Please check your credits balance.");
                    }
//                } else {
//                    showAlert("Order Error", "Invalid payment details. Please check your inputs and try again.");
//                }
            } catch (NumberFormatException | DateTimeParseException ex) {
                showAlert("Order Error", "Invalid fake time format. Please enter the time in HH:mm format.");
            }
                
        });

        pane.add(cardNumberLabel, 0, 0);
        pane.add(cardNumberField, 1, 0);
        pane.add(expiryDateLabel, 0, 1);
        pane.add(expiryDateField, 1, 1);
        pane.add(cvvLabel, 0, 2);
        pane.add(cvvField, 1, 2);
        pane.add(fakeTimeLabel, 0, 3);
        pane.add(fakeTimeField, 1, 3);
        pane.add(creditsLabel, 0, 4);
        pane.add(creditField, 1, 4);
        pane.add(placeOrderBtn, 1, 5);
        return pane;
    }
	
	
	//
	private static void itemsBeingOrdered(Order order, String itemName, int quantity) {
		for(int i = 0; i < quantity; i++) {
			switch(itemName) {
			case "Burrito":
				order.addItem(new FoodItem("Burrito", 7, 1), 1);
				break;
			case "Fries":
				order.addItem(new FoodItem("Fries", 4, 1),1);
				break;
			case "Soda":
				order.addItem(new FoodItem("soda", 2.5, 1), 1);
			}
		}
	}
	
	//method that makes sure the order is valid
	//alows for a check of all the vital needs.
	 private static boolean validateOrder(String cardNumber, String expiryDate, String cvv) {
	        return PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate) && PaymentInfo.validateCVV(cvv);
	    }

	
	private static LocalDateTime parseFakeTime(String fakeTime) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime time = LocalTime.parse(fakeTime, formatter);
			return LocalDateTime.of(LocalDate.now(), time);
		}catch (DateTimeParseException e) {
			return null;
		}
	}
	
	
	//shown when thers an error
	private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
	//shown when the user is furthing their progress
	private static void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
