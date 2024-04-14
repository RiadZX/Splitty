package client.scenes;

import client.services.I18NService;
import client.services.NotificationService;
import client.utils.DebtResolve;
import client.utils.ServerUtils;
import client.utils.DebtResolveTableEntry;
import client.utils.User;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
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
        event.getExpenses().stream().flatMap(e -> e.getDebts().stream()).forEach(Debt::pay);
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
        amountColumn.setCellFactory(new Callback<TableColumn<DebtResolveTableEntry, Double>, TableCell<DebtResolveTableEntry, Double>>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                return new AmountCellFormatter(server, mainCtrl);
            }
        });
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        debtTable.setItems(tableEntries);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void refresh() {
        amountColumn.setCellFactory(new Callback<TableColumn<DebtResolveTableEntry, Double>, TableCell<DebtResolveTableEntry, Double>>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                return new AmountCellFormatter(server, mainCtrl);
            }
        });
        tableEntries.clear();
        DebtResolve.resolve(this.event, server, mainCtrl)
                .stream()
                .map(DebtResolveTableEntry::fromResult)
                .forEach(tableEntries::add);
        System.out.println(this.tableEntries);
    }
}

class AmountCellFormatter extends TableCell<Object, Double> {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    AmountCellFormatter(ServerUtils server, MainCtrl main) {
        this.server = server;
        this.mainCtrl = main;
    }

    @Override
    protected void updateItem(Double e, boolean empty) {
        super.updateItem(e, empty);
        if (e == null || empty) {
            return;
        }

        setText(String.format("%.2f",server.convert(e, String.valueOf(User.Currency.EUR), String.valueOf(mainCtrl.getUser().getPrefferedCurrency()), Instant.now()))
                + " "
                + mainCtrl.getUser().getPrefferedCurrency()
        );
    }
}
