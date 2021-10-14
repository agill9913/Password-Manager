package model;

import java.util.HashMap;

public class UserData {
    private HashMap<String, HashMap<String, byte[]>> dataMap;

    UserData() {
        dataMap = new HashMap<>();
    }

    public HashMap<String, byte[]> getSiteMap(String site) {
        return dataMap.get(site);
    }

    //MODIFIES:
    //EFFECTS: returns sites in a user readable string
    //REQUIRES:
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
    //REQUIRES:
    public void addSite(String site) {
        dataMap.put(site, new HashMap<>());
    }

    //MODIFIES: this
    //EFFECTS: adds data to a site in dataMap
    //REQUIRES:
    public void addData(String site, String key, byte[] data) {
        if (dataMap.isEmpty() || !dataMap.containsKey(site)) {
            addSite(site);
        }
        dataMap.get(site).put(key, data);
    }

    //MODIFIES: this
    //EFFECTS: edits data in site from dataMap
    //REQUIRES:
    public void editData(String site, String key, byte[] data) {
        dataMap.get(site).replace(key, data);
    }

    //MODIFIES: this
    //EFFECTS: removes site from dataMap
    //REQUIRES:
    public void removeSite(String site) {
        dataMap.remove(site);
    }

    //MODIFIES: this
    //EFFECTS: removes data from site in dataMap
    //REQUIRES:
    public void removeData(String site, String key) {
        dataMap.get(site).remove(key);
    }
}
