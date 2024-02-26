package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;

public class ApplicationService {
    private final UserDataAccess userDataAccess;

    public ApplicationService(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public String clear(User newUser) throws RegistrationException {
        // Clear authTokens
        userDataAccess.addUser(newUser);


        // Serialize the new user to JSON
        Gson gson = new Gson();
        return gson.toJson(newUser);
    }
}
