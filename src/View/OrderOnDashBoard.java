package View;


import Controller.PaymentInfo;
import Controller.UserManager;
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

// this class takes care of the order placment on the dashboard. 
//not sure if i will keep this class honestly.... may have been a waste.
public class OrderOnDashBoard {
	
	
	
	
	public static GridPane createOrderPlacement(User user) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25 ));
		
		Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberField = new TextField();
        cardNumberField.setMaxWidth(150);

        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setMaxWidth(50);

        Label cvvLabel = new Label("CVV:");
        TextField cvvField = new TextField();
        cvvField.setMaxWidth(50);
		
		

		
		
		Button placeOrderBtn = new Button("Place Order");
		placeOrderBtn.setOnAction(e -> {
			String cardNumber = cardNumberField.getText();
			String expiryDate = expiryDateField.getText();
			String cvv = cvvField.getText();
			
			if(validateOrder(cardNumber, expiryDate, cvv)) {
				UserManager.placeOrder(user.getUsername(), Dashboard.getTempOrder(), cardNumber, expiryDate, cvv);
				System.out.println("Order Successful");
				BurritoKingApp.showDashboard(user);
				
			}else {
				System.out.println("Details are incorrect, try again");
			}
	});
		pane.add(cardNumberLabel, 0, 0);
        pane.add(cardNumberField, 1, 0);
        pane.add(expiryDateLabel, 0, 1);
        pane.add(expiryDateField, 1, 1);
        pane.add(cvvLabel, 0, 2);
        pane.add(cvvField, 1, 2);
        pane.add(placeOrderBtn, 1, 3);

        return pane;
	}
	
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
	
	private static boolean validateOrder(String cardNumber, String expiryDate, String cvv) {
		return PaymentInfo.validateCardNumber(cardNumber) && PaymentInfo.validateExpiryDate(expiryDate) && PaymentInfo.validateCVV(cvv);
	}
	
	private static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
