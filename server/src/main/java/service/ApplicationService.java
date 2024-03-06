package service;

import dataAccess.*;

import java.sql.SQLException;

public class ApplicationService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ApplicationService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {

        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public boolean clear() throws RegistrationException, DataAccessException, SQLException {
        // Clear
        userDataAccess.clear();
        gameDataAccess.clear();
        authDataAccess.clear();

        return true;
    }
}
