package client.scenes;

import client.services.I18N;
import client.services.NotificationHelper;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditParticipantCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField iban;

    @FXML
    private TextField bic;

    @FXML
    public Button cancelButton2;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Label nameLabel2;
    @FXML
    public Label editParticipant;
    private Event event;
    private Participant p;

    @Inject
    public EditParticipantCtrl(MainCtrl mainCtrl, Event event, Participant p, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.p = p;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        I18N.update(cancelButton2);
        I18N.update(editButton);
        I18N.update(deleteButton);
        I18N.update(nameLabel2);
        I18N.update(editParticipant);
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public void setParticipant(Participant p) {
        this.p = p;
    }

    public void editParticipantButton() {
        if (name.getText().isEmpty() || email.getText().isEmpty() || iban.getText().isEmpty()
                || bic.getText().isEmpty()) {
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = I18N.get("participant.add.error");
            if (name.getText().isEmpty()){
                warningMessage += I18N.get("participant.add.error.name") + " ";
            }
            if (email.getText().isEmpty()){
                warningMessage += "email ";
            }
            if (iban.getText().isEmpty()){
                warningMessage += "iban ";
            }
            if (bic.getText().isEmpty()){
                warningMessage += "bic";
            }
            warningMessage += ")";
            notificationHelper.showError(I18N.get("general.warning"), warningMessage);
            return;
        }
        if (!email.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            String warningMessage = I18N.get("participant.add.error.message.email");
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError(I18N.get("general.warning"), warningMessage);
            return;
        }
        if (iban.getText().length() != 34){
            String warningMessage = I18N.get("participant.add.error.message.iban");
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError(I18N.get("general.warning"), warningMessage);
            return;
        }
        p.setName(name.getText());
        p.setEmail(email.getText());
        p.setIban(iban.getText());
        p.setBic(bic.getText());
        server.updateParticipant(
                event,
                p
        );
        returnToOverview();
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }

    public void removeParticipant() {
        try {
            server.removeParticipant(event, p);
        } catch (WebApplicationException e) {
            String warningMessage = I18N.get("participant.remove.error");
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError(I18N.get("general.warning"), warningMessage);

        } finally {
            returnToOverview();
        }
    }

    public void refresh() {
        this.email.setText(p.getEmail());
        this.name.setText(p.getName());
        this.iban.setText(p.getIban());
        this.bic.setText(p.getBic());
    }
}
