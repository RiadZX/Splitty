# OOPP Splitty Project

Splitty.

# running the project
Make sure to have the server running before you run the client.

running  the server 

`./gradlew bootRun`

running the client

`./gradlew run`

# live updating
## websockets
websockets are used in the event overview page.

## longpolling
used in the admin overview table.


# Extensions
## Foreign currency
## Statistics page
## custom colored tags

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