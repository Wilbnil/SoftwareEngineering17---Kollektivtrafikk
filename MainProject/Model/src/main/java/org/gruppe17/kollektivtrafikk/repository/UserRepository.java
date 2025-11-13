package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_UserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserRepository implements I_UserRepo {

    private static Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getById(int id) throws Exception {
        String sql =
                "SELECT * FROM administrators " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            result.next();
            int returnId = result.getInt("id");
            String email = result.getString("email");
            String password = result.getString("password");
            LocalDate last_login = LocalDate.parse(result.getString("last_login"));
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            User returnUser = new User(returnId, email, password, last_login, created_on);

            return returnUser;
    }

    @Override
    public User getByName(String name) throws Exception {
        String sql =
                "SELECT * FROM administrators " +
                "WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();

            result.next();
            int returnId = result.getInt("id");
            String email = result.getString("email");
            String password = result.getString("password");
            LocalDate last_login = LocalDate.parse(result.getString("last_login"));
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            User returnUser = new User(returnId, email, password, last_login, created_on);

            return returnUser;
    }

    @Override
    public ArrayList<User> getAll() throws Exception {
        String sql = "SELECT * FROM administrators;";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        ArrayList<User> returnUsers = new ArrayList<>();

        while(result.next()) {
            int returnId = result.getInt("id");
            String email = result.getString("email");
            String password = result.getString("password");
            LocalDate last_login = LocalDate.parse(result.getString("last_login"));
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            returnUsers.add(new User(returnId, email, password, last_login, created_on));
        }

        return returnUsers;
    }

    @Override
    public void insert(User object) throws Exception {
        String sql =
                "INSERT INTO administrators (email, password, created_on) " +
                "VALUES (?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, object.getEmail());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getCreated_on().toString());

        int rowsAdded = statement.executeUpdate();
        System.out.println(rowsAdded + " Rows added in administrators");
    }

    @Override
    public void update(User object, User newObject) throws Exception {
        String sql =
                "UPDATE administrators " +
                "SET email = ?, password = ?, last_login = ? " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, newObject.getEmail());
            statement.setString(2, newObject.getPassword());
            statement.setString(3, newObject.getLast_login().toString());
            // WHERE
            statement.setInt(4, object.getId());

            statement.executeUpdate();
            System.out.println("User " + object.getId() + " has been updated");
    }

    @Override
    public void delete(User object) throws Exception {
        String sql =
                "DELETE FROM administrators " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, object.getId());

            statement.executeUpdate();
            System.out.println("User " + object.getId() + " has been deleted");
    }
}
