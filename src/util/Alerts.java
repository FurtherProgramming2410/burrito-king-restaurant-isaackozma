package util;

import javafx.scene.control.Alert;

public class Alerts {

	public static void errorMessage(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

//    public static void confirmationMessage(String title, String message) {
//        showAlert(Alert.AlertType., title, message);
//    }

    public static void infoMessage(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
