# OOPP Template Project

This repository contains the template for the OOPP project. Please extend this README.md with instructions on how to run your project.

# adding a language controlled button/label/something with text
 - Add the item in the fxml
 - make sure it has a id with the fx:id fxml property
 - make the text in the object the language item (something like "event.error")
 - make sure the item is in the class with the @FXML
 - call I18N.update() with the label as argument
 - add the language item in the .propery files

