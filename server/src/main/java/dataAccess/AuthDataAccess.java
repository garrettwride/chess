package dataAccess;

import model.AuthData;
import java.util.UUID;

public class AuthDataAccess {
    private final DataMemory dataMemory;

    public AuthDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new authToken for a user
    public String addAuthToken(String username) {
        String authToken = createAuthToken();
        AuthData authData = new AuthData(username, authToken);
        dataMemory.addAuthToken(authData);
        return authToken;
    }

    // Method to retrieve authToken by username
    public String getAuthToken(String authToken) {
        AuthData authData = dataMemory.getAuthToken(authToken);
        return authData != null ? authData.getAuthToken() : null;
    }

    public AuthData getAuthData(String authToken) {
        AuthData authData = dataMemory.getAuthToken(authToken);
        return authData;
    }
    public String getUsername(String authToken){
        return dataMemory.getUsername(authToken);
    }

     // Method to delete authToken by username
    public void deleteAuthToken(String username) {
        dataMemory.deleteAuthToken(username);
    }

    // Method to clear authTokens
    public void clear(){
        dataMemory.clearAuthTokens();
    }

    // Method to create a new authToken
    private String createAuthToken() {
        return UUID.randomUUID().toString();
    }
}
