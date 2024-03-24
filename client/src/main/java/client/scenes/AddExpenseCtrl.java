package client.scenes;

import client.services.NotificationHelper;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;

    private Expense expense;

    @FXML
    private Button submitButton;
    @FXML
    private CheckBox allBox, someBox;
    @FXML
    private ComboBox<String> paidBySelector, currencySelector;
    @FXML
    private TextField howMuchField, tagField;
    @FXML
    private DatePicker whenField;
    @FXML
    private VBox tagSelector, createTagBox, partialPaidSelector;
    @FXML
    private Label tagErrorLabel, errorLabel;

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
        partialPaidSelector.setVisible(false);
    }

    @FXML
    public void checkSome(){
        System.out.println(paidBySelector.getValue());
        allBox.setSelected(false);
        partialPaidSelector.setVisible(true);
    }

    public void setup(Event event, Expense expense){
        this.event = event;
        this.expense = expense;

        createTagBox.setVisible(false);

        currencySelector.setItems(FXCollections.observableList(Stream.of("EUR", "USD", "RON").toList()));
        currencySelector.setValue("EUR");
        currencySelector.setVisible(true);

        paidBySelector.setItems(FXCollections.observableList(event.getParticipants().stream().map(Participant::getName).toList()));

        tagSelector.getChildren().clear();
        for (int i = 1; i<event.getTags().size() + 1; i++){
            CheckBox checkbox = new CheckBox(event.getTags().get(i-1).getTag());
            tagSelector.getChildren().add(checkbox);
        }

        partialPaidSelector.getChildren().clear();
        for (Participant p : event.getParticipants()){
            partialPaidSelector.getChildren().add(new CheckBox(p.getName()));
        }

        if (expense == null){
            setupNewExpense();
        }
        else {
            setupExistingExpense(expense);
        }
    }

    private void setupExistingExpense(Expense expense){
        submitButton.setText("Save");

        paidBySelector.setValue(expense.getPaidBy().getName());
        howMuchField.setText(String.valueOf(expense.getAmount()));
        whenField.setValue(LocalDate.from(expense.getDate()));

        //check partial debtors if any
        boolean partialPay = false;
        List<Participant> debtors = expense.getDebts().stream().map(Debt::getParticipant).toList();
        for (Participant p : event.getParticipants()){
            if (!p.equals(expense.getPaidBy()) && !debtors.contains(p)){
                checkSome();
                partialPay = true;
            }
        }
        if (partialPay){
            for (Participant p : debtors){
                for (Node c : partialPaidSelector.getChildren()){
                    if (c.getClass() == CheckBox.class){
                        if (((CheckBox) c).getText().equals(p.getName())){
                            ((CheckBox) c).setSelected(true);
                        }
                    }
                }
            }
        }
        else {
            checkAll();
        }

        //check selected tags
        for (Tag t : expense.getTags()){
            for (Node c : tagSelector.getChildren()){
                if (c.getClass() == CheckBox.class){
                    if (((CheckBox) c).getText().equals(t.getTag())){
                        ((CheckBox) c).setSelected(true);
                    }
                }
            }
        }
    }

    private void setupNewExpense() {
        submitButton.setText("Add");

        paidBySelector.setValue(event.getParticipants().get(0).getName());

        howMuchField.setText("");
        someBox.setSelected(false);
        allBox.setSelected(false);

        partialPaidSelector.setVisible(false);
    }

    public void submitPressed(){
        //create an expense
        Expense newExpense = createExpense();
        if (newExpense != null) {
            newExpense.setEvent(event);
            for (Debt d : newExpense.getDebts()) {
                d.setExpense(newExpense); //setup each debt's expense pointer
            }

            //update the eventual edited expense or add it as a new one
            if (expense == null) {
                event.addExpense(newExpense);
                server.addExpense(event.getId(), newExpense);
            } else {
                expense = newExpense;
                server.updateExpense(event.getId(), expense);
            }
            mainCtrl.showEventOverviewScene(event);
        }
    }

    private Expense createExpense(){
        //get participants
        List<Participant> participantList = new ArrayList<>(event.getParticipants());

        //store who paid
        Participant paidBy = findParticipant(paidBySelector.getValue());
        if (paidBy == null) {
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    Your payee information is incorrect
                    please add a valid payee
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return null;
        }

        LocalDate date = whenField.getValue();
        if (date == null){
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    You have not selected any date
                    please select a valid date
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return null;
        }

        if (!someBox.isSelected() && !allBox.isSelected()){
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    You have not selected any split options
                    please select how you wish to split
                    or if you wish to split with the whole group
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return null;
        }

        //create a list of debtors
        participantList.remove(paidBy);
        if (someBox.isSelected()){
            for (Node c : partialPaidSelector.getChildren()){
                if (c.getClass() == CheckBox.class && !((CheckBox) c).isSelected()){
                    participantList.remove(findParticipant(((CheckBox) c).getText()));
                }
            }
        }

        if (howMuchField.getText() == null || howMuchField.getText().isEmpty()){
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    You have not selected an amount to pay
                    please type in an amount
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return null;
        }

        if (Integer.parseInt(howMuchField.getText()) < 0){
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    You cannot select a negative amount
                    please type in a positive number
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return null;
        }

        List<Debt> debts = createDebts(toEur(Double.parseDouble(howMuchField.getText()), currencySelector.getValue()), participantList);

        //create the list of tags (God bless the creator of stream() :) )
        List<Tag> tags = tagSelector.getChildren().stream().filter(n -> n.getClass() == CheckBox.class).map(n -> ((CheckBox) n)).filter(CheckBox::isSelected).map(Labeled::getText).map(this::findTag).toList();

        // TODO we leave tag implementation for next week

        /*if (tags.isEmpty()){
            NotificationHelper notificationHelper = new NotificationHelper();
            String warningMessage = """
                    You have not selected any tags
                    please create a tag.
                    Or select a pre-existing one.
                    """;
            notificationHelper.showError("Warning", warningMessage);
            return;
        }
         */

        //create the expense
        return new Expense(paidBy.getName() + " paid for ", Double.parseDouble(howMuchField.getText()), date.atStartOfDay(), paidBy, event, debts, new ArrayList<>());
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
        if (tagName == null || tagName.isEmpty() || event.getTags().stream().map(Tag::getTag).toList().contains(tagName)){
            tagErrorLabel.setVisible(true);
        }
        else {
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
    public Participant findParticipant(String name){
        Participant r = null;
        for (Participant p : event.getParticipants()){
            if (p.getName().equals(name)){
                r = p;
            }
        }
        return r;
    }

    public Tag findTag(String name){
        Tag t = null;
        for (Tag tag : event.getTags()){
            if (tag.getTag().equals(name)){
                t = tag;
            }
        }
        return t;
    }
}
