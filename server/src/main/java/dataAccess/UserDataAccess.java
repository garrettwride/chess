package dataAccess;

import dataModels.User;

public class UserDataAccess {
    private final DataMemory dataMemory;

    public UserDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new user
    public void addUser(User user) {
        dataMemory.addUser(user);
    }

    // Method to retrieve a user by username
    public User getUser(String username) {
        return dataMemory.getUser(username);
    }

}
