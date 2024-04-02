package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML public Label expenseLabel;
    @FXML public TableColumn<StatsRow, String> tName;
    @FXML public TableColumn<StatsRow, Double> tIncoming;
    @FXML public TableColumn<StatsRow, Double> tOutgoing;
    @FXML public TableView<StatsRow> tableView;
    private Event event;

    @FXML private PieChart pieStats;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tIncoming.setCellValueFactory(new PropertyValueFactory<>("incoming"));
        tOutgoing.setCellValueFactory(new PropertyValueFactory<>("outgoing"));
    }

    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public void updatePieChart(){
        pieStats.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Double> stats = new HashMap<>();
        //for each expense get the tags and add the amount to the corresponding tag
        for (Expense e : event.getExpenses()){
            for (Tag t : e.getTags()){
                stats.put(t.getTag(), stats.getOrDefault(t.getTag(), 0.0) + e.getAmount());
            }
        }
        for (Map.Entry<String, Double> entry : stats.entrySet()){
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieStats.setData(pieChartData);
    }
    public void setParticipantStats(){
        tableView.getItems().clear();
        ObservableList<StatsRow> data = FXCollections.observableArrayList();
        for (Participant p : event.getParticipants()){
            double incoming = calculateIncoming(p);
            double outgoing = -20;
            data.add(new StatsRow(p.getName(), incoming, outgoing));
        }
        tableView.setItems(data);
    }
    public double calculateIncoming(Participant p){
        double incoming = 0;
        for (Expense e : event.getExpenses()){
            if (!e.getPaidBy().getId().equals(p.getId())){
                continue;
            }
            List<Debt> debts = e.getDebts();
            for (Debt d : debts) {
                if (d.isPaid()) {
                    continue;
                }
                incoming += d.getAmount();
            }
        }
        return incoming;
    }

    public void setSumOfExpenses(){
        double sum = 0;
        for (Expense e : event.getExpenses()){
            sum += e.getAmount();
        }
        expenseLabel.setText("Total expenses: " + sum);
    }

    public void back(){
        mainCtrl.showEventOverviewScene(event);
    }

    public void refresh() {
        updatePieChart();
        setSumOfExpenses();
        setParticipantStats();
    }
    public class StatsRow {
        private final String name;
        private final double incoming;
        private final double outgoing;

        public StatsRow(String name, double incoming, double outgoing) {
            this.name = name;
            this.incoming = incoming;
            this.outgoing = outgoing;
        }

        public String getName() {
            return name;
        }

        public double getIncoming() {
            return incoming;
        }

        public double getOutgoing() {
            return outgoing;
        }
    }
}

