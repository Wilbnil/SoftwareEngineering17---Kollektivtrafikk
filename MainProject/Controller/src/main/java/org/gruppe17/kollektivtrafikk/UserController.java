package org.gruppe17.kollektivtrafikk;

import io.javalin.http.Context;
import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.service.UserService;

import java.util.ArrayList;


/**
 * The {@code UserController} class handles all HTTP endpoints related user management.
 * It acts as the middle layer between the HTTP request and the {@link UserService},
 * providing methods for creating, retrieving, updating and deleting user accounts.
 *
 * <p>Thos controller supports:</p>
 * <li>Fetching all users</li>
 * <li>Fetching a user by ID</li>
 * <li>Adding a new user</li>
 * <li>Updating an existing user</li>
 * <li>Deleting a user</li>
 * <li>Logging in (simple validation only)</li>
 *
 * Correct Usage Example:
 * <blockquote><pre>
 *     app.get("/api/users", userController::getAllUsers);
 *     app.post("'api/users", userController::addUser);
 * </pre></blockquote>
 *
 * Incorrect Usage Example:
 * <blockquote><pre>
 *     userController.addUser(null);
 * </pre></blockquote>
 *
 * This controller does not implement authentication or cookie/session
 * management. Admin permissions are simulated using an HTTP header {@code admin=true}.
 */
public class UserController {

    private UserService userService;

    /**
     * Creates a new {@code UserController} instance with the required service dependency
     *
     * @param userService service used to perform user-related operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Return all users as JSON.
     *
     * @param context Javalin HTTP context
     */
    public void getAllUsers(Context context) {
        ArrayList<User> users = userService.getAllUsers();
        context.json(users);
    }

    /**
     * Return a user by ID.
     * If the user does not exist, return HTTP 404.
     *
     * @param context Javalin request context containing a path parameter {@code id}
     */
    public void getUserById(Context context) {
        try {
            int id = Integer.parseInt(context.queryParam("id"));
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

    /**
     * Adds a new user using form parameters {@code email} and {@code password}
     *
     * Admin right are checked using the HTTP header:
     * admin: true
     *
     *
     * @param context Javalin HTTP context
     */
    public void addUser(Context context) {
        try {

            boolean isAdmin = true;//Boolean.parseBoolean(context.header("admin"));

            String email = context.queryParam("email");
            String password = context.queryParam("password");

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

    /**
     * Updates an existing user.
     * If the user does not exist, HTTP 404 is returned.
     *
     * <p>Admin rights are required and are passed via {@code admin} header</p>
     *
     * @param context Javalin request context
     */

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

    /**
     * Deletes a user by ID.
     * If the user does not exist, it returns HTTP 404.
     * <p>Admin rights are required via {@code admin} header</p>
     *
     * @param context Javalin HTTP context
     */

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

    /**
     * A simplified login method used onlu for demonstration.
     *
     * <p>This method does not use hashing, sessions.
     * It simply checks for one hardcoded email/password combination.</p>
     *
     * @param context Javalin HTTP context
     */

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
