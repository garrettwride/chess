package server;

import com.google.gson.Gson;
import dataAccess.DataMemory;
import dataAccess.UserDataAccess;
import dataModels.User;
import service.RegistrationException;
import spark.*;
import service.RegistrationService;

public class Server {

    private RegistrationService registrationService;

    public Server(){
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        registrationService = new RegistrationService(userDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define routes
        Spark.post("/user", this::handleRegistration);

        Spark.awaitInitialization();

        return desiredPort;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) throws RegistrationException {
        var user = new Gson().fromJson(request.body(), User.class);

        return registrationService.register(user);
    }
}
