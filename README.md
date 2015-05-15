# Github link
https://github.com/bordozer/totalizator

# Installation

install npm

$ **sudo zypper in npm**

install bower

$ **npm install -g bower**

clone remote repository

$ **git clone https://github.com/bordozer/totalizator.git**

change directory to project

$ **cd totalizator**

install dependencies

$ **bower install**

$ **bower install https://github.com/harvesthq/chosen/releases/download/v1.4.0/chosen_v1.4.0.zip**

# Configuration

There is two properties files

- database.properties

	create file from template

	$ **cp src/main/resources/database.properties.template src/main/resources/database.properties**

	'*test*' profile uses a virtual DB, so it is not necessary to change it. The file is used for *development* profile, when not virtual DB is used.

- system.properties

	$ **cp src/main/resources/system.properties.template src/main/resources/system.properties**

	edit system properties

	$ **vim src/main/resources/system.properties**

	- system.admin.ids: system admins IDs separated by comma (for clear DB should be edited after system run and admin user registration, not necessary to change it if '*test*' profile used)

		*system.admin.ids=1*

 	- system.logos.path: folder with teams' logo images

		*system.logos.path=/home/user/totalizator-files/logos/*

	- system.imports.path: folder with imported games' data

		*system.imports.path=/home/user/totalizator-files/imports/*

# Run

run from command line
- change *9091* to your free port
- define path to your Java 8 instead of */opt/java/jdk1.8.0_20*

$ **JAVA_HOME=*/opt/java/jdk1.8.0_20* ; mvn clean install jetty:run -Djetty.port=*9091* -Dspring.profiles.active=*test***

# Browser

in a browser

**http://localhost:*9091*/resources/public/login.html**

# Notes
*test* profile automatically generates random data - users and games.

Use with ID=1 ( system.admin.ids=1 in system.properties ) Hakeem Olajuwon ( login 'hakeem', password 'hakeem' ) is administrator. He has additional menu Administration where cups and games are configured.



# Development
IDE IDEA configuration (with JRebel)

jetty:run -Djetty.port=*9091* -Dspring.profiles.active=development -Djavaagent:/home/blu/.IntelliJIdea13/config/plugins/jr-ide-idea/lib/jrebel/jrebel.jar


