/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl quoteoverviewCtrl;
    private Scene quoteoverview;

    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private StartCtrl startCtrl;
    private Scene start;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> quoteoverview,
            Pair<AddQuoteCtrl, Parent> add, Pair<EventOverviewCtrl, Parent> eventOverview, Pair<StartCtrl, Parent> start, Pair<AddExpenseCtrl, Parent> addExpense) {
        this.primaryStage = primaryStage;

        this.quoteoverviewCtrl = quoteoverview.getKey();
        this.quoteoverview = new Scene(quoteoverview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.eventOverviewCtrl=eventOverview.getKey();
        this.eventOverview= new Scene(eventOverview.getValue());

        this.startCtrl=start.getKey();
        this.start= new Scene(start.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        showStart();
        primaryStage.show();
    }

    public void showStart() {
        primaryStage.setTitle("Splitty: Start");
        startCtrl.addRecentEvents();
        primaryStage.setScene(start);
    }
    public  void showEventOverview(Event newEvent){
        primaryStage.setTitle("Splitty: Event Overview");
        eventOverviewCtrl.setEvent(newEvent);
        primaryStage.setScene(eventOverview);

    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showAddExpense(){
        primaryStage.setTitle("Splitty: Add/Edit Expense");
        addExpenseCtrl.setup();
        primaryStage.setScene(addExpense);
    }
}