package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.glassfish.jersey.internal.guava.Table;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;


public class DebtResolveCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Event event;

    // the amounts that everyone is receiving
    private HashMap<Participant, Amounts> amounts;

    @FXML
    private TableView<TableEntry> debtTable;

    @FXML
    private TableColumn<TableEntry, String> personColumn;

    @FXML
    private TableColumn<TableEntry, Double> amountColumn;

    private ObservableList<TableEntry> tableEntries = FXCollections.observableArrayList();

    @Inject
    public DebtResolveCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.amounts = new HashMap<>();
        personColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        debtTable.setItems(tableEntries);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void refresh() {
        amounts.clear();
        List<Expense> expenses = event.getExpenses();

        System.out.println(expenses);

        for (Expense e : expenses) {
            List<Debt> debts = e.getDebts();
            amounts.merge(e.getPaidBy(),
                    new Amounts(e.getAmount(), 0.),
                    (start, end) -> new Amounts(
                            start.giving() + end.giving(),
                            start.getting() + end.getting()
                    )
            );
            for (Debt d : debts) {
                amounts.merge(d.getParticipant(),
                        new Amounts(0., d.getAmount()),
                        (start, end) -> new Amounts(
                                start.giving() + end.giving(),
                                start.getting() + end.getting()
                        )
                );
            }
        }

        HashMap<Participant, Double> netAmounts = new HashMap<>();

        for (Entry<Participant, Amounts> a : this.amounts.entrySet()) {
            // calculate net spent
            Double net = a.getValue().net();
            netAmounts.put(a.getKey(), net);
        }

        Participant max = null;
        // gotta get negative infinity
        Double maxAmount = Double.NEGATIVE_INFINITY;

        for (Entry<Participant, Double> a : netAmounts.entrySet()) {
            if (a.getValue() > maxAmount) {
                maxAmount = a.getValue();
                max = a.getKey();
            }
        }
        tableEntries.clear();
        for (Entry<Participant, Double> a : netAmounts.entrySet()) {
            netAmounts.put(a.getKey(), a.getValue() - maxAmount);
            tableEntries.add(new TableEntry(a.getKey().getName(), a.getValue() - maxAmount));
        }

        System.out.println("calculated");
        System.out.println(netAmounts);
    }
}

record Amounts(Double giving, Double getting) {
    public Amounts add(Amounts one, Amounts two) {
        return new Amounts(
                one.giving + two.giving,
                one.getting + two.getting
        );
    }

    public Double net() {
        return this.giving - this.getting;
    }
}

record TableEntry(String person, Double amount) {}