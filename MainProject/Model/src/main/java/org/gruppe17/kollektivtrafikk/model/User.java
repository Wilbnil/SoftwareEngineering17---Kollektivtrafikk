package org.gruppe17.kollektivtrafikk.model;

import java.time.LocalDate;

public class User {
    private int id;
    private String email;
    private String password;
    private LocalDate last_login;
    private LocalDate created_on;

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
}
