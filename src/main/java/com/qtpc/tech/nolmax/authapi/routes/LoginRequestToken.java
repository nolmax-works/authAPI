package com.qtpc.tech.nolmax.authapi.routes;

import com.nolmax.database.database.UserDAO;
import com.qtpc.tech.nolmax.authapi.Main;
import com.qtpc.tech.nolmax.authapi.configuration.WebserverConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginRequestToken {
    public static final Logger log = LoggerFactory.getLogger(LoginRequestToken.class);

    public static void processRoute(Context ctx) {
        try {
            if (Main.config.server.debug) log.info("Received a request from {}", ctx.ip());
            WebserverConfig.UserPassRequest loginData = ctx.bodyAsClass(WebserverConfig.UserPassRequest.class);
            UserDAO userDAO = new UserDAO();
            String token = userDAO.requestToken(loginData.username(), loginData.password());
            if (Main.config.server.debug) log.info("Received username is {}", loginData.username()); // no i wont reveal password 😂
            if (token != null) {
                ctx.status(200).json(new WebserverConfig.LoginRespose(token));
                if (Main.config.server.debug) log.info("Database provided a valid (non-null) token!");
            } else {
                ctx.status(403).result("Access denied! Invalid credentials!");
                if (Main.config.server.debug) log.info("Refusing token authentication because of invalid credentials");
            }
        } catch (Exception e) {
            ctx.status(400);
            if (Main.config.server.debug) {
                log.error("An error has occured!");
                log.error(e.getMessage());
            }
        }
    }
}
