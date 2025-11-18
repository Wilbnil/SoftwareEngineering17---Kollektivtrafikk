package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.repository.interfaces.I_UserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The {@code UserRepository} class handles all SQL-Queries to the database regarding the
 * "administrators" table in the userdatabase
 * <p>
 * The Connection entails a database connection and can for example be the connection
 * from the SQLiteDatabase class' startDB() method
 * </p>
 * Example usage:
 * <blockquote><pre>
 *     UserRepository userRepo = new UserRepository(connection);
 *     User returnedUser = userRepo.getById(1);
 * </pre></blockquote>
 *     Using an Interface to instantiate UserRepository can be beneficial to allow multiple
 *     different classes that implements the Repository Interface to be instantiated depending on which Repository you want to use
 * </p>
 * <blockquote><pre>
 *     I_UserRepo userRepo = new UserRepository(connection);
 *     User returnedUser = userRepo.getById(1);
 * </pre></blockquote>
 */

public class UserRepository implements I_UserRepo {

    private static Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get User by Id
     *
     * @param {int} id - User id
     * @return {User} - User object
     * @throws {Exception} If database connection fails
     */
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

            // Since last_login can be null, the .parse method will cause a NullPointerException
            // last_login is null. Therefore we check if last_login is null before assigning it to the .parse method
            LocalDate last_login;
            if(result.getString("last_login") == null) {
                last_login = null;
            } else {
                last_login = LocalDate.parse(result.getString("last_login"));
            }
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            User returnUser = new User(returnId, email, password, last_login, created_on);

            return returnUser;
    }

    /**
     * Get User by name
     *
     * @param {String} email - User email
     * @return {User} - User object
     * @throws {Exception} If database connection fails
     */
    @Override
    public User getByName(String email) throws Exception {
        String sql =
                "SELECT * FROM administrators " +
                "WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            result.next();
            int returnId = result.getInt("id");
            String returnEmail = result.getString("email");
            String password = result.getString("password");

            // Since last_login can be null, the .parse method will cause a NullPointerException when
            // last_login is null. Therefore we check if last_login is null before assigning it to the .parse method
            LocalDate last_login;
            if(result.getString("last_login") == null) {
                last_login = null;
            } else {
                last_login = LocalDate.parse(result.getString("last_login"));
            }
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            User returnUser = new User(returnId, email, password, last_login, created_on);

            return returnUser;
    }

    /**
     * Get all Users in database
     *
     * @return {ArrayList<User>} - Arraylist of all Users
     * @throws {Exception} If database connection fails
     */
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

            // Since last_login can be null, the .parse method will cause a NullPointerException when
            // last_login is null. Therefore we check if last_login is null before assigning it to the .parse method
            LocalDate last_login;
            if(result.getString("last_login") == null) {
                last_login = null;
            } else {
                last_login = LocalDate.parse(result.getString("last_login"));
            }
            LocalDate created_on = LocalDate.parse(result.getString("created_on"));
            returnUsers.add(new User(returnId, email, password, last_login, created_on));
        }

        return returnUsers;
    }

    /**
     * Insert User into database
     *
     * @param {User} user - User to insert
     * @throws {Exception} If database connection fails
     */
    @Override
    public void insert(User user) throws Exception {
        String sql =
                "INSERT INTO administrators (email, password, created_on) " +
                "VALUES (?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getCreated_on().toString());

        int rowsAdded = statement.executeUpdate();
        System.out.println(rowsAdded + " Rows added in administrators");
    }

    /**
     * Update User in database
     *
     * @param {User} user - User to update
     * @param {User} newUser - Updated User
     * @throws {Exception} If database connection fails
     */
    @Override
    public void update(User user, User newUser) throws Exception {
        String sql =
                "UPDATE administrators " +
                "SET email = ?, password = ?, last_login = ? " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getPassword());

        // Since last_login can be null, the .toString method will cause a NullPointerException when
        // last_login is null. Therefore we check if last_login is null before assigning it to the .toString method
        if(newUser.getLast_login() == null) {
                statement.setString(3, null);
            } else {
                statement.setString(3, newUser.getLast_login().toString());
            }
            // WHERE
            statement.setInt(4, user.getId());

            statement.executeUpdate();
            System.out.println("User " + user.getId() + " has been updated");
    }

    /**
     * Delete User in database
     *
     * @param {User} user - User for deletion
     * @throws {Exception} If database connection fails
     */
    @Override
    public void delete(User user) throws Exception {
        String sql =
                "DELETE FROM administrators " +
                "WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, user.getId());

            statement.executeUpdate();
            System.out.println("User " + user.getId() + " has been deleted");
    }
}
