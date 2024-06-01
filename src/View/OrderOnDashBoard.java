package View;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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
import util.Alerts;

//This class is used for the layout and functionality for placing an order on the dahsboard

public class OrderOnDashBoard {
    public static GridPane createOrderPlacement(User user) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        Label cardNumberLabel = new Label("Please enter a fake Credit Card Number(16 numbers):");
        TextField cardNumberField = new TextField();
        cardNumberField.setMaxWidth(150);

        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setMaxWidth(50);

        Label cvvLabel = new Label("CVV (3 Numbers):");
        TextField cvvField = new TextField();
        cvvField.setMaxWidth(50);

        Label fakeTimeLabel = new Label("Fake Time (HH:mm):");
        TextField fakeTimeField = new TextField();
        fakeTimeField.setMaxWidth(100);

        Label creditsLabel = new Label("Credits to use (enter 0 if none):");
        TextField creditField = new TextField();
        creditField.setMaxWidth(100);

        if (!user.isVIP()) {
            creditsLabel.setVisible(false);
            creditField.setVisible(false);
        }

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setOnAction(e -> {
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();
            String fakeTimeStr = fakeTimeField.getText();
            String creditsStr = user.isVIP() ? creditField.getText() : "0";

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || fakeTimeStr.isEmpty() || (user.isVIP() && creditsStr.isEmpty())) {
            	Alerts.errorMessage("Order Error", "All fields must be filled out.");
                return;
            }

            try {
                String[] timeSplit = fakeTimeStr.split(":");
                if (timeSplit.length != 2) {
                    throw new DateTimeParseException("Invalid time format", fakeTimeStr, 0);
                }

                int hour = Integer.parseInt(timeSplit[0]);
                int minute = Integer.parseInt(timeSplit[1]);
                LocalDateTime fakeTime = LocalDateTime.now().withHour(hour).withMinute(minute);

                Order tempOrder = Dashboard.getTempOrder();
                System.out.println("Placing order ID: " + tempOrder.getOrderID() + " with items: " + tempOrder.getItems().size());

                for (KingItem item : tempOrder.getItems()) {
                    System.out.println("Item: " + item.getName() + " Price: " + item.getPrice());
                }

                if (validateOrder(cardNumber, expiryDate, cvv)) {
                    int credits = Integer.parseInt(creditsStr);
//                    UserManager userManager = UserManager.getInstance();//added skeleton// changed for credits
                    if (user.isVIP() && credits > 0) {
//                        boolean success = UserManager.useCredits(user.getUsername(), credits);
                    	
//                        boolean success = userManager.useCredits(user.getUsername(), credits);// added skelton
                    	boolean success = UserManager.getInstance().useCredits(user.getUsername(), credits);// changed for credits
                        if (!success) {
                        	Alerts.errorMessage("Order Error", "Failed to use credits. Please check your credits balance.");
                            return;
                        }
                    }
                    

//                    UserManager.placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, credits); // loss skeleton
                    
//                    userManager.placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, credits); // added skeleton
                    UserManager.getInstance().placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, credits);// changed for credits
                    System.out.println("Order placed: " + tempOrder);

//                    UserManager.saveOrdersToFile(); // loss skeleton 
                    
                    
//                    userManager.saveOrdersToFile();// addded skelton
                    UserManager.getInstance().saveOrdersToFile();//changed for credits
                    System.out.println("Order placed and saved successfully!");

//                    int preparationTime = UserManager.calculatePreparationTime(tempOrder); // loss skeleton
//                    int preparationTime = userManager.calculatePreparationTime(tempOrder);// added skelton 
                    int preparationTime = UserManager.getInstance().calculatePreparationTime(tempOrder);// changed for credits
                    Alerts.infoMessage("Order Successful", "Your order has been placed successfully. Preparation time: " + preparationTime + " minutes. "
                    		+ "\n" + tempOrder);
                    Dashboard.clearTempOrder();
                    BurritoKingApp.showDashboard(user);
                } else {
                    Alerts.errorMessage("Order Error", "Invalid payment details. Please check your inputs and try again.");
                }
            } catch (NumberFormatException | DateTimeParseException ex) {
            	Alerts.errorMessage("Order Error", "Invalid fake time format. Please enter the time in HH:MM format.");
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
	private static void itemsBeingOrdered(Order order, String itemName, int quantity) {///// this even being used anymore?
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

	
	private static LocalDateTime parseFakeTime(String fakeTime) {/// yeah nah probably change this.
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime time = LocalTime.parse(fakeTime, formatter);
			return LocalDateTime.of(LocalDate.now(), time);
		}catch (DateTimeParseException e) {
			return null;
		}
	}
	
	
	

}