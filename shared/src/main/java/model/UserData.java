package model;

import com.google.gson.Gson;

public record UserData(String username, String password) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

