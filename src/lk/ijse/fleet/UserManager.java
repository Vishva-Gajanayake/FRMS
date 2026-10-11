
package lk.ijse.fleet;

import java.util.ArrayList;

public class UserManager {
    
    private ArrayList<SystemUser> users = new ArrayList<>();

    public UserManager() {
    }

    public UserManager(ArrayList<SystemUser> users) {
        this.users = users;
    }

    public ArrayList<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(SystemUser user) {
        
        if(user != null){
            this.users.add(user);
            System.out.println("User added sucsessfully..!");
        }
    }
    
    public SystemUser registerUser(String username, String password, String fullName){
        String un = findByUsername(username);
        
        if (un == null) {
            System.out.println("Username already taken");
            return null;
        }
        SystemUser user = new SystemUser(username, password, fullName);
        
        return user;
    }
    
    public SystemUser login(String username, String password){
        
        for (SystemUser user : users) {
            if (user.getUsername().equals(username) & user.getPassword().equals(password)) {
                return user;
            }
        }
        
        return null;
    }
    
    public String findByUsername(String username){
        
        for (SystemUser user : users) {
            if (user.getUsername().equals(username)) {
                return username = null;
            }
        }
        return username;
    }
}
