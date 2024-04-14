package client.scenes;

import client.services.I18NService;
import client.services.NotificationService;
import client.utils.ServerUtils;
import client.utils.User;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;


public class UserSettingsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;
    @FXML
    private ComboBox<String> currencyComboBox;

    @FXML
    private Labeled profileSettings;
    @FXML
    private Labeled nameLabel;
    @FXML
    private Labeled emailLabel;
    @FXML
    private Labeled ibanLabel;
    @FXML
    private Labeled bicLabel;
    @FXML
    private Labeled sneButton;
    @FXML
    private Labeled cancelButton;
    @FXML
    private Labeled currencyOption;

    private final I18NService i18n;
    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService, I18NService i18n) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
        this.i18n = i18n;
    }

    public void refreshFields(){
        User user=this.mainCtrl.getUser();
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        ibanField.setText(user.getIban());
        bicField.setText(user.getBic());
        currencyComboBox.getSelectionModel().select(user.getPrefferedCurrency().toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> currencies = Arrays.stream(User.Currency.values()).map(Enum::toString).toList();
        currencyComboBox.getItems().addAll(currencies);
        i18n.update(profileSettings);
        i18n.update(nameLabel);
        i18n.update(emailLabel);
        i18n.update(ibanLabel);
        i18n.update(bicLabel);
        i18n.update(sneButton);
        i18n.update(cancelButton);
        i18n.update(currencyOption);
    }

    public void back(){
        mainCtrl.showSettings();
    }

    public void save(){
        var name=nameField.getText();
        var email=emailField.getText();
        var iban=ibanField.getText();
        var bic=bicField.getText();
        var currency=currencyComboBox.getValue();
        if (name.isEmpty()) {
            notificationService.showError(i18n.get("general.warning"), i18n.get("settings.profile.warning"));
            return;
        }
        User newUser=new User(name, email, iban, bic);
        newUser.setLanguage(this.mainCtrl.getUser().getLanguage());
        LinkedHashMap<UUID, UUID> tmp =(LinkedHashMap<UUID, UUID>) this.mainCtrl.getUser().getEventParticipant();
        newUser.setEventParticipant(tmp);
        newUser.setPrefferedCurrency(User.Currency.valueOf(currency));
        this.mainCtrl.setUser(newUser);
        back();
    }
}
