package client.services;

/**
 * NotificationService
 *     Interface for the notification service, the NotificationHelper class implements this interface.
 *     To use the interface, add it to the constructor of controller, so that it gets injected.
 *     If you need to show an error, call the showError method.
 *     If you need to give feedback to the user for an action they have done
 *     (e.g. to confirm that they successfully switched languages), call the informUser method.
 *     For more types of notifications, extend the interface and implement the new methods in the NotificationHelper class.
 */
public interface NotificationService {
    void showError(String title, String message);

    void informUser(String title, String message, String header);

    boolean showConfirmation(String title, String message);
}
