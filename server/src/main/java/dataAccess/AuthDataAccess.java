package dataAccess;

import dataModels.AuthData;
import java.util.UUID;

public class AuthDataAccess {
    private final DataMemory dataMemory;

    public AuthDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new authToken for a user
    public void addAuthToken(String username) {
        String authToken = createAuthToken();
        AuthData authData = new AuthData(username, authToken);
        dataMemory.addAuthToken(authData);
    }

    // Method to retrieve authToken by username
    public String getAuthToken(String username) {
        AuthData authData = dataMemory.getAuthToken(username);
        return authData != null ? authData.getAuthToken() : null;
    }

    public AuthData getAuthData(String username) {
        AuthData authData = dataMemory.getAuthToken(username);
        return authData;
    }
    public String getUsername(String authToken){
        return dataMemory.getUsernameByAuthToken(authToken);
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
