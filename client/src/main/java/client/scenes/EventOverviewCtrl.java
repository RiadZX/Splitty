package client.scenes;

import client.services.I18N;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML
    public TextFlow textFlow;
    @FXML
    public Pane backButton;
    @FXML
    public Button settleDebt;
    @FXML
    public Button sendInvite;
    @FXML
    public Button addExpense;
    @FXML
    public Label expenseLabel;
    @FXML
    public Label participantLabel;
    @FXML
    public Label backButtonLabel;
    @FXML
    public Label allFilter;
    @FXML
    public Label fromFilter;
    @FXML
    public Label toFilter;

    @FXML
    public ImageView flagView;

    @FXML
    private TextField eventTitle;

    @FXML
    private ListView<Expense> expensesList;

    @FXML
    private ComboBox<Participant> payerSelector;

    private Event event;

    private List<Expense> expenses;

    private Participant payer;

    private int filter;

    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
        this.event=new Event();
        this.filter=0;
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

        this.sendInvite.setOnAction(event -> sendInvite());
        I18N.update(sendInvite);
        I18N.update(addExpense);
        I18N.update(settleDebt);
        I18N.update(expenseLabel);
        I18N.update(participantLabel);
        I18N.update(eventTitle);
        I18N.update(backButtonLabel);
        this.sendInvite.setOnAction(event -> sendInvite());

        payerSelector.setCellFactory(param -> getPayerListCell());
        payerSelector.setButtonCell(getPayerListCell());

        expensesList.setCellFactory(param -> getExpenseListCell());

    }

    public ListCell<Expense> getExpenseListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setGraphic(null);
                    setText(null);
                    return;
                }
                List<UUID> uuids=item.getDebts().stream().map(debt -> debt.getParticipant().getId()).toList();
                System.out.println(uuids);
                System.out.println(filter);
                if (payer != null && payer.getId() != null && (!item.getPaidBy().getId().equals(payer.getId()) && filter==2)||(filter==1 && !uuids.contains(payer.getId()))){
                    setGraphic(null);
                    setText(null);
                }
                else {
                    System.out.println(payer);
                    System.out.println(item.getPaidBy());
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
                    setText(null);
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
            notificationService.showConfirmation("Title Change", "The title has been successfully changed!");
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

    public void editExpense(Expense e) {
        mainCtrl.showEditExpense(e);
    }


    public void refresh(){
        try {
            Event refreshed = server.getEvent(event.getId());
            System.out.println("refreshing");
            this.setEvent(refreshed);
            payerSelector.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerSelector.getSelectionModel().selectFirst();
//            payerSelector.setOnAction(e -> {
//                payer = payerSelector.getValue();
//                expensesList.getItems().clear();
//                expensesList.getItems().addAll(expenses);
//            });
            expensesList.getItems().clear();
            expensesList.getItems().addAll(expenses);
            System.out.println("refreshed");
            /* TO DO:
            * - refresh all data related to the event
            * - add functionality to the expense list and filtering*/
        }catch (WebApplicationException e) {
            notificationService.showError(I18N.get("event.overview.showRefreshingEvent"), I18N.get("event.overview.showRefreshingEventMessage"));
        }
    }

    private BorderPane createRow(Expense e) {
        Insets insets = new Insets(0.0, 5.0, 0.0, 5.0);
        BorderPane bp = new BorderPane();
        double convertedAmount = server.convert(e.getAmount(), e.getCurrency(), String.valueOf(mainCtrl.getUser().getPrefferedCurrency()), e.getDate());
        DecimalFormat df = new DecimalFormat("#.00");
        Text text=new Text(
                (e.getPaidBy() == null ? "NULL" : e.getPaidBy().getName())
                        + "'s expense - "
                        + df.format(convertedAmount)
                        + " "
                        + mainCtrl.getUser().getPrefferedCurrency());
        text.setFill(Color.WHITESMOKE);
        bp.setLeft(text);

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

    public void setFlag(String language){
        this.flagView.setImage(new Image("client/icons/flag-"+language+".png"));
    }

    public void changeLanguage(){
        switch (this.mainCtrl.getUser().getLanguage()){
            case "english" -> this.mainCtrl.switchToDutch();
            case "dutch" -> this.mainCtrl.switchToEnglish();
            default -> System.out.println("Unsupported language "+this.mainCtrl.getUser().getLanguage());
        }
    }
    public void refreshLanguage(){
        this.allFilter.setText(I18N.get("general.all"));
        this.fromFilter.setText(I18N.get("general.from"));
        this.toFilter.setText(I18N.get("general.to"));
    }

    public void setToFilter(){
        filter=1;
        payer = payerSelector.getValue();
        expensesList.getItems().clear();
        expensesList.getItems().addAll(expenses);
    }

    public void setFromFilter(){
        filter=2;
        payer = payerSelector.getValue();
        expensesList.getItems().clear();
        expensesList.getItems().addAll(expenses);
    }

    public void setAllFilter(){
        filter=0;
        payer = payerSelector.getValue();
        expensesList.getItems().clear();
        expensesList.getItems().addAll(expenses);
    }

    //    public double calculateIncoming(Participant p){
//        double incoming = 0;
//        for (Expense e : event.getExpenses()){
//            if (!e.getPaidBy().getId().equals(p.getId())){
//                continue;
//            }
//            List<Debt> debts = e.getDebts();
//            for (Debt d : debts) {
//                if (d.isPaid()) {
//                    continue;
//                }
//                incoming += d.getAmount();
//            }
//        }
//        return incoming;
//    }
//
//    public double calculateOutgoing(Participant p){
//        double outgoing = 0;
//        for (Expense e : event.getExpenses()){
//            if (e.getPaidBy().getId().equals(p.getId())){
//                continue;
//            }
//            List<Debt> debts = e.getDebts();
//            for (Debt d : debts) {
//                if (d.isPaid()) {
//                    continue;
//                }
//                if (d.getParticipant().getId().equals(p.getId())){
//                    outgoing+=d.getAmount();
//                }
//            }
//        }
//        return outgoing;
//    }

}
