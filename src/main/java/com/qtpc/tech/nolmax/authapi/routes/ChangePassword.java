package com.qtpc.tech.nolmax.authapi.routes;

import com.nolmax.database.database.UserDAO;
import com.qtpc.tech.nolmax.authapi.Main;
import com.qtpc.tech.nolmax.authapi.configuration.WebserverConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangePassword {
    public static final Logger log = LoggerFactory.getLogger(ChangePassword.class);

    public static void processRoute(Context ctx) {
        try {
            if (Main.config.server.debug) log.info("Received a request from {}", ctx.ip());
            WebserverConfig.ChangePasswordRequest changePasswordData = ctx.bodyAsClass(WebserverConfig.ChangePasswordRequest.class);
            UserDAO userDAO = new UserDAO();
            boolean changePassword = userDAO.changePassword(changePasswordData.username(), changePasswordData.oldPassword(), changePasswordData.newPassword());
            if (Main.config.server.debug) log.info("Received password change request for username {}", changePasswordData.username());
            if (changePassword) {
                ctx.status(200).result("Password changed successfully!");
                if (Main.config.server.debug) log.info("Password changed successfully for username {}", changePasswordData.username());
            } else {
                ctx.status(403).result("Error occurred! Invalid username or credentials!");
                if (Main.config.server.debug) log.info("Refusing token authentication for username {}", changePasswordData.username());
            }
        } catch (Exception e) {
            ctx.status(400);
            if (Main.config.server.debug) {
                log.error("An error has occurred!");
                log.error(e.getMessage());
            }
        }
    }
}