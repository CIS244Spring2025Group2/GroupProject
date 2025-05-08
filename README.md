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

* **Java:** Ensure you have Java 17 or a later version installed on your system. You can download it from [your preferred Java distribution (e.g., Oracle Java, OpenJDK)](your-java-download-link-here).
* **JavaFX:** This project requires JavaFX 17 or above. If you are using a Java Development Kit (JDK) that does not bundle JavaFX (like recent versions of Oracle JDK), you will need to install it separately. You can find installation instructions on the [OpenJFX website](https://openjfx.io/openjfx-docs/).
* **MySQL Connector/J:** This project uses MySQL as its database. You will need to include the MySQL Connector/J driver as a dependency in your project build (e.g., via Maven or Gradle). You can find the dependency information on [Maven Repository](https://mvnrepository.com/artifact/mysql/mysql-connector-java).
* **Docker (Optional):** If you choose to use the Docker database setup, you will need to have Docker installed on your system. You can find installation instructions here: [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/)
* **Docker Compose (Optional):** The Docker database setup also requires Docker Compose. You can find installation instructions here: [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
* **Running MySQL on Docker:** If you want to run MySQL on docker, create a file named compose.yaml and add the following content:

```compose.yaml
services:
  mysql:
    image: mysql:5.7
    command: --default-authentication-plugin=mysql_native_password --character-set-server=utf8
    restart: always
    environment:
      MYSQL_DATABASE: [enter your database name]
      MYSQL_ROOT_PASSWORD: [enter your password]
    ports:
      - 127.0.0.1:3306:3306

```

### Configuration

Before running the application, you need to configure the database connection and default admin user details in the `config.properties` file. This file should be located in the root directory of the project (or specify the correct path if it's elsewhere).

Open the `config.properties` file and fill in the following values:

```properties
## Database connection details ##
# add your database connection details
db.type=mysql
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://[IPAddress:port]/[databaseName]?user=[databaseUser]&password=[databasePassword]&useSSL=false


## Admin User Details ##
# Default admin user details can be added below
default.admin.email=test@test.com
default.admin.password=test
default.admin.securityQuestion=What's your father's middle name?
default.admin.securityAnswer=test