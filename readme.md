# roo-bootstrap

## Introduction

This is a Spring Roo plugin for including Twitter Bootstrap in your Roo projects.

Thanks go to [pmartinezalvarez](https://github.com/pmartinezalvarez) for providing the base project [roo-bootstrap](https://github.com/pmartinezalvarez/roo-bootstrap).

With this plugin you get Twitter Bootstrap 3.0 and jQuery 2.0.3.

## Usage

To run roo-bootstrap download or clone the repository and run "mvn package" to build the project. The file *de.eightbitboy.roo.bootstrap-0.2.0.jar* will be generated. Leave the file in the project folder or move it to a location of your choice.

To activate the pluging fire up roo and type the following command:

	osgi start --url file://[YOUR_FILE_PATH]/de.eightbitboy.roo.bootstrap-0.3.1.jar

For example at my machine it looks this (notice the *three* slashes!):

	osgi start --url  file:///C:/roo-bootstrap/de.eightbitboy.roo.bootstrap-0.3.1.jar

To install roo-bootstrap in your project type the following command:

	web mvc install bootstrap

Contact me if things are not clear!

## Related proojects

Roo-bootstrap uses [dbootstrap](https://github.com/thesociable/dbootstrap), [roostrap](https://github.com/bhagyas/roostrap) and [Font Awesome](http://fontawesome.io/).