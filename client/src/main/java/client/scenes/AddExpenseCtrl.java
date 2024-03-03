package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private CheckBox allBox;
    @FXML
    private CheckBox someBox;
    @FXML
    private ComboBox<String> paidBySelector, partialPaySelector;
    @FXML
    private TextField whatForField, howMuchField;
    @FXML
    private DatePicker whenField;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, Event event) {
        this.server = server;
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
        partialPaySelector.setVisible((false));
    }

    @FXML
    public void checkSome(){
        allBox.setSelected(false);
        partialPaySelector.setVisible(true);
    }

    public void setup(Event event){
        this.event = event;
        partialPaySelector.setVisible(false);
        for (ComboBox<String> stringComboBox : Arrays.asList(paidBySelector, partialPaySelector)) {
            stringComboBox.setItems(FXCollections.observableList(event.getParticipants().stream().map(Participant::getName).sorted().toList()));
        }
    }

    public void createExpense(){
        //get participants
        List<Participant> participantList = event.getParticipants();

        //store who paid
        Participant paidBy = null;
        for (Participant p : participantList) if (p.getName().equals(paidBySelector.getValue())) paidBy = p;
        if (paidBy == null) return;

        //create a list of debtors
        participantList.remove(paidBy);
        List<Debt> debts = createDebts(Double.parseDouble(howMuchField.getText()), participantList);

        //create the expense
        Expense newExpense = new Expense(whatForField.getText(), Double.parseDouble(howMuchField.getText()), whenField.getValue().atStartOfDay(), paidBy, event, debts);
        for(Debt d : newExpense.getDebts()) d.setExpense(newExpense); //setup each debt's expense pointer
        event.addExpense(newExpense);
        mainCtrl.showEventOverviewScene(event);
    }

    private List<Debt> createDebts(double amount, List<Participant> participants){
        List<Debt> debts = new ArrayList<>();
        for (Participant p : participants) debts.add(new Debt(new Expense(), p, amount/participants.size() + 1));
        return debts;
    }
}
