package View;

import Controller.UserManager;
import Model.User;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import util.Alerts;

//this class is for the ability to edit the profile of a user.
public class Profile {
	
	//reference to the main application instance
	private BurritoKingApp app;

	//constructor that initializes the app reference
    public Profile(BurritoKingApp app) {
        this.app = app;
    }
	
	//this method is used to create the edit profile page layout
    //It will allow for users to edit their first & last name as well as the password.
    //set the alignment and spacing the same as other classes
	public  GridPane createProfile(User user) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		
		//Label and textField for first name
		Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField(user.getFirstName());
        pane.add(firstNameLabel, 0, 0);
        pane.add(firstNameField, 1, 0);

        //label and textfield for last name
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField(user.getLastName());
        pane.add(lastNameLabel, 0, 1);
        pane.add(lastNameField, 1, 1);

        //label and textfield for password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        pane.add(passwordLabel, 0, 2);
        pane.add(passwordField, 1, 2);
        
        //Here the program gets the instance of UserManager
        UserManager userManager = UserManager.getInstance();
        
        //Here I create the save button which when clicked updates the user profile
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e ->{
        	if (passwordField.getText().isEmpty()) {
        		Alerts.errorMessage("Update Error", "Password must be filled out. Can be the same or different");
        		return;
        	}
        	//here the user object is updated with the new values given by the user.
        	 user.setFirstName(firstNameField.getText());
             user.setLastName(lastNameField.getText());
             user.setPassword(passwordField.getText());
             
             //here the program uodates the user profile in the UserManager class
             //It then returns the user back to the dashboard.
             userManager.updateUserProfile(user.getUsername(), firstNameField.getText(), lastNameField.getText(), passwordField.getText());
             app.showDashboard(user);
        });
        
        pane.add(saveBtn, 1, 3);
        return pane;
	}
	


}
