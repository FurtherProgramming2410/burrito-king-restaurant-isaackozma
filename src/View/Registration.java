package View;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import Controller.UserManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class Registration {
	
	//here i create the basic layout of the registration screen
	//so far i've made all of the screens the same layout
	//the further along the program goes I believe there will be more changes
	public static GridPane createRegistration() {
		//I here set the alignment of the grid.
		//i set the vertical and horizontal gaps
		//Also set the padding 
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		pane.getStyleClass().add("grid-pane");
		
		//Here i created a label for the user for the username
		//i also positioned it
		Label userNameLabel = new Label("Username:");
		pane.add(userNameLabel, 0, 1);
		
		//Here I created an area for the user to enter their username
		//positioned it as well
		TextField userNameTextField = new TextField();
		pane.add(userNameTextField, 1, 1);
		
		//here i created a label for the password
		//positioned it.
		Label passwordLabel = new Label("password:");
		pane.add(passwordLabel,  0, 2);
		
		//here ive added an area for the user to enter their password
		//also positioned it
		PasswordField passwordEntryField = new PasswordField();
		pane.add(passwordEntryField,  1, 2);
		
		//here ive added a label for firstname
		//also positioned it
		Label firstNameLabel = new Label("First Name:");
		pane.add(firstNameLabel,  0, 3);
		
		//here ive added an area for the user to enter their first name
		//also positioned it
		TextField firstNameField = new TextField();
		pane.add(firstNameField,  1, 3);
		
		//here ive added a label for the last name
		//also positioned it
		Label lastNameLabel = new Label("Last Name:");
		pane.add(lastNameLabel, 0, 4);
		
		//here ive added an area for the user to enter their last name
		//also positioned it
		TextField lastNameField = new TextField();
		pane.add(lastNameField, 1, 4);
		
		
		//here ive created a button for register
		//when clicked it gets the information from the text fields
		//username, password, first & last name
		Button btnRegiester = new Button("Register");
		
		btnRegiester.setOnAction(event -> {
			String username = userNameTextField.getText();
			String password = passwordEntryField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			
			//here to check if anyting is empty 
			 if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
				 showAlert1("Registration Error", "All fields must be filled out.");
			        return;
			    }
			//boolean is used to register the user, checks if it is unique
			boolean registered = UserManager.registerUser(username, password, firstName, lastName);
		
			//if the registration is ok then it will direct the user to the login area
			if(registered ) {
				showAlert("registration Successful", "Account successfully registered");
				BurritoKingApp.showLogin();
			
			//if its not unique it will say the attempt was unsucessful.
			}else {
				showAlert("Registration error", "Username already exists");
			}
			});
			pane.add(btnRegiester, 1, 5);
		
		return pane;
	}
	
	//used to let the user know theyve the account was successfull
	private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}
	
	//used when something has gone wrong.
	private static void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
	}

}
