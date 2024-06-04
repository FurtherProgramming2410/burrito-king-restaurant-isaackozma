package util;

import javafx.scene.control.Alert;

//This class is used to hold my different alerts i have
//i figured it would be cleaner if i had this class and package instead of repeating code constantly
public class Alerts {

	//This method is to display an error message
	public static void errorMessage(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

	//this method is to display a confirmation message
    public static void confirmationMessage(String title, String message) {
        showAlert(Alert.AlertType.CONFIRMATION, title, message);
    }

    //this method is used to display a info message.
    public static void infoMessage(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    //this private method it to display the specific type of alert
    //Takes the alert type and sets all the content for the alert
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
