package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//Represents a password manager that has multiple accounts that have their own data
public class PasswordManager implements Writable, DataOperations {

    private ArrayList<UserAccount> accounts;
    private UserAccount currUser;

    //MODIFIES: this
    //EFFECTS: initialize PasswordManager with a new ArrayList and current user set to null
    public PasswordManager() {
        accounts = new ArrayList<>();
        currUser = null;
    }

    //MODIFIES: this
    //EFFECTS: adds a new user to the accounts
    public void addUser(UserAccount user) {
        accounts.add(user);
    }

    //MODIFIES: this
    //EFFECTS: adds a user to the user accounts arraylist
    public void addUser(String username, String password) throws NoSuchAlgorithmException {
        accounts.add(new UserAccount(username, password, true));
    }

    //EFFECTS: returns false if there's no user being served, true if a user is logged in
    public boolean isLoggedIn() {
        return currUser != null;
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: adds a new site hashmap to the current user's account
    @Override
    public void addSite(String site) {
        currUser.addSite(site);
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: adds new info to a site in user account
    @Override
    public void addData(String site, String key, String info) {
        currUser.addData(site, key, info);
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: edits existing site data with newData
    @Override
    public void editData(String site, String key, String newData) {
        currUser.editData(site, key, newData);
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: removes data from a site in a user account
    @Override
    public void removeData(String site, String key) {
        currUser.removeData(site, key);
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: removes a site, and it's data from a user account
    @Override
    public void removeSite(String site) {
        currUser.removeSite(site);
    }

    //MODIFIES: this
    //EFFECTS: returns user exists and matches given login, if yes set them as current user
    public boolean checkLogin(String username, String password) throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        for (UserAccount user: accounts) {
            if (user.checkLoginCreds(username, password)) {
                currUser = user;
                currUser.updateData(username + password);
                return true;
            }
        }
        return false;
    }

    public String[] getSites() {
        return currUser.getSites();
    }

    public String[] getData(String site) {
        return currUser.getData(site);
    }

    public String userStringList() {
        StringBuilder retVal = new StringBuilder();
        for (UserAccount acc: accounts) {
            retVal.append(acc.getCredsString()).append('\n');
        }
        return retVal.toString();
    }

    //REQUIRES: user has logged in
    //MODIFIES: this
    //EFFECTS: sets current user to null when they log out
    public void userLoggedOut() throws Exception {
        currUser.closeData();
        currUser = null;
    }

    //REQUIRES: user has logged in
    //EFFECTS: returns user readable string of all sites and their data
    public String displayAllInfo() {
        return currUser.allDataToString();
    }

    //REQUIRES: user has logged in
    //EFFECTS: returns only the sites that are saved in a user readable string
    public String displaySites() {
        return currUser.sitesToString();
    }

    //REQUIRES: user has logged in
    //EFFECTS: returns all info of a site in a user readable string
    public String displayInfo(String site) {
        return currUser.siteDataToString(site);
    }

    //EFFECTS: returns the number of accounts saved
    public int userCount() {
        return accounts.size();
    }

    //EFFECTS: returns a JSON array representation of the accounts
    private JSONArray accountsToJson() throws NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        JSONArray arr = new JSONArray();
        for (UserAccount acc: accounts) {
            arr.put(acc.toJson());
        }
        return arr;
    }

    //EFFECTS: returns a json objects of the accounts in password manager
    @Override
    public JSONObject toJson() throws NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new JSONObject().put("accounts", accountsToJson());
    }
}
