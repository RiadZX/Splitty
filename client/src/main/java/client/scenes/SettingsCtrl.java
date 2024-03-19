package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;

public class SettingsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;

    @Inject
    public SettingsCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
    }
}
