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


//represents the users saved data which has credentials and other information for various sites
public class UserData implements Writable {
    private HashMap<String, HashMap<String, String>> dataMap;
    private boolean decrypted;
    private AES encryption;


    //EFFECTS: initializes a new hash map for userdata for new data
    UserData(String hash) throws NoSuchAlgorithmException {
        dataMap = new HashMap<>();
        decrypted = true;
        encryption = new AES(hash);
    }

    //EFFECTS: initializes a new hash map for userdata for saved data
    UserData(HashMap<String, HashMap<String, String>> oldData, String hash) throws NoSuchAlgorithmException {
        dataMap = oldData;
        decrypted = false;
        encryption = new AES(hash);
    }

    //EFFECTS: updates the AES key with the corresponding key value
    //MODIFIES: this
    public void updateKey(String hash) throws NoSuchAlgorithmException {
        encryption.updateKey(hash);
    }

    //EFFECTS: Encrypts all of the data in the dataMap via AES
    //MODIFIES: this
    public void encryptData() throws NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (decrypted) {
            for (String key: dataMap.keySet()) {
                for (String data: dataMap.get(key).keySet()) {
                    editData(key, data, encryption.encrypt(dataMap.get(key).get(data)));
                }
            }
            decrypted = false;
        }
    }

    //EFFECTS: Decrypts all of the data in the dataMap userdata via AES
    //MODIFIES: this
    public void decryptData() throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!decrypted) {
            for (String key: dataMap.keySet()) {
                for (String data: dataMap.get(key).keySet()) {
                    editData(key, data, encryption.decrypt(dataMap.get(key).get(data)));
                }
            }
            decrypted = true;
        }
    }

    //EFFECTS: Gets the HashMap for the data of a site
    public HashMap<String, String> getSiteMap(String site) {
        return dataMap.get(site);
    }

    //EFFECTS: returns sites in a user readable string
    public String[] getAllSites() {
        String[] sites = new String[dataMap.size()];
        int i = 0;
        for (String siteKey: dataMap.keySet()) {
            sites[i] = siteKey;
            i++;
        }
        return sites;
    }

    //MODIFIES: this
    //EFFECTS: adds site to dataMap
    public void addSite(String site) {
        if (!dataMap.containsKey(site)) {
            dataMap.put(site, new HashMap<>());
        }
    }

    //MODIFIES: this
    //EFFECTS: adds data to a site in dataMap
    public void addData(String site, String key, String data) {
        if (!dataMap.containsKey(site)) {
            addSite(site);
        }
        if (dataMap.get(site).containsKey(key)) {
            int i = 1;
            while (dataMap.get(key).containsKey(key + i)) {
                i++;
            }
            dataMap.get(site).put(key + i, data);
        } else {
            dataMap.get(site).put(key, data);
        }
    }

    //REQUIRES: key and site exist in hashmap
    //MODIFIES: this
    //EFFECTS: edits data in site from dataMap
    public void editData(String site, String key, String data) {
        dataMap.get(site).replace(key, data);
    }

    //MODIFIES: this
    //EFFECTS: removes site from dataMap
    public void removeSite(String site) {
        dataMap.remove(site);
    }

    //MODIFIES: this
    //EFFECTS: removes data from site in dataMap
    public void removeData(String site, String key) {
        dataMap.get(site).remove(key);
    }

    @Override
    public JSONObject toJson() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        encryptData();
        return new JSONObject(dataMap);
    }
}
