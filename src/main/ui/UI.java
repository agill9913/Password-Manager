package ui;

import model.PasswordManager;

import java.util.Scanner;

public class UI implements LoginUI {

    private final Scanner scan;
    private boolean running;
    private boolean loggedIn;
    private PasswordManager manager;

    UI() {
        scan = new Scanner(System.in);
        running = true;
        loggedIn = false;
        manager = new PasswordManager();
    }

    public void init() {
        System.out.println("Welcome to Password Manager");
        while (running) {
            System.out.println("What would you like to do today?");
            String choice = scan.nextLine().trim();
            checkOptions(choice);
            while (loggedIn) {
                System.out.println("What would you like to do: ");
                choice = scan.nextLine().trim();
                afterLoginChoice(choice);
            }
        }
    }

    public boolean login() {
        System.out.println("Username:");
        String username = scan.nextLine().trim();
        System.out.println("Password:");
        String password = scan.nextLine().trim();
        return manager.checkLogin(username, password);
    }

    public void register() {
        System.out.print("Enter a username: ");
        String username = scan.nextLine().trim();
        System.out.print("Enter a password: ");
        String password = scan.nextLine().trim();
        manager.addUser(username, password);
    }

    private int checkOptions(String choice) {
        switch (choice) {
            case "login":
                loggedIn = login(); //todo - rework to allow checksum fault
                if (loggedIn) {
                    return 1;
                }
                System.out.println("Invalid credentials");
                break;
            case "register":
                register();
                return 2;
            case "exit":
                running = false;
                return 0;
            default:
                System.out.println("login - logs user in with username and password");
                System.out.println("register - creates a new user credential with username and password");
                System.out.println("exit - exits the password manager");
        }
        return -1;
    }

    private String askUserForData() {
        System.out.println("Next data to add: ");
        return scan.nextLine();
    }

    private void addInformation() {
        System.out.println("Identifier/Site you would like to save ");
        String id = scan.nextLine();
        System.out.println("How many items are you adding");
        int size = scan.nextInt();
        String[] data = new String[size];
        for (int i = 0; i < size; i++) {
            data[i] = askUserForData();
        }
        manager.addInfo(id, data);
    }

    private void editInfo() {
        System.out.println(manager.displayIDs());
        System.out.println("Which ID would you like to edit");
        String editID = scan.nextLine();
        System.out.println("Choose data to edit by typing 1-" + manager.dataSize(editID));
        int choice = scan.nextInt();
        System.out.println("What would you like to change it to: ");
        String newValue = scan.nextLine();
        manager.editData(editID, choice, newValue);
        System.out.println("Data saved");
    }

    private void removeInfo() {
        System.out.println("Identifier for information to remove: ");
        String removedID = scan.nextLine();
        System.out.println("Removing " + removedID);
        if (manager.removeInfo(removedID)) {
            System.out.println("Data removed");
        } else {
            System.out.println("Data not found, unable to remove");
        }

    }

    private void afterLoginChoice(String choice) {
        switch (choice) {
            case "add":
                addInformation();
                break;
            case "display":
                System.out.println(manager.displayAllInfo());
                break;
            case "remove":
                removeInfo();
                break;
            case "edit":
                editInfo();
                break;
            case "logout":
                loggedIn = false;
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}
