package dataModels;

public record AuthData(String username, String authToken) {

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
