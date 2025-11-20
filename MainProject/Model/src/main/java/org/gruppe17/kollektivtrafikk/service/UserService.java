package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The {@code UserService} class handles all the logic related stuff for the bus stops in the public transport system.
 *
 * <p>
 * This service class provides methods like retrieving, adding, updating and deleting users.
 * It includes as well a method that handles user login.
 * This class interacts with {@code UserRepository} in order to do its database operations.
 * </p>
 *
 * <p>
 * Only admins are able to do certain stuff like adding, updating and deleting users.
 * </p>
 *
 * <p>
 * Example usage:
 * <blockquote><pre>
 * User user = userService.getUserById(1);
 * boolean loginSuccess = userService.login("admin@example.com", "password123");
 * </pre></blockquote>
 * </p>
 *
 * <p>
 * {@code UserService} should be instantiated with {@code UserRepository}.
 * </p>
 */
public class UserService {
    private UserRepository userRepository;

    /**
     * Creates a new userService with UserRepository.
     *
     * @param userRepository The repository used.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets all users from the database.
     *
     * @return An ArrayList with all the users or an empty ArrayList if it fails
     */
    public ArrayList<User> getAllUsers() {
        try {
            // Gets all users from the repository
            return userRepository.getAll();
        } catch (Exception e) {
            // Catches in case anything goes wrong
            System.err.println(e.getMessage());
            // Returns an empty ArrayList if unable to connect to the repository
            return new ArrayList<>();
        }
    }

    /**
     * Gets a user by its id
     *
     * @param id The identifier of the user
     * @return The user if found, or null otherwise
     */
    public User getUserById(int id) {
        try {
            return userRepository.getById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a user by its email.
     *
     * @param email The name of the email
     * @return The user if found, or null otherwise
     */
    public User getUserByEmail(String email) {
        try {
            return userRepository.getByName(email);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The User that is going to be added
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void addUser(User user, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to add users.");
        }

        user.setCreated_on(LocalDate.now());

        // Checks if the email already exists
        User existingUser = userRepository.getByName(user.getEmail());
        if (existingUser != null) {
            throw new Exception("Email already exists.");
        }
        userRepository.insert(user);
    }

    /**
     * Updates a user in the database.
     *
     * @param oldUser The old user that is going to be updated
     * @param newUser The new user that gets its new updated information
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void updateUser(User oldUser, User newUser, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to update users.");
        }
        userRepository.update(oldUser, newUser);
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The User that is going to be deleted
     * @param isAdmin Checks if the user is an admin
     * @throws Exception if the user is not an admin
     */
    public void deleteUser(User user, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to delete users.");
        }
        userRepository.delete(user);
    }

    /**
     * Verify a user by their email and password, then updates their last login.
     * <p>
     * If the info is correct, then it will update your last login.
     * </p>
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return true if the verification is successful, false otherwise
     */
    public boolean login(String email, String password) {
        try {
            User user = userRepository.getByName(email);

            if (user.getPassword().equals(password)) {
                // Creates a new User with an updated last_login
                User updatedUser = new User(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        java.time.LocalDate.now(),
                        user.getCreated_on()
                );
                userRepository.update(user, updatedUser);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Fake login that always returns true, used for testing.
     *
     * @param email Any email
     * @param password Any password
     * @return Returns true always
     */
    public boolean fakeLogin(String email, String password) {
        return true;
    }
}