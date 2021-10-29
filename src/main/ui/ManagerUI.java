package ui;

import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


//creates a UI to allow the user to use the Password Manager
public class ManagerUI {

    private final Scanner scan;
    private boolean running;
    private PasswordManager passManager;
    private JsonWriter writer;
    private JsonReader reader;
    private static final String JSON_PATH = "./data/PManager.json";

    //EFFECTS: initializes a new UI object and call the init method to start the password manager
    ManagerUI() {
        scan = new Scanner(System.in);
        running = true;
        writer = new JsonWriter(JSON_PATH);
        reader = new JsonReader(JSON_PATH);
        try {
            passManager = reader.read();
        } catch (IOException e) {
            System.out.println("Previous data not found, loading default");
            passManager = new PasswordManager();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("An error has occurred " + e);
        }
        start();
        saveData();
    }

    //EFFECTS: creates a welcome UI and menu on startup
    public void start() {
        System.out.println("Welcome to Password Manager");
        while (running) {
            System.out.println("login/register/load/exit");
            String choice = scan.nextLine().trim();
            checkOptions(choice);
            while (passManager.isLoggedIn()) {
                System.out.println("add/display/edit/remove/save/logout");
                choice = scan.nextLine().trim();
                afterLoginChoice(choice);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: reads data from a json file and then assigns it to password Manager
    private void loadData() {
        System.out.println("What is the name of the data you would like to load: ");
        String srcFile = "./data/" + scan.nextLine() + ".json";
        JsonReader readFile = new JsonReader(srcFile);
        try {
            passManager = readFile.read();
        } catch (IOException e) {
            System.err.println("Unable to read file");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //EFFECTS: saves the password manager data to a JSON file
    private void saveData() {
        try {
            writer.open();
            writer.write(passManager);
            writer.close();
            System.out.println("File saved to: " + JSON_PATH);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to file");
        } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            System.err.println("An error occured");
        }
    }

    //EFFECTS: saves the password manager data to a JSON file
    private void saveData(String fname) {
        String destFile = "./data/" + fname + ".json";
        JsonWriter newFile = new JsonWriter(destFile);
        try {
            newFile.open();
            newFile.write(passManager);
            newFile.close();
            System.out.println("File saved to: " + destFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to " + destFile);
        } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            System.err.println("An error has occurred");
        }
    }

    //EFFECTS: asks user for login info and returns true if it matches existing data otherwise false
    public boolean login() {
        System.out.print("Username:");
        String username = scan.nextLine().trim();
        System.out.print("Password:");
        String password = scan.nextLine().trim();
        try {
            if (passManager.checkLogin(username, password)) {
                System.out.println("Welcome " + username);
                return true;
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: asks user to decide on login credentials and then saves them
    public void register() {
        System.out.print("Enter a username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter a password: ");
        String password = scan.nextLine().trim();
        try {
            if (passManager.checkLogin(username, password)) {
                System.out.println("User could not be registered");
            } else {
                passManager.addUser(username, password);
                System.out.println("User: " + username + " has been added");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.err.println("An error has occured - " + e.toString());
        }
    }

    //MODIFIES: this
    //EFFECTS: checks what option the user selected and calls relative methods
    private void checkOptions(String choice) {
        switch (choice) {
            case "login":
                if (!login()) {
                    System.out.println("Cannot login user");
                }
                break;
            case "register":
                register();
                break;
            case "exit":
                running = false;
                break;
            case "load":
                loadData();
                break;
            default:
                System.out.println("login - logs user in with username and password");
                System.out.println("register - creates a new user credential with username and password");
                System.out.println("exit - exits the password manager");
        }
    }

    //MODIFIES: this
    //EFFECTS: gets information that the user wants to save and call the save method
    private void addInformation() {
        System.out.print("Site you are saving: ");
        String site = scan.nextLine();
        passManager.addSite(site);
        System.out.print("How many items are you saving: ");
        int numOfItems = Integer.parseInt(scan.nextLine());
        for (int i = 0; i < numOfItems; i++) {
            System.out.print("What is the category of item " + (i + 1) + ": ");
            String dataKey = scan.nextLine();
            System.out.print("What is the data: ");
            String data = scan.nextLine();
            try {
                passManager.addInfo(site, dataKey, data);
            } catch (Exception e) {
                System.err.println("An error has occurred, info couldn't be added - " + e.toString());
            }
        }
        System.out.println("Data added");
    }

    //MODIFIES: this
    //EFFECTS: asks user what data they would like to edit and then calls the editData method with data
    private void editInfo() {
        System.out.print("Which Site would you like to edit: ");
        String site = scan.nextLine();
        try {
            System.out.println(passManager.displayInfo(site));
        } catch (Exception e) {
            System.err.println("An error has occurred");
        }
        System.out.print("What would you like to edit: ");
        String key = scan.nextLine();
        System.out.print("What would you like to change it to: ");
        String newData = scan.nextLine();
        try {
            passManager.editData(site, key, newData);
        } catch (Exception e) {
            System.err.println("Error has occurred, info couldn't be added");
        }
    }

    //MODIFIES: this
    //EFFECTS: asks user what data they would like to remove and then calls the remove data method
    private void removeInfo() {
        System.out.print("What site do you want to remove from: ");
        String site = scan.nextLine();
        System.out.print("What data are you removing");
        String key = scan.nextLine();
        passManager.removeData(site, key);
        System.out.println("Removed data");
    }

    //MODIFIES: this
    //EFFECTS: asks user what site they would like to remove and then calls the remove site method
    private void removeSite() {
        System.out.print("What site do you want to remove: ");
        String site = scan.nextLine();
        passManager.removeSite(site);
        System.out.println("Site removed");
    }

    //EFFECTS: checks what the user would like to remove and calls appropriate method
    private void remove() {
        String choice;
        do {
            System.out.print("Are you removing data or site: ");
            choice = scan.nextLine();
            if (choice.equals("data")) {
                removeInfo();
            } else if (choice.equals("site")) {
                removeSite();
            } else {
                System.out.println("Invalid option");
            }
        } while (!choice.equals("data") && !choice.equals("site"));
    }

    //EFFECTS: asks user what data they will like displayed and display it
    private void display() {
        System.out.println("What do you want to display all/sites/info: ");
        switch (scan.nextLine()) {
            case "all":
                try {
                    System.out.println(passManager.displayAllInfo());
                } catch (Exception e) {
                    System.err.println("An error has occurred, info couldn't be displayed");
                }
                break;
            case "sites":
                System.out.println(passManager.displaySites());
                break;
            case "info":
                System.out.println("Which site do you want info for: ");
                try {
                    System.out.println(passManager.displayInfo(scan.nextLine()));
                } catch (Exception e) {
                    System.err.println("An Error has occurred, couldn't access info about site");
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    //EFFECTS: Saves password manager to a json file of user choice
    private void saveToFile() {
        System.out.print("Name of file to save: ");
        saveData(scan.nextLine());
    }

    //EFFECTS: sets current user to null (represents no user being served) and encrypts data
    //MODIFIES: this
    private void logOutUser() {
        try {
            passManager.userLoggedOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //MODIFIES: this
    //EFFECTS: checks what option the user chooses after they log in and call appropriate method for that option
    private void afterLoginChoice(String choice) {
        if (choice.equals("add")) {
            addInformation();
        } else if (choice.equals("display")) {
            display();
        } else if (choice.equals("remove")) {
            remove();
        } else if (choice.equals("edit")) {
            editInfo();
        } else if (choice.equals("logout")) {
            logOutUser();
        } else if (choice.equals("save")) {
            saveToFile();
        } else {
            System.out.println("Invalid option");
        }
    }
}
