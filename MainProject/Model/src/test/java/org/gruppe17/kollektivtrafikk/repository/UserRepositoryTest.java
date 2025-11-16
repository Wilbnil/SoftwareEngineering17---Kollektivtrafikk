package org.gruppe17.kollektivtrafikk.repository;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.testDB.H2TestDatabase;
import org.gruppe17.kollektivtrafikk.testDB.TestDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserRepositoryTest {
    private final static TestDatabase testDB = new H2TestDatabase();

    private static Connection connection;

    // Creates a connection and calls for the creation of all the tables in the test database
    @BeforeAll
    public static void setUpTestDB() throws Exception{
        // Starts the test database
        connection = testDB.startDB();

        // Creates the tables
        testDB.createTables();

        // Allows the test database to roll back data
        connection.setAutoCommit(false);
    }

    // Inserts the dummy data for testing
    @BeforeEach
    public void prepareTest() throws Exception{
        testDB.createDummyData();
    }

    // Rolls back the data after the test as to not hinder other tests from working correctly
    @AfterEach
    public void cleanUpTest() throws Exception{
        connection.rollback();
        // This method is important since the AUTO_INCREMENT will not reset in between test without it, causing foreign key constraints
        testDB.updateAutoIncrement();
    }

    // Stops the test database
    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    @Test
    @DisplayName("User returned successfully from id")
    public void userGetById_UserReturnedSuccessfully() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        User idealUser = new User(1, "anton@publictransport.com", null, LocalDate.parse("2025-10-30"), LocalDate.parse("2025-10-30"));

        // Act
        User returnedUser = userRepo.getById(1);

        // Assert
        Assertions.assertEquals(idealUser.getEmail(), returnedUser.getEmail());
        Assertions.assertEquals(null, returnedUser.getPassword());
        Assertions.assertEquals(LocalDate.of(2025, 10, 30), returnedUser.getCreated_on());
    }

    @Test
    @DisplayName("User returned successfully from name (email)")
    public void userGetByName_UserReturnedSuccessfully() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        User idealUser = new User(2, "worker@publictransport.com", null, null, LocalDate.parse("2025-10-30"));

        // Act
        User returnedUser = userRepo.getByName("worker@publictransport.com");

        // Assert
        Assertions.assertEquals(idealUser.getId(), returnedUser.getId());
        Assertions.assertEquals(null, returnedUser.getPassword());
        Assertions.assertEquals(null, returnedUser.getLast_login());
        Assertions.assertEquals(LocalDate.of(2025, 10, 30), returnedUser.getCreated_on());

    }

    @Test
    @DisplayName("All users returned successfully")
    public void userGetAll_UsersReturnedSuccessfully() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        ArrayList<User> returnedUsers;

        // Act
        returnedUsers = userRepo.getAll();

        // Assert
        Assertions.assertEquals(3, returnedUsers.size());
        Assertions.assertEquals(1, returnedUsers.getFirst().getId());
        Assertions.assertEquals(3, returnedUsers.getLast().getId());
        Assertions.assertEquals(null, returnedUsers.get(1).getLast_login());
    }

    @Test
    @DisplayName("User inserted successfully")
    public void userInsert_UserInsertedCorrectly() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        User newUser = new User("insertedUser@gmail.com", "12345", null, LocalDate.of(2025, 11, 15));

        // Act
        userRepo.insert(newUser);

        // Assert
        Assertions.assertEquals(4, testDB.countRowsInTable("administrators"));
        Assertions.assertEquals(4, testDB.getIdFromEmail("insertedUser@gmail.com"));
        Assertions.assertEquals(null, testDB.getLastLoginFromId(4));
    }

    @Test
    @DisplayName("User updated successfully")
    public void userUpdate_UserUpdatedCorrectly() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        User oldUser = new User(3, "withPassword@publictransport.com", "password", null, LocalDate.of(2020,03, 01));
        User newUser = new User(3, "updatedUser@gmail.com", "updatedpassword", null, LocalDate.of(2020, 03, 01));

        // Act
        userRepo.update(oldUser, newUser);

        // Assert
        Assertions.assertEquals("updatedUser@gmail.com", testDB.getEmailFromId(3));
        Assertions.assertEquals("updatedpassword", testDB.getPasswordFromId(3));
    }

    @Test
    @DisplayName("User deleted successfully")
    public void userDelete_UserDeletedCorrectly() throws Exception {
        // Arrange
        UserRepository userRepo = new UserRepository(connection);

        User deleteUser = new User(1, "anton@publictransport.com", null, LocalDate.of(2025, 10, 30), LocalDate.of(2025, 10, 30));

        // Act
        userRepo.delete(deleteUser);

        // Assert
        Assertions.assertEquals(2, testDB.countRowsInTable("administrators"));
    }
}
