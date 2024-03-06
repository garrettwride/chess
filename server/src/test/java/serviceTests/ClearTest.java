package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.*;

import java.sql.SQLException;

public class ClearTest {

    private ApplicationService applicationService;

    @BeforeEach
    public void setUp() {
        // Initialize your ApplicationService instance here
        UserDataAccess userDataAccess = new UserDataAccess();
        GameDataAccess gameDataAccess = new GameDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        this.applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
    }

    @Test
    public void testClearSuccess() throws RegistrationException, SQLException, DataAccessException {
        // Given: Set up any preconditions necessary for the test

        // When: Call the method you want to test
        boolean success = applicationService.clear();

        // Then: Assert the expected result
        assertTrue(success, "Expected the clear method to return true");
    }
}

