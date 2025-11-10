package org.gruppe17.kollektivtrafikk;

import io.javalin.Javalin;

public class FrontEndControllerAdmin {

    public static void register(Javalin app) {
        // Serve admin page
        app.get("/admin", context -> context.redirect("admin.html"));
    }
}
