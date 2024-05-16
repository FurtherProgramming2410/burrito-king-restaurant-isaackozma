package View;
import Controller.UserManager;
import Model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Login {
	//here i create the basic layout of the login screen
	//so far i've made all of the screens the same layout
	//the further along the program goes I believe there will be more changes
	public static GridPane createLogin() {
		//I here set the alignment of the grid.
		//i set the vertical and horizontal gaps
		//Also set the padding 
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25,25,25,25));
		
		//Here i created a label for the user for the username
		//i also positioned it
		Label userNameLabel = new Label("Username: ");
		pane.add(userNameLabel, 0, 1);
		
		//Here I created an area for the user to enter their username
		//positioned it as well
		TextField userNameTextField = new TextField();
		pane.add(userNameTextField, 1, 1);
		
		//here i created a label for the password
		//positioned it.
		Label passwordLabel = new Label("Password:");
		pane.add(passwordLabel,  0, 2);
		
		//here ive added an area for the user to enter their password
		//also positioned it
		PasswordField passwordEntryField = new PasswordField();
		pane.add(passwordEntryField, 1, 2);
		
		//Here i created a button for the user to click once theyve entered their details
		//positioned it
		Button btnLogin = new Button ("Sign in");
		pane.add(btnLogin, 1, 4);
		
		//here is an action statement that is triggered when the button is pressed
		//gets the text from the user name and password.
		btnLogin.setOnAction(event -> {
			String username = userNameTextField.getText();
			String password = passwordEntryField.getText();
			
			//this will try to log in the user
			//if the entry is correct then the user will be taken to the dashboard
			User user = UserManager.loginUser(username, password);
			if (user != null) {
				BurritoKingApp.showDashboard(user);
				
				//otherwise this message will print saying there was an issue with the log in
			}else {
				System.out.println("Login unsuccessfull, please try again or register.");
			}
		});
		
		
		
		// here i have a register button if the user has not got a profile yet.
		//the button when pressed triggers an action which takes users to the registration screen.
		Button btnToRegister = new Button("Register");
		btnToRegister.setOnAction(event -> BurritoKingApp.showRegistration());
		pane.add(btnToRegister, 1, 5);
		
		return pane;
		
	}

}
