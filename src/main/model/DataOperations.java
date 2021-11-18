package model;

//Interface for the operations on user data
public interface DataOperations {

    void addSite(String site);

    void addData(String site, String key, String data);

    void editData(String site, String key, String data);

    void removeSite(String site);

    void removeData(String site, String key);

}
