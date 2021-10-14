package model;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PasswordManager {

    private ArrayList<UserAccount> accounts;
    private UserAccount currUser;

    //MODIFIES: this
    //EFFECTS: initialize pmanager with a new ArrayList and current user set to null
    //REQUIRES:
    public PasswordManager() {
        accounts = new ArrayList<>();
        currUser = null;
    }

    //MODIFIES:
    //EFFECTS: returns false if there's no user being served, true if a user is logged in
    //REQUIRES:
    public boolean isLoggedIn() {
        return currUser != null;
    }

    //MODIFIES: currUser
    //EFFECTS: adds a new site hashmap to the current user's account
    //REQUIRES:
    public void addSite(String site) {
        currUser.addSite(site);
    }

    //MODIFIES: currUser
    //EFFECTS: adds new info to a site in user account
    //REQUIRES:
    public void addInfo(String site, String key, String info) throws Exception {
        currUser.addData(site, key, info);
    }

    //MODIFIES: currUser
    //EFFECTS: edits existing site data with newData
    //REQUIRES:
    public void editData(String site, String key, String newData) throws Exception {
        currUser.editData(site, key, newData);
    }

    //MODIFIES: currUser
    //EFFECTS: removes data from a site in a user account
    //REQUIRES:
    public void removeData(String site, String key) {
        currUser.removeData(site, key);
    }

    //MODIFIES: currUser
    //EFFECTS: removes a site, and it's data from a user account
    //REQUIRES:
    public void removeSite(String site) {
        currUser.removeSite(site);
    }

    //MODIFIES: this
    //EFFECTS: adds a user to the user accounts arraylist
    //REQUIRES:
    public void addUser(String username, String password) throws NoSuchAlgorithmException {
        accounts.add(new UserAccount(username, password));
    }

    //MODIFIES: this
    //EFFECTS: returns user exists and matches given login, if yes set them as current user
    //REQUIRES:
    public boolean checkLogin(String username, String password) {
        for (UserAccount user: accounts) {
            if (user.checkLoginCreds(username, password) != -1) {
                currUser = user;
                return true;
            }
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: sets current user to null when they log out
    //REQUIRES:
    public void userLoggedOut() {
        currUser = null;
    }

    //MODIFIES:
    //EFFECTS: returns user readable string of all sites and their data
    //REQUIRES:
    public String displayAllInfo() throws Exception {
        return currUser.allDataToString();
    }

    //MODIFIES:
    //EFFECTS: returns only the sites that are saved in a user readable string
    //REQUIRES:
    public String displaySites() {
        return currUser.sitesToString();
    }

    //MODIFIES:
    //EFFECTS: returns all info of a site in a user readable string
    //REQUIRES:
    public String displayInfo(String site) throws Exception {
        return currUser.siteDataToString(site);
    }
}
