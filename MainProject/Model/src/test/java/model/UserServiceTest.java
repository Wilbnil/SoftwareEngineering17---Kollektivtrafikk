package model;

import org.gruppe17.kollektivtrafikk.model.User;
import org.gruppe17.kollektivtrafikk.repository.UserRepository;
import org.gruppe17.kollektivtrafikk.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testGetAllUsers_ReasonableValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        ArrayList<User> result = service.getAllUsers();

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    public void testGetAllUsers_UnexpectedValues_EmptyDatabase() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        fakeRepo.empty = true;
        UserService service = new UserService(fakeRepo);

        // Act
        ArrayList<User> result = service.getAllUsers();

        // Assert
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllUsers_UnexpectedValues_Exception() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        fakeRepo.throwError = true;
        UserService service = new UserService(fakeRepo);

        // Act
        ArrayList<User> result = service.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetUserById_ReasonableValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        User result = service.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("user@test.com", result.getEmail());
    }

    @Test
    public void testGetUserById_UnexpectedValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        User resultNonExistent = service.getUserById(999);

        // Assert
        assertNull(resultNonExistent);
    }

    @Test
    public void testGetUserByEmail_ReasonableValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        User result = service.getUserByEmail("user2@test.com");

        // Assert
        assertNotNull(result);
        assertEquals("user2@test.com", result.getEmail());
        assertEquals(2, result.getId());
    }

    @Test
    public void testGetUserByEmail_UnexpectedValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        User resultNonExistent = service.getUserByEmail("nonexistent@test.com");

        // Assert
        assertNull(resultNonExistent);
    }

    @Test
    public void testLogin_ReasonableValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        boolean resultCorrect = service.login("user3@test.com", "password67");
        boolean resultWrong = service.login("user3@test.com", "wrongpassword");

        // Assert
        assertTrue(resultCorrect);
        assertFalse(resultWrong);
    }

    @Test
    public void testLogin_UnexpectedValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        boolean resultNonExistent = service.login("nonexistent@test.com", "password");
        boolean resultEmpty = service.login("", "");

        // Assert
        assertFalse(resultNonExistent);
        assertFalse(resultEmpty);
    }

    @Test
    public void testFakeLogin_ReasonableValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        boolean result1 = service.fakeLogin("user@test.com", "password123");
        boolean result2 = service.fakeLogin("user3@test.com", "admin123");
        boolean result3 = service.fakeLogin("test@example.com", "test123");

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    public void testFakeLogin_UnexpectedValues() {
        // Arrange
        FakeUserRepository fakeRepo = new FakeUserRepository();
        UserService service = new UserService(fakeRepo);

        // Act
        boolean resultEmpty = service.fakeLogin("", "");
        boolean resultNull = service.fakeLogin(null, null);
        boolean resultWrong = service.fakeLogin("wrong@test.com", "wrongpassword");

        // Assert
        assertTrue(resultEmpty);
        assertTrue(resultNull);
        assertTrue(resultWrong);
    }

    // Fake repository
    class FakeUserRepository extends UserRepository {
        public boolean empty = false;
        public boolean throwError = false;

        FakeUserRepository() {
            super(null);
        }

        public ArrayList<User> getAll() throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return new ArrayList<>();
            }

            ArrayList<User> users = new ArrayList<>();
            users.add(new User(1, "user@test.com", "password123", null, LocalDate.now()));
            users.add(new User(2, "user2@test.com", "password456", null, LocalDate.now()));
            users.add(new User(3, "user3@test.com", "password67", null, LocalDate.now()));
            return users;
        }

        public User getById(int id) throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return null;
            }

            ArrayList<User> users = getAll();
            for (User user : users) {
                if (user.getId() == id) {
                    return user;
                }
            }
            return null;
        }

        public User getByName(String email) throws Exception {
            if (throwError) {
                throw new Exception("Error");
            }
            if (empty) {
                return null;
            }

            ArrayList<User> users = getAll();
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    return user;
                }
            }
            return null;
        }

        public void update(User oldUser, User newUser) throws Exception {
            // Fake update
        }
    }
}