# Sprout, a Plant Tracker Application

A JavaFX application designed to help you effectively track the care of your plants. Keep your green friends happy and healthy by managing their watering, fertilizing, repotting schedules, and more.

## Table of Contents

- [Description](#description)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
- [User Roles and Privileges](#user-roles-and-privileges)
- [Key Features](#key-features)
- [Usage](#usage)
  - [Viewing and Managing Plants](#viewing-and-managing-plants)
  - [Managing Reminders](#managing-reminders)
  - [Home Screen Overview](#home-screen-overview)
  - [User Account Management](#user-account-management)
- [Contributing](#contributing)
- [License](#license)

## Description

The Plant Tracker application provides a user-friendly interface to manage plant care. It allows you to keep track of your plants, schedule reminders for essential tasks, and monitor their care history.

### Prerequisites

* **Java:** Ensure you have Java 17 or a later version installed on your system. You can download it from [OpenJDK Downloads](https://www.openlogic.com/openjdk-downloads) or your prefered distributor.
* **JavaFX:** This project requires JavaFX 17 or above. If you are using a Java Development Kit (JDK) that does not bundle JavaFX (like recent versions of Oracle JDK), you will need to install it separately. You can find installation instructions on the [Gluon](https://gluonhq.com/products/javafx/) or your prefered distributor.
* **MySQL Database:** This project connects to a MySQL database. You need to run a database locally and provide the connection details in the `config.properties` file, and the app will generate the necessary tables.

### Optional Prerequisites
* **Docker (Optional):** If you choose to use the Docker database setup, you will need to have Docker installed on your system. You can find installation instructions here: [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/)
* **Docker Compose (Optional):** The Docker database setup also requires Docker Compose. You can find installation instructions here: [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
* **Running MySQL on Docker:** If you want to run MySQL on docker, create a file named compose.yaml with the following contents:

Change the database name and password if you wish.

```compose.yaml
services:
  mysql:
    image: mysql:5.7
    command: --default-authentication-plugin=mysql_native_password --character-set-server=utf8
    restart: always
    environment:
      MYSQL_DATABASE: group2test
      MYSQL_ROOT_PASSWORD: test
    ports:
      - 127.0.0.1:3306:3306

```

### Configuration

Before running the application, you may configure the database connection and default admin user details in the `config.properties` file. This file should be located in the root directory of `SproutPlantTracker.jar`.

1. Extract the `config.properties` file
2. Replace the database connection or admin values in the properties file
3. Repackage the .jar with the new properties file

```properties
## Database connection details ##
# add your database connection details
db.type=mysql
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://127.0.0.1:3306/group2test?user=root&password=test&useSSL=false


## Admin User Details ##
# Default admin user details can be added below
default.admin.email=test@test.com
default.admin.password=test
default.admin.securityQuestion=What's your father's middle name?
default.admin.securityAnswer=test
```

### Directions for Running
Run `SproutPlantTracker.jar` with the following command

```
java --module-path [path-to-your-javafx/lib] --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.web,javafx.graphics -jar SproutPlantTracker.jar
```
Replce `[path-to-your-javafx/lib]` with the path to your `javafx/lib` folder.