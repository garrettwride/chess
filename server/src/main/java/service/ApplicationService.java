package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;

public class ApplicationService {
    private final UserDataAccess userDataAccess;

    public ApplicationService(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void clear() throws RegistrationException {
        // Clear authTokens
        userDataAccess.clear();

    }
}
