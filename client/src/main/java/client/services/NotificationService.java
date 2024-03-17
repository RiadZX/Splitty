package client.services;

/**
 * NotificationService
 *     Interface for the notification service, the NotificationHelper class implements this interface.
 *     To use the interface, add it to the constructor of controller, so that it gets injected.
 *     If you need to show an error, call the showError method.
 *     For more types of notifications, extend the interface and implement the new methods in the NotificationHelper class.
 */
public interface NotificationService {
    void showError(String title, String message);
}
