package serviceTests;

import dataAccess.AuthDataAccess;
import dataAccess.DataMemory;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.*;

public class ClearTest {

    private ApplicationService applicationService;

    @BeforeEach
    public void setUp() {
        // Initialize your ApplicationService instance here
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
    }

    @Test
    public void testClearSuccess() throws RegistrationException {
        // Given: Set up any preconditions necessary for the test

        // When: Call the method you want to test
        boolean success = applicationService.clear();

        // Then: Assert the expected result
        assertTrue(success, "Expected the clear method to return true");
    }
}

