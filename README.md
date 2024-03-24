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
start springboot and open the h2 web interface at localhost:8080/h2-console, past the setup_db.sql into the console and run it twice.

# cli args
to launch directly into admin page use <br>
``--admin=1``

# shortcuts
- ``ALT + 1`` - go back
- ``ALT + S`` - in home page -> go to settings page
