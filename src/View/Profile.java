package View;

import Controller.UserManager;
import Model.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

//this class is just so the user can edit their profile
public class Profile {
	
	//setting up the display of the profile page
	//i give the user the ability to edit their names and password. 
	// not the username as that was said not to.
	public static GridPane createProfile(User user) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25, 25, 25, 25));
		
		Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField(user.getFirstName());
        pane.add(firstNameLabel, 0, 0);
        pane.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField(user.getLastName());
        pane.add(lastNameLabel, 0, 1);
        pane.add(lastNameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        pane.add(passwordLabel, 0, 2);
        pane.add(passwordField, 1, 2);
        
        
        //here I make a save button for the changes
        //takes the info added and updates it in usermanager class.
        //user is then taken back to the dashboard. 
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e ->{
        	user.setFirstName(firstNameField.getText());
        	user.setLastName(lastNameField.getText());
        	user.setPassword(passwordField.getText());
        	UserManager.updateUserProfile(user.getUsername(), firstNameField.getText(), lastNameField.getText(), passwordField.getText());
        	BurritoKingApp.showDashboard(user);
        });
        
        pane.add(saveBtn, 1, 3);
        return pane;
	}

}
