package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML
    public Button sendInviteButton;
    @FXML
    public TextFlow textFlow;
    @FXML
    public Pane backButton;

    @FXML
    private TextField eventTitle;

    @FXML
    private ListView<Expense> expensesList;

    @FXML
    private ComboBox<Participant> payerSelector;

    private Event event;

    private List<Expense> expenses;

    private Participant payer;

    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
        this.event=new Event();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.eventTitle.setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case ENTER -> changeTitle();
                case ESCAPE -> this.eventTitle.setText(this.event.getTitle());
                default -> {
                }
            }
        }));

        this.sendInviteButton.setOnAction(event -> sendInvite());

        payerSelector.setCellFactory(param -> getPayerListCell());
        payerSelector.setButtonCell(getPayerListCell());

        expensesList.setCellFactory(param -> getExpenseListCell());

    }

    public ListCell<Expense> getExpenseListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || (payer != null && !item.getPaidBy().getId().equals(payer.getId()))) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(createRow(item));
                }
            }
        };
    }
    public ListCell<Participant> getPayerListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                 super.updateItem(item, empty);
                 if (empty || item == null) {
                     setGraphic(null);
                     setText("ALL");
                 } else {
                     setText(item.getName());
                 }
            }
        };
    }

    public Event getEvent(){
        return this.event;
    }

    public void setEvent(Event newEvent){
        this.event=newEvent;
        eventTitle.setText(this.event.getTitle());
        reassignParticipants(this.event.getParticipants());
        System.out.println("set event: "+ event);
        this.expenses = server.getExpensesByEvent(this.event.getId());
        System.out.println("expenses: "+ expenses);
    }

    public void reassignParticipants(List<Participant> participantList){
        System.out.println(participantList.stream().map(x -> x.getName()).toList());
        textFlow.getChildren().clear();
        if (participantList.isEmpty()) {
            textFlow.getChildren().add(new Label("No participants, yet"));
            return;
        }
        for (Participant p : participantList.subList(0, participantList.size() - 1)) {
            Label label = new Label(p.getName());
            label.setOnMouseClicked(e -> editParticipant(p));
            textFlow.getChildren().add(label);
            textFlow.getChildren().add(new Label(", "));
        }
        Label lastLabel = new Label(participantList.get(participantList.size() - 1).getName());
        lastLabel.setOnMouseClicked(e -> editParticipant(participantList.get(participantList.size() - 1)));
        textFlow.getChildren().add(lastLabel);
    }

    public void changeTitle(){
        this.event.setName(this.eventTitle.getText());
        try {
            this.server.updateEvent(this.event);
        }
        catch (WebApplicationException e){
            notificationService.showError("Error updating event", "Could not update event title");
        }
    }

    public void sendInvite(){
        mainCtrl.showInviteView(this.event);
    }

    public void backToStart(){
        mainCtrl.showStartScene();
    }

    public void addParticipant(){
        mainCtrl.showAddParticipantScene(event);
    }

    public void editParticipant(Participant p){
        mainCtrl.showEditParticipantScene(event, p);
    }

    public void addExpense(){
        mainCtrl.showAddExpense();
    }

    public void refresh(){
        try {
            Event refreshed = server.getEvent(event.getId());
            System.out.println("refreshing");
            this.setEvent(refreshed);
            payerSelector.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerSelector.getItems().add(0, null);
            payerSelector.getSelectionModel().selectFirst();
            payerSelector.setOnAction(e -> {
                payer = payerSelector.getValue();
                expensesList.getItems().clear();
                expensesList.getItems().addAll(expenses);
            });
            expensesList.getItems().clear();
            expensesList.getItems().addAll(expenses);
            System.out.println("refreshed");
            /* TO DO:
            * - refresh all data related to the event
            * - add functionality to the expense list and filtering*/
        }catch (WebApplicationException e) {
            notificationService.showError("Error refreshing event", "Could not refresh event data");
        }
    }

//    public BorderPane createComboBoxEntry(Participant p) {
//        BorderPane bp = new BorderPane();
////        bp.setOnKeyPressed(x -> {
////            payer = p;
////            populateList();
////        });
//        bp.setLeft(new Text(p.getName()));
//        return bp;
//    }

//    public void populateList() {
//        expensesList.getItems().clear();
//        List<BorderPane> contents = expenses.stream().map(this::createRow).toList();
//        expensesList.getItems().addAll(contents);
//    }
//
    public void editExpense(Expense e) {
        System.out.println("TODO: Create page for editing expenses");
    }


    private BorderPane createRow(Expense e) {
        Insets insets = new Insets(0.0, 5.0, 0.0, 5.0);
        BorderPane bp = new BorderPane();
        bp.setLeft(new Text("Expense paid by " + (e.getPaidBy() == null ? "NULL" : e.getPaidBy().getName())));

        Image editImage = new Image("client/icons/pencil.png");
        ImageView edit = new ImageView();
        edit.setImage(editImage);
        edit.setOnMouseClicked(x -> editExpense(e));
        edit.cursorProperty().set(Cursor.HAND);
        edit.setFitHeight(12.0);
        edit.setPickOnBounds(true);
        edit.setFitWidth(12.0);
        BorderPane.setMargin(edit, insets);

        bp.setRight(edit);
        return bp;
    }
}
