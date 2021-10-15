package ui;

import model.PasswordManager;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


//creates a UI to allow the user to use the Password Manager
public class UI {

    private final Scanner scan;
    private boolean running;
    private PasswordManager passManager;

    //MODIFIES: this
    //EFFECTS: initializes a new UI object and call the init method to start the password manager
    //REQUIRES:
    UI() {
        scan = new Scanner(System.in);
        running = true;
        passManager = new PasswordManager();
        start();
    }

    //MODIFIES:
    //EFFECTS: creates a welcome UI and menu on startup
    //REQUIRES:
    public void start() {
        System.out.println("Welcome to Password Manager");
        while (running) {
            System.out.println("login/register/exit");
            String choice = scan.nextLine().trim();
            checkOptions(choice);
            while (passManager.isLoggedIn()) {
                System.out.println("add/display/remove/logout");
                choice = scan.nextLine().trim();
                afterLoginChoice(choice);
            }
        }
    }

    //MODIFIES:
    //EFFECTS: asks user for login info and returns true if it matches existing data otherwise false
    //REQUIRES:
    public boolean login() {
        System.out.println("Username:");
        String username = scan.nextLine().trim();
        System.out.println("Password:");
        String password = scan.nextLine().trim();
        if (passManager.checkLogin(username, password)) {
            System.out.println("Welcome " + username);
            return true;
        }
        return false;
    }

    //MODIFIES: PManager
    //EFFECTS: asks user to decide on login credentials and then saves them
    //REQUIRES:
    public void register() {
        System.out.print("Enter a username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter a password: ");
        String password = scan.nextLine().trim();
        try {
            passManager.addUser(username, password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("An error has occurred");
        }
    }

    //MODIFIES: this
    //EFFECTS: checks what option the user selected and calls relative methods
    //REQUIRES:
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
            default:
                System.out.println("login - logs user in with username and password");
                System.out.println("register - creates a new user credential with username and password");
                System.out.println("exit - exits the password manager");
        }
    }

    //MODIFIES: PasswordManager
    //EFFECTS: gets information that the user wants to save and call the save method
    //REQUIRES:
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
                System.err.println("An error has occurred");
            }
        }
        System.out.println("Data added");
    }

    //MODIFIES: PasswordManager
    //EFFECTS: asks user what data they would like to edit and then calls the editData method with data
    //REQUIRES:
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
            System.err.println("Error has occurred");
        }
    }

    //MODIFIES: PasswordManager
    //EFFECTS: asks user what data they would like to remove and then calls the remove data method
    //REQUIRES:
    private void removeInfo() {
        System.out.print("What site do you want to remove from: ");
        String site = scan.nextLine();
        System.out.print("What data are you removing");
        String key = scan.nextLine();
        passManager.removeData(site, key);
        System.out.println("Removed data");
    }

    //MODIFIES: PManager
    //EFFECTS: asks user what site they would like to remove and then calls the remove site method
    //REQUIRES:
    private void removeSite() {
        System.out.print("What site do you want to remove: ");
        String site = scan.nextLine();
        passManager.removeSite(site);
        System.out.println("Site removed");
    }

    //MODIFIES:
    //EFFECTS: checks what the user would like to remove and calls appropriate method
    //REQUIRES:
    private void remove() {
        System.out.print("Are you removing data or site: ");
        String choice = scan.nextLine();
        do {
            if (choice.equals("data")) {
                removeInfo();
            } else if (choice.equals("site")) {
                removeSite();
            } else {
                System.out.println("Invalid option");
            }
        } while (!choice.equals("data") && !choice.equals("site"));
    }

    //MODIFIES:
    //EFFECTS: asks user what data they will like displayed and display it
    //REQUIRES:
    private void display() {
        System.out.println("What do you want to display all/sites/info: ");
        switch (scan.nextLine()) {
            case "all":
                try {
                    System.out.println(passManager.displayAllInfo());
                } catch (Exception e) {
                    System.err.println("An error has occurred");
                }
                break;
            case "site":
                System.out.println(passManager.displaySites());
                break;
            case "info":
                System.out.println("Which site do you want info for: ");
                try {
                    System.out.println(passManager.displayInfo(scan.nextLine()));
                } catch (Exception e) {
                    System.out.println("An Error has occurred");
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    //MODIFIES: PasswordManager
    //EFFECTS: checks what option the user chooses after they log in and call appropriate method for that option
    //REQUIRES:
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
