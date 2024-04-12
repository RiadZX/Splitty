# OOPP Template Project

This repository contains the template for the OOPP project. Please extend this README.md with instructions on how to run your project.

# adding a language controlled button/label/something with text
 - Add the item in the fxml
 - make sure it has a id with the fx:id fxml property
 - make the text in the object the language item (something like "event.error")
 - make sure the item is in the class with the @FXML
 - call I18N.update() with the label as argument
 - add the language item in the .propery files

# first time setup
no setup needed. If something breaks, then most likely there has been a change in the userconfig class, to fix delete the build folders in both client and server to make sure everything works correctly.

# cli args
to launch directly into admin page use <br>
``--admin=1``

# shortcuts
to view all shortcuts, refer to the button in the main menu.
ALT + H to open it quickly (in main menu)
