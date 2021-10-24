package model;

import org.json.JSONObject;
import persistence.Writable;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


//Represents a user account which has the login information, their saved data and a unique AES cipher for their data
public class UserAccount implements Writable {

    private final LoginInformation userCred;
    private UserData dataMap;
    private final AES cipher;

    //EFFECTS: initialize a new UserAccount with a new Hashmap and LoginInformation
    public UserAccount(String name, String pswd) throws NoSuchAlgorithmException {
        userCred = new LoginInformation(name, pswd);
        dataMap = new UserData();
        cipher = new AES();
    }

    public UserAccount(String name, String pswd, byte[] savedHashing,
                       HashMap<String, HashMap<String, String>> data, AES savedCipher) throws NoSuchAlgorithmException {
        userCred = new LoginInformation(name, pswd, savedHashing);
        dataMap = new UserData(data);
        cipher = new AES();
    }

    public UserAccount(LoginInformation info, HashMap<String,
            HashMap<String, String>> data, AES savedCipher) throws NoSuchAlgorithmException {
        userCred = info;
        dataMap = new UserData(data);
        cipher = new AES();
    }

    //EFFECTS: returns true if credentials given match credentials saved, false otherwise
    public boolean checkLoginCreds(String username, String password) {
        return userCred.checkValues(username, password);
    }

    //EFFECTS: returns entire dataMap in a user readable string
    public String allDataToString() throws Exception {
        StringBuilder retVal = new StringBuilder();
        HashMap<String, String> currentSite;
        for (String site : dataMap.getAllSites()) {
            retVal.append(site).append('\n');
            currentSite = dataMap.getSiteMap(site);
            for (String currKey : currentSite.keySet()) {
                retVal.append('\t').append(currKey).append(": ");
                retVal.append(currentSite.get(currKey)).append('\n');
            }
        }
        return retVal.toString();
    }

    //EFFECTS: returns all sites in a user readable string
    public String sitesToString() {
        StringBuilder retVal = new StringBuilder();
        for (String siteKey: dataMap.getAllSites()) {
            retVal.append(siteKey).append('\n');
        }
        return retVal.toString();
    }

    //EFFECTS: returns data of a specific site in a user readable string
    public String siteDataToString(String site) throws Exception {
        StringBuilder retVal = new StringBuilder();
        HashMap<String, String> data = dataMap.getSiteMap(site);
        for (String key: data.keySet()) {
            retVal.append(key).append(": ").append(data.get(key)).append('\n');
        }
        return retVal.toString();
    }


    //MODIFIES: this
    //EFFECTS: adds site to dataMap
    public void addSite(String site) {
        dataMap.addSite(site);
    }

    //MODIFIES: this
    //EFFECTS: adds data to a site in dataMap
    public void addData(String site, String key, String data) throws Exception {
        dataMap.addData(site, key, data);
    }

    //MODIFIES: this
    //EFFECTS: edits data in site from dataMap
    public void editData(String site, String key, String data) throws Exception {
        dataMap.editData(site, key, data);
    }

    //MODIFIES: this
    //EFFECTS: removes site from dataMap
    public void removeSite(String site) {
        dataMap.removeSite(site);
    }

    //MODIFIES: this
    //EFFECTS: removes data from site in dataMap
    public void removeData(String site, String key) {
        dataMap.removeData(site, key);
    }


    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("LoginInfo", userCred.toJson());
        obj.put("data", dataMap.toJson());
        obj.put("AES", cipher.toJson());
        return obj;
    }
}
