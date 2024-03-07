package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.UserDataAccess;
import org.junit.jupiter.api.Test;
import model.UserData;
import service.*;
import dataAccess.*;

public class RegistrationTests {

    private RegistrationService registrationService;

    RegistrationTests() {
        UserDataAccess userDataAccess = new UserDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
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

