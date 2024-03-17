package client.services;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class NotificationHelper implements NotificationService {
    /**
     * Show an error message, in a new window.
     * @param title title of the error message
     * @param message message of the error
     */
    @Override
    public void showError(String title, String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.initModality(Modality.APPLICATION_MODAL);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
