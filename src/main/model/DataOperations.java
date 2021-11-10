package model;

//Interface for the operations on user data
public interface DataOperations {

    enum Operations { ADDSITE, ADDDATA, EDITDATA, REMOVESITE, REMOVEDATA }

    void addSite(String site);

    void addData(String site, String key, String data);

    void editData(String site, String key, String data);

    void removeSite(String site);

    void removeData(String site, String key);

}
