package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;

public class ApplicationService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;

    public ApplicationService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess) {

        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public void clear() throws RegistrationException {
        // Clear authTokens
        userDataAccess.clear();
        gameDataAccess.clear();

    }
}
