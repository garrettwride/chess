package model;

import com.google.gson.Gson;

public record UserData(String username, String password, String email) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UserData withUsername(String Username) {
        return new UserData(username, this.password, this.email);
    }

    public UserData withPassword(String password) {
        return new UserData(this.username, password, this.email);
    }

    public UserData withEmail(String email) {
        return new UserData(this.username, this.password, email);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

