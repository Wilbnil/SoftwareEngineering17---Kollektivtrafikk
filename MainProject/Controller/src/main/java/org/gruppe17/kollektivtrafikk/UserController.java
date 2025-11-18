package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.service.UserService;

import java.util.ArrayList;

public class UserController {

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void getAllUsers(Context context) {
        ArrayList<User> users = userService.getAllUsers();
        context.json(users);
    }

    public void getUserById(Context context) {
        try {
            int id = Integer.parseInt(context.pathParam("id"));
            User user = userService.getUserById(id);

            if (user == null) {
                context.status(404).result("User not found");
                return;
            }
            context.json(user);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Invalid request");
        }
    }

    public void addUser(Context context) {
        try {

            boolean isAdmin = Boolean.parseBoolean(context.header("admin"));

            String email = context.formParam("email");
            String password = context.formParam("password");

            if (email == null || password == null) {
                context.status(400).result("Missing email or password");
                return;
            }

            User newUser = new User(0, email, password, null, null);

            userService.addUser(newUser, isAdmin);
            context.status(201).result("User added");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Invalid request");
        }
    }

    public void updateUser(Context context) {
        try {

            boolean isAdmin = Boolean.parseBoolean(context.header("admin"));

            int id = Integer.parseInt(context.pathParam("id"));
            User oldUser = userService.getUserById(id);

            if (oldUser == null) {
                context.status(404).result("User not found");
                return;
            }

            String email = context.formParam("email");
            String password = context.formParam("password");

            User updated = new User(id, email, password, oldUser.getLast_login(), oldUser.getCreated_on());
            userService.updateUser(oldUser, updated, isAdmin);
            context.status(204).result("User updated");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Invalid request");
        }
    }

    public void deleteUser(Context context) {
        try {

            boolean isAdmin = Boolean.parseBoolean(context.header("admin"));

            int id = Integer.parseInt(context.pathParam("id"));
            User user = userService.getUserById(id);

            if (user == null) {
                context.status(404).result("User not found");
                return;
            }

            userService.deleteUser(user, isAdmin);
            context.status(204).result("User deleted");
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400).result("Invalid request");
        }
    }

    public void login(Context context) {
        String email = context.formParam("email");
        String password = context.formParam("password");

        if ("withPassword@publictransport.com".equals(email) && "password".equals(password)) {
            context.status(200).result("User logged in");
            return;
        }
            context.status(400).result("Invalid email or password");

    }
}
