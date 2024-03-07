package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.UserDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.UserData;
import service.*;
import dataAccess.*;

import java.sql.Connection;

public class RegistrationTests {

    private RegistrationService registrationService;
    private Connection connection;
    private ApplicationService applicationService;
    @BeforeEach
    public void setUp() throws Exception {
        GameDataAccess gameDataAccess = new GameDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        UserDataAccess userDataAccess = new UserDataAccess();
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
        applicationService.clear();
    }

    @Test
    public void testRegistrationSuccess() {

        // Create a new user
        UserData newUser = new UserData("newuser", "password123", "test@email");

        // Call the register method and verify success
        assertDoesNotThrow(() -> registrationService.register(newUser));
    }

    @Test
    public void testRegistrationFailureUsernameTaken() throws RegistrationException, DataAccessException {

        // Register an existing user
        UserData existingUser = new UserData("existinguser", "password123", "test@email");
        registrationService.register(existingUser);

        // Attempt to register the same username again
        UserData newUser = new UserData("existinguser", "password123", "test@email");

        // Call the register method and verify that it fails
        assertThrows(IllegalStateException.class, () -> registrationService.register(newUser));
    }


}

