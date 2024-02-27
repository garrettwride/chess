package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.UserDataAccess;
import org.junit.jupiter.api.Test;
import dataModels.User;
import service.*;
import dataAccess.*;

public class RegistrationTests {

    private RegistrationService registrationService;

    RegistrationTests() {
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        this.registrationService = new RegistrationService(userDataAccess);
    }

    @Test
    public void testRegistrationSuccess() {

        // Create a new user
        User newUser = new User("newuser", "password123", "newuser@example.com");

        // Call the register method and verify success
        assertDoesNotThrow(() -> registrationService.register(newUser));
    }

    @Test
    public void testRegistrationFailureUsernameTaken() throws RegistrationException {

        // Register an existing user
        User existingUser = new User("existinguser", "password123", "existing@example.com");
        registrationService.register(existingUser);

        // Attempt to register the same username again
        User newUser = new User("existinguser", "password123", "newuser@example.com");

        // Call the register method and verify that it fails
        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }


}

