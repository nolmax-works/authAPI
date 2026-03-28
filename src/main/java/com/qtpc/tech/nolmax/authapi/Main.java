package com.qtpc.tech.nolmax.authapi;

import com.nolmax.database.config.DatabaseConfig;
import com.qtpc.tech.nolmax.authapi.configuration.AppConfig;
import com.qtpc.tech.nolmax.authapi.configuration.WebserverConfig;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static AppConfig config;
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Path configPath = AppConfig.getJarDirectory().resolve("config.yml");

        if (Files.notExists(configPath)) {
            AppConfig.createDefaultConfig(configPath);
            log.info("Created default config.yml at: {}", configPath.toAbsolutePath());
            log.info("Exiting the app so you can edit the file...");
            return;
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        config = mapper.readValue(configPath.toFile(), AppConfig.class);

        log.info("Connecting to Nolmax database at {}:{}", config.database.address, config.database.port);
        DatabaseConfig.initialize(config.database.address, config.database.port, config.database.db, config.database.username, config.database.password);

        log.info("Initializing webserver to listen at {}:{}", config.server.listen_address, config.server.port);
        Javalin app = WebserverConfig.initializeConfig();
        app.start(config.server.port);
    }
}