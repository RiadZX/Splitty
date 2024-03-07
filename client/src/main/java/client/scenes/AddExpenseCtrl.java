package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddExpenseCtrl implements Initializable {
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private CheckBox allBox;
    @FXML
    private CheckBox someBox;
    @FXML
    private ComboBox<String> paidBySelector, partialPaySelector, currencySelector;
    @FXML
    private TextField howMuchField, tagField;
    @FXML
    private DatePicker whenField;
    @FXML
    private VBox tagSelector;
    @FXML
    private VBox createTagBox;
    @FXML
    private Label tagErrorLabel;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, Event event) {
        this.mainCtrl = mainCtrl;
        this.event = event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    @FXML
    public void backToOverview(){
        mainCtrl.showEventOverviewScene(event);
    }

    @FXML
    public void checkAll(){
        someBox.setSelected(false);
    }

    @FXML
    public void checkSome(){
        allBox.setSelected(false);
    }

    public void setup(Event event){
        this.event = event;
        createTagBox.setVisible(false);
        currencySelector.setItems(FXCollections.observableList(Stream.of("EUR", "USD", "RON").toList()));
        currencySelector.setValue("EUR");
        currencySelector.setVisible(true);
        for (int i = 1; i<event.getTags().size() + 1; i++){
            CheckBox checkbox = new CheckBox(event.getTags().get(i-1).getTag());
            tagSelector.getChildren().add(checkbox);
        }
    }

    public void createExpense(){
        //get participants
        List<Participant> participantList = event.getParticipants();

        //store who paid
        Participant paidBy = null;
        for (Participant p : participantList){
            if (p.getName().equals(paidBySelector.getValue())) {
                paidBy = p;
            }
        }
        if (paidBy == null) {
            return;
        }

        //create a list of debtors
        participantList.remove(paidBy);
        List<Debt> debts = createDebts(toEur(Double.parseDouble(howMuchField.getText()), currencySelector.getValue()), participantList);

        //create the expense
        Expense newExpense = new Expense("a", Double.parseDouble(howMuchField.getText()), whenField.getValue().atStartOfDay(), paidBy, event, debts, new ArrayList<>());
        for (Debt d : newExpense.getDebts()){
            d.setExpense(newExpense); //setup each debt's expense pointer
        }
        event.addExpense(newExpense);
        //server.updateEvent(event); yields a StackOverFlow when serializing because of bidirectional pointers inside Expense.java and Event.java
        mainCtrl.showEventOverviewScene(event);
    }

    private List<Debt> createDebts(double amount, List<Participant> participants){
        List<Debt> debts = new ArrayList<>();
        for (Participant p : participants) {
            debts.add(new Debt(new Expense(), p, amount/participants.size() + 1));
        }
        return debts;
    }

    //temporary method to convert other currencies in EUR
    private double toEur(double amount, String curr){
        return switch (curr) {
            case "USD" -> amount * mainCtrl.getUsdToEur();
            case "RON" -> amount * mainCtrl.getRonToEur();
            default -> amount;
        };
    }

    public void createTag(){
        String tagName = tagField.getText();
        if(tagName == null || tagName.isEmpty() || event.getTags().stream().map(Tag::getTag).toList().contains(tagName)) tagErrorLabel.setVisible(true);
        else{
            Tag tag = new Tag(tagName);
            event.addTag(tag);
            tagSelector.getChildren().add(new CheckBox(tag.getTag()));
            closeCreateTag();
        }
    }

    public void openTagScene(){
        createTagBox.setVisible(true);
        tagErrorLabel.setVisible(false);
    }

    public void closeCreateTag(){
        createTagBox.setVisible(false);
    }
}
