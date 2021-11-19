package model;

import org.json.JSONObject;
import persistence.Writable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


//Represents a user account which has the login information, their saved data and a unique AES cipher for their data
public class UserAccount implements Writable, DataOperations {

    private final LoginInformation userCred;
    private UserData dataMap;

    //EFFECTS: initialize a new UserAccount with a new Hashmap and LoginInformation for new users
    public UserAccount(String name, String pswd, boolean isNew) throws NoSuchAlgorithmException {
        userCred = new LoginInformation(name, pswd, isNew);
        dataMap = new UserData(name + pswd);
    }

    //EFFECTS: initialize a new UserAccount with a new Hashmap and LoginInformation from saved parsed json data
    public UserAccount(LoginInformation info, HashMap<String, HashMap<String, String>> data)
            throws NoSuchAlgorithmException {
        userCred = info;
        dataMap = new UserData(data, info.getHash());
    }

    //EFFECTS: Returns a string representation of user credentials
    public String getCredsString() {
        return userCred.toString();
    }

    //EFFECTS: Returns all the sites saved
    public String[] getSites() {
        return dataMap.getAllSites();
    }

    //EFFECTS: Returns all data associated with a site
    public String[] getData(String site) {
        return dataMap.getData(site);
    }

    //EFFECTS: Updates the AES key and decrypts the data with said key
    //MODIFIES: this
    public void updateData(String hash) throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        dataMap.updateKey(hash);
        dataMap.decryptData();
    }

    //EFFECTS: encrypts the user's data
    //MODIFIES: this
    public void closeData() throws Exception {
        dataMap.encryptData();
    }

    //EFFECTS: returns true if credentials given match credentials saved, false otherwise
    public boolean checkLoginCreds(String username, String password) {
        return userCred.checkValues(username, password);
    }

    //EFFECTS: returns entire dataMap in a user readable string
    public String allDataToString() {
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
    public String siteDataToString(String site) {
        StringBuilder retVal = new StringBuilder();
        HashMap<String, String> data = dataMap.getSiteMap(site);
        for (String key: data.keySet()) {
            retVal.append(key).append(": ").append(data.get(key)).append('\n');
        }
        return retVal.toString();
    }


    //MODIFIES: this
    //EFFECTS: adds site to dataMap
    @Override
    public void addSite(String site) {
        dataMap.addSite(site);
    }

    //MODIFIES: this
    //EFFECTS: adds data to a site in dataMap
    @Override
    public void addData(String site, String key, String data) {
        dataMap.addData(site, key, data);
    }

    //MODIFIES: this
    //EFFECTS: edits data in site from dataMap
    @Override
    public void editData(String site, String key, String data) {
        dataMap.editData(site, key, data);
    }

    //MODIFIES: this
    //EFFECTS: removes site from dataMap
    @Override
    public void removeSite(String site) {
        dataMap.removeSite(site);
    }

    //MODIFIES: this
    //EFFECTS: removes data from site in dataMap
    @Override
    public void removeData(String site, String key) {
        dataMap.removeData(site, key);
    }

    //EFFECTS: returns a new json object representation of this
    @Override
    public JSONObject toJson() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        JSONObject obj = new JSONObject();
        obj.put("LoginInfo", userCred.toJson());
        obj.put("data", dataMap.toJson());
        return obj;
    }
}
