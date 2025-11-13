package org.gruppe17.kollektivtrafikk.service;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.repository.UserRepository;

import java.util.ArrayList;

public class UserService {
    private UserRepository userRepository;

    // Accepts a UserRepository when a UserService object is created
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Returns all users from the database
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

    // Returns a user by their ID
    public User getUserById(int id) {
        try {
            return userRepository.getById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // Returns a user by their email
    public User getUserByEmail(String email) {
        try {
            return userRepository.getByName(email);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // Adds a user to the database (checks if the email already exists first)
    public void addUser(User user, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to add users.");
        }
        // Checks if the email already exists
        User existingUser = userRepository.getByName(user.getEmail());
        if (existingUser != null) {
            throw new Exception("Email already exists.");
        }
        userRepository.insert(user);
    }

    // Updates a user in the database
    public void updateUser(User oldUser, User newUser, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to update users.");
        }
        userRepository.update(oldUser, newUser);
    }

    // Deletes a user from the database
    public void deleteUser(User user, boolean isAdmin) throws Exception {
        if (!isAdmin) {
            throw new Exception("You do not have access to delete users.");
        }
        userRepository.delete(user);
    }

    // Login method that checks if email and password is correct, then updates last_login
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

    // Fake login method that always returns true (Used for testing)
    public boolean fakeLogin(String email, String password) {
        return true;
    }
}