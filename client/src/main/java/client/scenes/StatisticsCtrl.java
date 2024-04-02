package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML public Label expenseLabel;
    private Event event;

    @FXML private PieChart pieStats;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
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
    }
}
