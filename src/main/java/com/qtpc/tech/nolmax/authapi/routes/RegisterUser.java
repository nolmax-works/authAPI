package com.qtpc.tech.nolmax.authapi.routes;

import com.nolmax.database.database.UserDAO;
import com.nolmax.database.model.User;
import com.nolmax.database.util.PasswordUtils;
import com.qtpc.tech.nolmax.authapi.Main;
import com.qtpc.tech.nolmax.authapi.configuration.WebserverConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterUser {
    public static final Logger log = LoggerFactory.getLogger(RegisterUser.class);

    public static void processRoute(Context ctx) {
        try {
            if (Main.config.server.debug) log.info("Received a request from {}", ctx.ip());
            WebserverConfig.UserPassRequest registerData = ctx.bodyAsClass(WebserverConfig.UserPassRequest.class);
            UserDAO userDAO = new UserDAO();
            User user = new User();
            user.setUsername(registerData.username());
            user.setPasswordHash(PasswordUtils.hashPassword(registerData.password()));
            boolean result = userDAO.register(user);
            if (result) {
                ctx.status(200).result("Registered successful!");
            } else {
                ctx.status(400).result("Something happened!");
            }
        } catch (Exception e) {
            if (Main.config.server.debug) {
                log.error("An error has occurred!");
                log.error(e.getMessage());
                ctx.status(400);
            }
        }
    }
}
