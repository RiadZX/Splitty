package client.scenes;

import client.services.I18NService;
import client.services.NotificationService;
import client.utils.DebtResolve;
import client.utils.ServerUtils;
import client.utils.DebtResolveTableEntry;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class DebtResolveCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Event event;

    @FXML
    private TableView<DebtResolveTableEntry> debtTable;

    @FXML
    private TableColumn<DebtResolveTableEntry, String> fromColumn;

    @FXML
    private TableColumn<DebtResolveTableEntry, String> toColumn;

    @FXML
    private TableColumn<DebtResolveTableEntry, Double> amountColumn;

    @FXML
    private Label backButtonLabel;

    @FXML
    private Button settleButton;


    private final ObservableList<DebtResolveTableEntry> tableEntries = FXCollections.observableArrayList();

    private final I18NService i18n;

    @Inject
    public DebtResolveCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService, I18NService i18n) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.i18n = i18n;
    }

    public void backToOverview() {
        mainCtrl.showEventOverviewScene(this.event);
    }

    public void executeSettle() {
        event.getExpenses().stream().flatMap(e -> e.getDebts().stream()).forEach(d -> d.pay());
        server.addEvent(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        i18n.update(fromColumn);
        i18n.update(toColumn);
        i18n.update(amountColumn);
        i18n.update(backButtonLabel);
        i18n.update(settleButton);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        debtTable.setItems(tableEntries);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void refresh() {

        tableEntries.clear();
        DebtResolve.resolve(this.event)
                .stream()
                .map(DebtResolveTableEntry::fromResult)
                .forEach(tableEntries::add);
        System.out.println(this.tableEntries);
    }
}


