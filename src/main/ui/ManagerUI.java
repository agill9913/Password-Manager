package ui;

import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


//creates a UI to allow the user to use the Password Manager
public class ManagerUI {

    private final Scanner scan;
    private boolean running;
    private PasswordManager passManager;
    private JsonWriter writer;
    private JsonReader reader;
    private static final String JSON_PATH = "./data/pmanager.json";

    //EFFECTS: initializes a new UI object and call the init method to start the password manager
    ManagerUI() {
        scan = new Scanner(System.in);
        running = true;
        passManager = new PasswordManager();
        writer = new JsonWriter(JSON_PATH);
        reader = new JsonReader(JSON_PATH);
        try {
            passManager = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        start();
        saveData();
    }

    //EFFECTS: creates a welcome UI and menu on startup
    public void start() {
        System.out.println("Welcome to Password Manager");
        while (running) {
            System.out.println("login/register/json/exit");
            String choice = scan.nextLine().trim();
            checkOptions(choice);
            while (passManager.isLoggedIn()) {
                System.out.println("add/display/edit/remove/logout");
                choice = scan.nextLine().trim();
                afterLoginChoice(choice);
            }
        }
    }

    private void saveData() {
        try {
            writer.open();
            writer.write(passManager);
            writer.close();
            System.out.println("File saved to: " + JSON_PATH);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to file");
        }
    }

    //EFFECTS: asks user for login info and returns true if it matches existing data otherwise false
    public boolean login() {
        System.out.print("Username:");
        String username = scan.nextLine().trim();
        System.out.print("Password:");
        String password = scan.nextLine().trim();
        if (passManager.checkLogin(username, password)) {
            System.out.println("Welcome " + username);
            return true;
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
            passManager.addUser(username, password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("An error has occurred, user couldn't be registered");
        }
    }

    //MODIFIES: this
    //EFFECTS: checks what option the user selected and calls relative methods
    private void checkOptions(String choice) {
        switch (choice) {
            case "login":
                if (!login()) {
                    System.out.println("Invalid credentials");
                }
                break;
            case "register":
                register();
                break;
            case "exit":
                running = false;
                break;
            case "json":
                System.out.println(passManager.toJson().toString());
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
            System.out.print("What is the category of what you are saving: ");
            String dataKey = scan.nextLine();
            System.out.print("What is the data: ");
            String data = scan.nextLine();
            try {
                passManager.addInfo(site, dataKey, data);
            } catch (Exception e) {
                System.err.println("An error has occurred, info couldn't be added");
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

    //MODIFIES: this
    //EFFECTS: checks what option the user chooses after they log in and call appropriate method for that option
    private void afterLoginChoice(String choice) {
        switch (choice) {
            case "add":
                addInformation();
                break;
            case "display":
                display();
                break;
            case "remove":
                remove();
                break;
            case "edit":
                editInfo();
                break;
            case "logout":
                passManager.userLoggedOut();
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}
