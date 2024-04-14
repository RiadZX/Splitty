# OOPP Splitty Project

Splitty.

# running the project
Make sure to have the server running before you run the client.

userconfig file
```toml
address = "http://localhost"
websocket = "ws://localhost"
port = 8080

[mailConfig]
    username = "xxx@gmail.com"
    password = "xxxx"
    host = "smtp.gmail.com"
    port = 587
    smtpAuth = true
    startTls = true
```
You can configure the file to add your email details. You can edit the 'address' which is the http url of the server.
websocket is the websocket url for the server.

running  the server 

`./gradlew bootRun`

running the client

`./gradlew run`

# live updating
## websockets
websockets are used in the event overview page. Websockets live sync all aspects of an event: expenses, participants, name changes etc.

## longpolling
used in the admin overview table. long polling is used when creating an event, updating an event or deleting it.


# Extensions
## Foreign currency
We have full support for foreign currencies including caching on the server. User can choose their preferred currency whenever they want.
## Statistics page
We have the three colored standard tags as well as ability to create new custom colored tags. 

In the statistics page you can view a piechart with a distribution of tags across all expenses. The piechart colors are based on the tag colors. The piechart contains absolute and relative values shown in your preferred currency.

You can view the sum of all expenses. 

You can view the tags in the expense overview in the event overview page.

There is a dedicated scene for managing tags.
## Live language switch
You can view the current language on the starting page and the eventoverview. You can click on the flag to change the language, very intuitive.

The preferred language gets saved to the config file. You can download the template via the settings page and fill it in to send it to the admins.

The app out of the box supports English, Dutch and Romanian.

## Detailed expenses
You can specify the date for an expense.

You can decide to eplit equally or within a subgroup.

You can filter expenses, per participant, on either from or to.

When clicking on an expense you can view more details about it.
## Email notification
You can use the app without email config, the buttons would be grayed out. email configuration is fully client side.

The user can configure their gmail credentials in the config file.
There is a test button in the invite scene, to see if it worked or not.
Using the invite scene you acn invite participants. They are automatically added if they join using a client. 

You can easily add/edit participants via the event overview page.

To send a paymnent reminder to a participant, click on the participant and click send reminder.



# HCI features
## Multi-modal visualization
Some buttons are colored. For example cancellation buttons are colored red, while confirming/accepting buttons are green.

Some buttons are an icon for easier use, like a settings cog or an icon of a person with a plus sign , resembling add new participant.

## confirmation popups
When doing an irreversible action, we ask for confirmation from the user, just in case.

## information popups
Whenever the user does something that needs some information, they get a popup with info. For example changing an event title would popup a window telling you if it is successful or a failure.

If a user enters invalid information they get informed.

## shortcuts
On the main screen, press the shortcuts button or use the shortcut `Alt + H` to view all available shortcuts and where.

## navigation
Navigation is logical and consistent througout the application and accross different scenes. All scenes have a back button, so you can go back to the last viewed scene.

Using the shortcuts you can create events, add expenses, change the title and more all without touching the mouse.

shortcuts, logical navigation

# CLI args
to launch directly into admin page use <br>
``--admin=1``

You can access the admin page via the app itself as well