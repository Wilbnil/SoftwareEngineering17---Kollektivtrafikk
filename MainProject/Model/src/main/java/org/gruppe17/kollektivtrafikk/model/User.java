package org.gruppe17.kollektivtrafikk.model;

import java.time.LocalDate;

/**
 * The {@code User} class will model information about administrator users that need to login into the system
 * to get access to special functionality, such as updating, deleting and creating new routes.
 *<p>
 *</p>
 * The class handles the same variables as the columns in the user database (with name brukerdatabase).
 * A unique identifier is used which is generated int the database.
 * Email and password is also modeled, but the password is not handled correctly in the MVP.
 * The dates for when the account was created and last logged in on is also stored, with the {@code LocalDate} class.
 */

public class User {
    // Variables
    private int id;
    private String email;
    private String password;
    private LocalDate last_login;
    private LocalDate created_on;

    // Constructors
    public User(int id, String email, String password, LocalDate last_login, LocalDate created_on) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.last_login = last_login;
        this.created_on = created_on;
    }

    public User(String email, String password, LocalDate created_on) {
        this.email = email;
        this.password = password;
        this.created_on = created_on;
    }

    public User(String email, String password, LocalDate last_login, LocalDate created_on) {
        this.email = email;
        this.password = password;
        this.last_login = last_login;
        this.created_on = created_on;
    }

    // Normal get and set methods.
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getLast_login() {
        return last_login;
    }

    public LocalDate getCreated_on() {
        return created_on;
    }

    public void setCreated_on(LocalDate created_on) {
        this.created_on = created_on;
    }
}
