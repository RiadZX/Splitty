package client.scenes;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddExpenseCtrl implements Initializable {
    private final MainCtrl mainCtrl;
    private Event event;
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
        allBox.setSelected(false);
        partialPaidSelector.setVisible(true);
    }

    public void setup(Event event){
        this.event = event;
        createTagBox.setVisible(false);
        System.out.println(event.getParticipants().size());
        paidBySelector.setItems(FXCollections.observableList(event.getParticipants().stream().map(Participant::getName).toList()));
        errorLabel.setVisible(false);

        currencySelector.setItems(FXCollections.observableList(Stream.of("EUR", "USD", "RON").toList()));
        currencySelector.setValue("EUR");
        currencySelector.setVisible(true);

        tagSelector.getChildren().clear();
        for (int i = 1; i<event.getTags().size() + 1; i++){
            CheckBox checkbox = new CheckBox(event.getTags().get(i-1).getTag());
            tagSelector.getChildren().add(checkbox);
        }

        partialPaidSelector.setVisible(false);
        partialPaidSelector.getChildren().clear();
        for (Participant p : event.getParticipants()){
            partialPaidSelector.getChildren().add(new CheckBox(p.getName()));
        }
    }

    public void createExpense(){
        //get participants
        List<Participant> participantList = event.getParticipants();

        //store who paid
        Participant paidBy = findParticipant(paidBySelector.getValue());
        if (paidBy == null) {
            showErrorLabel("Please select who paid!");
            return;
        }

        if (!someBox.isSelected() && !allBox.isSelected()){
            showErrorLabel("Please select how to split!");
            return;
        }

        //create a list of debtors
        participantList.remove(paidBy);
        if (someBox.isSelected()){
            for(Node c : partialPaidSelector.getChildren()){
                if (c.getClass() == CheckBox.class && !((CheckBox) c).isSelected()){
                    participantList.remove(findParticipant(((CheckBox) c).getText()));
                }
            }
        }

        if (howMuchField.getText() == null || howMuchField.getText().isEmpty()){
            showErrorLabel("Please type in the amount!");
            return;
        }

        List<Debt> debts = createDebts(toEur(Double.parseDouble(howMuchField.getText()), currencySelector.getValue()), participantList);

        //create the list of tags (God bless the creator of stream() :) )
        List<Tag> tags = tagSelector.getChildren().stream().filter(n -> n.getClass() == CheckBox.class).map(n -> ((CheckBox) n).getText()).map(this::findTag).toList();

        if (tags.isEmpty()){
            showErrorLabel("Please select at least one tag or create one!");
            return;
        }

        //create the expense
        Expense newExpense = new Expense(paidBy.getName() + " paid for " + tags.get(0).getTag(), Double.parseDouble(howMuchField.getText()), whenField.getValue().atStartOfDay(), paidBy, event, debts, tags);
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
    public Participant findParticipant(String name){
        Participant r = null;
        for (Participant p : event.getParticipants()) if (p.getName().equals(name)) r = p;
        return r;
    }

    public Tag findTag(String name){
        Tag t = null;
        for (Tag tag : event.getTags()) if (tag.getTag().equals(name)) t = tag;
        return t;
    }

    public void showErrorLabel(String error){
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }
}
