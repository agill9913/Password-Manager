package model;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//Represents a password manager that has multiple accounts that have their own data
public class PasswordManager {

    private ArrayList<UserAccount> accounts;
    private UserAccount currUser;

    //MODIFIES: this
    //EFFECTS: initialize PasswordManager with a new ArrayList and current user set to null
    public PasswordManager() {
        accounts = new ArrayList<>();
        currUser = null;
    }

    //EFFECTS: returns false if there's no user being served, true if a user is logged in
    public boolean isLoggedIn() {
        return currUser != null;
    }

    //MODIFIES: this
    //EFFECTS: adds a new site hashmap to the current user's account
    public void addSite(String site) {
        currUser.addSite(site);
    }

    //MODIFIES: this
    //EFFECTS: adds new info to a site in user account
    public void addInfo(String site, String key, String info) throws Exception {
        currUser.addData(site, key, info);
    }

    //MODIFIES: this
    //EFFECTS: edits existing site data with newData
    public void editData(String site, String key, String newData) throws Exception {
        currUser.editData(site, key, newData);
    }

    //MODIFIES: this
    //EFFECTS: removes data from a site in a user account
    public void removeData(String site, String key) {
        currUser.removeData(site, key);
    }

    //MODIFIES: this
    //EFFECTS: removes a site, and it's data from a user account
    public void removeSite(String site) {
        currUser.removeSite(site);
    }

    //MODIFIES: this
    //EFFECTS: adds a user to the user accounts arraylist
    public void addUser(String username, String password) throws NoSuchAlgorithmException {
        accounts.add(new UserAccount(username, password));
    }

    //MODIFIES: this
    //EFFECTS: returns user exists and matches given login, if yes set them as current user
    public boolean checkLogin(String username, String password) {
        for (UserAccount user: accounts) {
            if (user.checkLoginCreds(username, password)) {
                currUser = user;
                return true;
            }
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: sets current user to null when they log out
    public void userLoggedOut() {
        currUser = null;
    }

    //EFFECTS: returns user readable string of all sites and their data
    public String displayAllInfo() throws Exception {
        return currUser.allDataToString();
    }

    //EFFECTS: returns only the sites that are saved in a user readable string
    public String displaySites() {
        return currUser.sitesToString();
    }

    //EFFECTS: returns all info of a site in a user readable string
    public String displayInfo(String site) throws Exception {
        return currUser.siteDataToString(site);
    }
}
