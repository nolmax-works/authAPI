package com.qtpc.tech.nolmax.authapi.configuration;

import com.qtpc.tech.nolmax.authapi.Main;
import com.qtpc.tech.nolmax.authapi.routes.LoginRequestToken;
import com.qtpc.tech.nolmax.authapi.routes.RegisterUser;
import io.javalin.Javalin;

public class WebserverConfig {

    public record UserPassRequest(String username, String password) {}
    public record LoginRespose(String token) {}

    public static Javalin initializeConfig() {
        return Javalin.create(cfg -> {
            cfg.startup.showJavalinBanner = false; // hide javalin banner
            cfg.startup.showOldJavalinVersionWarning = false; // stop checking for updates
            cfg.jetty.host = Main.config.server.listen_address;
            if (Runtime.version().feature() >= 21) cfg.concurrency.useVirtualThreads = true; // use virtual threads on Java 21
            cfg.routes.get("/", ctx -> ctx.result("hello from nolmax!"));
            cfg.routes.post("/loginRequestToken", LoginRequestToken::processRoute);
            cfg.routes.post("/registerUser", RegisterUser::processRoute);
        });
    }
}
