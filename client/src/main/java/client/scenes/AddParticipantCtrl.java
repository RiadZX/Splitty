package client.scenes;

import client.services.NotificationHelper;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;
import javafx.scene.control.TextField;

public class AddParticipantCtrl {
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

    @Inject
    public AddParticipantCtrl(MainCtrl mainCtrl, Event event, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.server = server;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void addParticipantButton() {
        //Participant toAdd = new Participant();
        String participantName = name.getText();
        String participantEmail = email.getText();
        String participantIban = iban.getText();
        String participantBic = bic.getText();
        if (participantName.isEmpty() || participantEmail.isEmpty() || participantIban.isEmpty()
                || participantBic.isEmpty()) {
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = "You haven't filled in the following fields: ( ";
            if (participantName.isEmpty()){
                warningMessage += "name ";
            }
            if (participantEmail.isEmpty()){
                warningMessage += "email ";
            }
            if (participantIban.isEmpty()){
                warningMessage += "iban ";
            }
            if (participantBic.isEmpty()){
                warningMessage += "bic";
            }
            warningMessage += ")";
            notificationHelper.showError("Warning!", warningMessage);
            return;
        }

        if (!participantEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            String warningMessage = """
                    The email address you have filled in is not valid,
                    please type in an email address with the correct format
                    (i.e john.smith@emailprovide.com)
                    """;
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", warningMessage);
            return;
        }
        if (participantIban.length() != 34) {
            String warningMessage = """
                    The IBAN address you have filled in is not valid,
                    please type in an Iban with the correct format
                    (i.e format with length 34)
                    """;
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", warningMessage);
            return;
        }
        server.addParticipant(
                event.getId(),
                new Participant(
                        participantName,
                        this.event,
                        participantIban,
                        participantEmail,
                        participantBic
                )
        );
        name.clear();
        email.clear();
        iban.clear();
        bic.clear();
        returnToOverview();
    }


    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }
}
