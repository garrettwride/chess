package dataModels;

import com.google.gson.Gson;

public record User(String username, String password, String email) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public User withUsername(String username) {
        return new User(username, this.password, this.email);
    }

    public User withPassword(String password) {
        return new User(this.username, password, this.email);
    }

    public User withEmail(String email) {
        return new User(this.username, this.password, email);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

