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
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static AppConfig config;
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Map<String, String> cliArgs = parseArgs(args);

        Path configPath = cliArgs.containsKey("config") ? Path.of(cliArgs.get("config")) : AppConfig.getJarDirectory().resolve("config.yml");

        if (Files.notExists(configPath)) {
            AppConfig.createDefaultConfig(configPath);
            log.info("Created default config.yml at: {}", configPath.toAbsolutePath());
            log.info("Exiting the app so you can edit the file...");
            return;
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        config = mapper.readValue(configPath.toFile(), AppConfig.class);
        applyOverrides(cliArgs);

        log.info("Connecting to Nolmax database at {}:{}", config.database.address, config.database.port);
        DatabaseConfig.initialize(config.database.address, config.database.port, config.database.db, config.database.username, config.database.password);

        log.info("Initializing webserver to listen at {}:{}", config.server.listen_address, config.server.port);
        Javalin app = WebserverConfig.initializeConfig();
        app.start(config.server.port);
    }

    private static void applyOverrides(Map<String, String> cliArgs) {
        if (config == null) return;

        if (cliArgs.containsKey("database.address")) config.database.address = cliArgs.get("database.address");
        if (cliArgs.containsKey("database.port")) config.database.port = Integer.parseInt(cliArgs.get("database.port"));
        if (cliArgs.containsKey("database.username")) config.database.username = cliArgs.get("database.username");
        if (cliArgs.containsKey("database.password")) config.database.password = cliArgs.get("database.password");
        if (cliArgs.containsKey("database.db")) config.database.db = cliArgs.get("database.db");

        if (cliArgs.containsKey("server.listen_address"))
            config.server.listen_address = cliArgs.get("server.listen_address");
        if (cliArgs.containsKey("server.port")) config.server.port = Integer.parseInt(cliArgs.get("server.port"));
        if (cliArgs.containsKey("server.debug"))
            config.server.debug = Boolean.parseBoolean(cliArgs.get("server.debug"));
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> result = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (!arg.startsWith("--")) continue;

            String key;
            String value;

            if (arg.contains("=")) {
                String[] split = arg.substring(2).split("=", 2);
                key = split[0];
                value = split.length > 1 ? split[1] : "true";
            } else {
                key = arg.substring(2);
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    value = args[++i];
                } else {
                    value = "true";
                }
            }

            result.put(key, value);
        }

        return result;
    }
}