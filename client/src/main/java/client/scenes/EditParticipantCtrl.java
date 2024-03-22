package client.scenes;

import client.services.NotificationHelper;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.scene.control.TextField;

public class EditParticipantCtrl {
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

    private Event event;
    private Participant p;

    @Inject
    public EditParticipantCtrl(MainCtrl mainCtrl, Event event, Participant p, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.p = p;
        this.server = server;
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
            String warningMessage = "You have not properly filled in the following fields: ( ";
            if (name.getText().isEmpty()){
                warningMessage += "name ";
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
            notificationHelper.showError("Warning!", warningMessage);
            return;
        }
        if (!email.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            String warningMessage = """
                    The email address you have filled in is not valid,
                    please type in an email address with the correct format
                    (i.e john.smith@emailprovide.com)
                    """;
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", warningMessage);
            return;
        }
        if (iban.getText().length() != 34){
            String warningMessage = """
                    The IBAN address you have filled in is not valid,
                    please type in an Iban with the correct format
                    (i.e format with length 34)
                    """;
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", warningMessage);
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
            String warningMessage = """
                    Unable to delete the participant.
                    """;
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", warningMessage);

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
