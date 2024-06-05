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

//This class is used for the layout and functionality for paying for an order.
public class OrderOnDashBoard {
	
	private BurritoKingApp app;

	//this constructor takes BurritoKingApp instance as a parameter
    public OrderOnDashBoard(BurritoKingApp app) {
        this.app = app;
    }
	
	//This method creates the layout for placing an order
    //ive set the alignment, horizontal and vertical gaps between the cells
    //and set the padding
    public GridPane createOrderPlacement(User user) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        //Label and textfield for the creditcard number 
        //user can enter their credit card number here
        Label cardNumberLabel = new Label("Please enter a fake Credit Card Number(16 numbers):");
        TextField cardNumberField = new TextField();
        cardNumberField.setMaxWidth(150);

        //Label and textfield for the expiry date 
        //user can enter their dates here
        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setMaxWidth(50);

        //Label and textfield for the CVV
        //user can enter their CVV here
        Label cvvLabel = new Label("CVV (3 Numbers):");
        TextField cvvField = new TextField();
        cvvField.setMaxWidth(50);

        //Label and textfield for the the fake time the user needs to enter
        Label fakeTimeLabel = new Label("Fake Time (HH:mm):");
        TextField fakeTimeField = new TextField();
        fakeTimeField.setMaxWidth(100);

        //Label and textfield for the Credits
        //user can enter the number of credits they want to use here
        //Will only appear if user is VIP
        Label creditsLabel = new Label("Credits to use (enter 0 if none):");
        TextField creditField = new TextField();
        creditField.setMaxWidth(100);

        //hides credits field if user is not a VIP
        if (!user.isVIP()) {
            creditsLabel.setVisible(false);
            creditField.setVisible(false);
        }
        
        //Button to place the order.
        Button placeOrderBtn = new Button("Place Order");
        UserManager userManager = UserManager.getInstance();
        
        //Here is the code for the action that will take place when the button is pressed
        //here the program retreives all the information from the user 
        //if any of it is empty then the user will be alerted of this.
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
            	//here the program parse the fake time which is entered by the user
            	//if they enter an invaild time than an alert will occur.
                String[] timeSplit = fakeTimeStr.split(":");
                if (timeSplit.length != 2) {
                    throw new DateTimeParseException("Invalid time format", fakeTimeStr, 0);
                }

                int hour = Integer.parseInt(timeSplit[0]);
                int minute = Integer.parseInt(timeSplit[1]);
                if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                    Alerts.errorMessage("Order Error", "Invalid time entered. Please enter a valid time in HH:mm format.");
                    return;
                }
                
                LocalDateTime fakeTime = LocalDateTime.now().withHour(hour).withMinute(minute);

                //here the program retrieves the tempOrder 
                Order tempOrder = app.getDashboard().getTempOrder();


                
                //here the payment details are validated
                //If there are any issues then an alert will appear informing the user
                if (validateOrder(cardNumber, expiryDate, cvv)) {
                    int credits = Integer.parseInt(creditsStr);
                    if (user.isVIP() && credits > 0) {
                    	boolean success = UserManager.getInstance().useCredits(user.getUsername(), credits);
                        if (!success) {
                        	Alerts.errorMessage("Order Error", "Failed to use credits. Please check your credits balance.");
                            return;
                        }
                    }

                    //Here the user places the order and saves all the details
                    //an alert will info the user of the preptime, price, orderID and the time which the order was placed.
                    //alerts will appear if there are any issues that arise such as invalid payment or wrong time format.
                    userManager.placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, credits);
                    UserManager.getInstance().placeOrder(user.getUsername(), tempOrder, cardNumber, expiryDate, cvv, fakeTime, credits);
                    UserManager.getInstance().saveOrdersToFile();
                    int preparationTime = UserManager.getInstance().calculatePreparationTime(tempOrder);
                    Alerts.infoMessage("Order Successful", "Your order has been placed successfully. Preparation time: " + preparationTime + " minutes. "
                            + "\n" + tempOrder);
                    app.getDashboard().clearTempOrder();
                    app.showDashboard(user);
                } else {
                    Alerts.errorMessage("Order Error", "Invalid payment details. Please check your inputs and try again.");
                }
            } catch (NumberFormatException | DateTimeParseException ex) {
            	Alerts.errorMessage("Order Error", "Invalid fake time format. Please enter the time in HH:MM format.");
            }
        });

        //Here the components are added to the grid pane
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
	
	
	//This method validates the order by checking the payment details
    //uses paymentInfo to ensure that the card is legit and everything is correct.
	 private boolean validateOrder(String cardNumber, String expiryDate, String cvv) {
	        return PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate) && PaymentInfo.validateCVV(cvv);
	    }


}