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

import client.services.I18N;
import client.services.NotificationHelper;
import client.utils.Config;
import client.utils.User;
import commons.Event;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Locale;
import java.util.UUID;

public class MainCtrl {

    private  User user;
    public boolean admin;

    private Stage primaryStage;
    private FirstTimeCtrl firstTimeCtrl;

    private  Scene firstTime;

    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;

    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipant;


    private EditParticipantCtrl editParticipantCtrl;
    private Scene editParticipant;

    private StartCtrl startCtrl;
    private Scene start;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;
    private Scene inviteView;
    private InviteViewCtrl inviteViewCtrl;
    private UserSettingsCtrl userSettingsCtrl;
    private Scene userSettings;

    private SettingsCtrl settingsCtrl;
    private Scene settings;

    private LanguageCtrl languageCtrl;
    private Scene languages;

    private AdminEventsCtrl adminEventsCtrl;
    private Scene adminEvents;

    public void initialize(Stage primaryStage, Pair<FirstTimeCtrl, Parent> firstTime,
                           Pair<EventOverviewCtrl, Parent> eventOverview,
                           Pair<AddParticipantCtrl, Parent> addParticipant,
                           Pair<StartCtrl, Parent> start,
                           Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<InviteViewCtrl, Parent> inviteView,
                           Pair<EditParticipantCtrl, Parent> editParticipant,
                           Pair<UserSettingsCtrl, Parent> userSettings,
                           Pair<SettingsCtrl, Parent> settings,
                           Pair<AdminEventsCtrl, Parent> adminEvents,
                           Pair<LanguageCtrl, Parent> languages
    ) {
        this.admin=false;
        this.user = new User();
        this.primaryStage = primaryStage;

        this.firstTimeCtrl=firstTime.getKey();
        this.firstTime=new Scene(firstTime.getValue());

        this.eventOverviewCtrl=eventOverview.getKey();
        this.eventOverview= new Scene(eventOverview.getValue());

        this.startCtrl=start.getKey();
        this.start= new Scene(start.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        this.user = new User();

        //showStartScene();
        //primaryStage.show();
        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());

        this.editParticipantCtrl = editParticipant.getKey();
        this.editParticipant = new Scene(editParticipant.getValue());

        this.inviteViewCtrl=inviteView.getKey();
        this.inviteView=new Scene(inviteView.getValue());

        this.userSettingsCtrl=userSettings.getKey();
        this.userSettings=new Scene(userSettings.getValue());

        this.settingsCtrl=settings.getKey();
        this.settings=new Scene(settings.getValue());

        this.languageCtrl = languages.getKey();
        this.languages=new Scene(languages.getValue());

        this.adminEventsCtrl = adminEvents.getKey();
        this.adminEvents = new Scene(adminEvents.getValue());

        chooseFirstPage();
    }

    public void showInviteView(Event event){
        primaryStage.setTitle(I18N.get("window.invite"));
        inviteViewCtrl.setEvent(event);
        primaryStage.setScene(inviteView);
    }

    public void chooseFirstPage(){
        this.user=Config.readUserConfigFile();
        if (user == null) {
            this.showFirstTimeScene();
            primaryStage.show();
        }
        else {
            this.showStartScene();
            primaryStage.show();
        }
    }
    public void showFirstTimeScene(){
        primaryStage.setTitle(I18N.get("window.setup"));
        primaryStage.setScene(this.firstTime);
    }

    public void showStartScene() {
        primaryStage.setTitle(I18N.get("window.start"));
        startCtrl.addRecentEvents();
        primaryStage.setScene(start);
    }

    public  void showSettings(){
        primaryStage.setTitle(I18N.get("window.settings"));
        primaryStage.setScene(settings);
    }
    public void showLanguageOptions() {
        primaryStage.setTitle("Splitty: Languages");
        primaryStage.setScene(languages);
    }

    // TODO Make the actual switch to the languages once button is pressed
    public void switchToEnglish() {
        I18N.setLocale(Locale.ENGLISH);
        String switchLanguageHeader = I18N.get("language.infoLanguages");
        String switchLanguageTitle = I18N.get("language.switchTitle");
        String switchLanguageMessage = I18N.get("language.switchMessage");
        NotificationHelper notificationHelper = new NotificationHelper();
        notificationHelper.informUser(switchLanguageTitle, switchLanguageMessage, switchLanguageHeader);
    }

    public void switchToDutch() {
        Locale dutch = I18N.getSupportedLocales().get(1);
        I18N.setLocale(dutch);
        String switchLanguageHeader = I18N.get("language.infoLanguages");
        String switchLanguageTitle = I18N.get("language.switchTitle");
        String switchLanguageMessage = I18N.get("language.switchMessage");
        NotificationHelper notificationHelper = new NotificationHelper();
        notificationHelper.informUser(switchLanguageTitle, switchLanguageMessage, switchLanguageHeader);
    }

    public  void showUserSettings(){
        primaryStage.setTitle(I18N.get("window.settings.profile"));
        userSettingsCtrl.refreshFields();
        primaryStage.setScene(userSettings);

    }

    // TODO Both setEvent and refresh call the setEvent function
    public void showEventOverviewScene(Event newEvent){
        primaryStage.setTitle(I18N.get("window.event.overview"));
        eventOverviewCtrl.setEvent(newEvent);
        eventOverviewCtrl.refresh();
        primaryStage.setScene(eventOverview);
    }
    public void showAddParticipantScene(Event event) {
        primaryStage.setTitle(I18N.get("window.event.participant.add"));
        addParticipantCtrl.setEvent(event);
        primaryStage.setScene(addParticipant);
    }

    public void showEditParticipantScene(Event event, Participant p) {
        primaryStage.setTitle(I18N.get("window.event.participant.edit"));
        editParticipantCtrl.setEvent(event);
        editParticipantCtrl.setParticipant(p);
        editParticipantCtrl.refresh();
        primaryStage.setScene(editParticipant);
    }

    public void showAdminEventsScene() {
        primaryStage.setTitle(I18N.get("window.event.admin"));
        adminEventsCtrl.populateList();
        primaryStage.setScene(adminEvents);
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public User getUser(){
        return this.user;
    }

    public  void setUser(User user){
        this.user=user;
        Config.writeUserConfigFile(user);
    }

    public void addUserEvent(UUID event, UUID participant){
        this.user.addEventParticipant(event, participant);
        Config.writeUserConfigFile(user);
        System.out.println(Config.readUserConfigFile());
    }

    public void showAddExpense(){
        primaryStage.setTitle(I18N.get("window.expense"));
        addExpenseCtrl.setup(eventOverviewCtrl.getEvent());
        primaryStage.setScene(addExpense);
    }

    public void deleteAllData(){
        Config.deleteUserConfigFile();
        this.chooseFirstPage();
    }

    public void loginAdmin(){
        admin=true;
        showAdminEventsScene();
    }

    //hardcoded temporary exchange rates
    public double getUsdToEur(){
        return 0.92;
    }

    public double getRonToEur() {
        return 0.2;
    }
}