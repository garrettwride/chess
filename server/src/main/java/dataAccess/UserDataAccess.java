package dataAccess;

import model.UserData;

public class UserDataAccess {
    private final DataMemory dataMemory;

    public UserDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new user
    public void addUser(UserData user) {
        dataMemory.addUser(user);
    }

    // Method to retrieve a user by username
    public UserData getUser(String username) {
        return dataMemory.getUser(username);
    }

    public void clear() {
        dataMemory.clearUsers();
    }

}
