package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;


//represents the users saved data which has credentials and other information for various sites
public class UserData implements Writable {
    private HashMap<String, HashMap<String, String>> dataMap;


    //EFFECTS: initializes a new hash map for userdata
    UserData() {
        dataMap = new HashMap<>();
    }

    UserData(HashMap<String, HashMap<String, String>> oldData) {
        dataMap = oldData;
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
        dataMap.put(site, new HashMap<>());
    }

    //MODIFIES: this
    //EFFECTS: adds data to a site in dataMap
    public void addData(String site, String key, String data) {
        if (!dataMap.containsKey(site)) {
            addSite(site);
        }
        dataMap.get(site).put(key, data);
    }

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
    public JSONObject toJson() {
        return new JSONObject(dataMap);
    }
}
