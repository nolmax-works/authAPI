# nolmax's authAPI
Simple "REST-like" HTTP API that is responsible for account creation and token requests for nolmax.

Designed to be used in conjunction with [nolmax's main server](https://github.com/nolmax-works/server).

# Usage
- Download the JAR executable file as `authAPI.jar`. Then run `java -jar authAPI.jar`.
- On first run, the application will spawn a `config.yml` file next to the JAR executable and exit. Make sure to edit the configuration file to include the necessary server configuration and credentials.
- Start the server again, and the app will be online at the listening address and the listening port of your choice. By default, this is `0.0.0.0:63636`.

# Startup arguments
There are startup arguments that override their respective options in the configuration file when specified:
```
--config <path> -> to specify custom path for the configuration file
--server.port <port> -> to specify custom port for the server
--server.listen_address <listen address> -> to specify custom listening address for the server
--server.debug <true/false> -> to specify whether debug mode is enabled or not
--database.address <database address> -> to specify the address of the PostgreSQL server
--database.port <database port> -> to specify the port of the PostgreSQL server
--database.username <database username> -> to specify the username of the PostgreSQL server
--database.password <database password> -> to specify the password of the PostgreSQL server
--database.db <database name> -> to specify the name of the database containing nolmax's schema
```

# Requirements
- Java 21 or later for virtual threads support in Javalin.

# Build
Ensure that JDK 21 or later is installed. Then, simply run `gradlew.bat build` if you're on Windows, or `./gradlew build` on *nix, or `gradle build` when you have Gradle installed in your development machine.