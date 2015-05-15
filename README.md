# https://github.com/bordozer/totalizator

# install npm
sudo zypper in npm

# install bower
npm install -g bower

# clone remote repository
git clone https://github.com/bordozer/totalizator.git

# change directory to project
cd totalizator

# install dependencies
bower install

bower install https://github.com/harvesthq/chosen/releases/download/v1.4.0/chosen_v1.4.0.zip

# create database.properties from template
( 'test' profile does not use the properties, so it is not necessary to change it )

cp src/main/resources/database.properties.template src/main/resources/database.properties

# edit database properties
vim src/main/resources/database.properties

# create system.properties from template
cp src/main/resources/system.properties.template src/main/resources/system.properties

# edit system properties

	system.admin.ids:		system admins IDs separated by comma (should be edited after system run and admin user registration)
							system.admin.ids=1

 	system.logos.path: 		folder with teams' logo images
							system.logos.path=/home/user/totalizator-files/logos/

	system.imports.path:	folder with imported games' data
							system.imports.path=/home/user/totalizator-files/imports/

vim src/main/resources/system.properties

#run from command line
JAVA_HOME=/opt/java/jdk1.8.0_20 ; mvn clean install jetty:run -Djetty.port=9091 -Dspring.profiles.active=test

# in a browser
http://localhost:9091/resources/public/login.html

# Notes
Use with ID=1 ( system.admin.ids=1 in system.properties ) Hakeem Olajuwon ( login 'hakeem', password 'hakeem' ) is administrator. He has additional menu Administration.



# Development
IDE IDEA configuration (with JRebel)

jetty:run -Djetty.port=9091 -Dspring.profiles.active=development -Djavaagent:/home/blu/.IntelliJIdea13/config/plugins/jr-ide-idea/lib/jrebel/jrebel.jar


