package ui;

import model.LoginInformation;

import java.util.Scanner;

public class UI implements LoginUI {

    Scanner scan;
    boolean running;
    volatile boolean loggedIn;

    UI() {
        scan = new Scanner(System.in);
        running = true;
        loggedIn = false;
    }

    public void init() {
        System.out.println("Welcome to Password Manager");
        while (running) {
            System.out.println("What would you like to do today?");
            String choice = scan.nextLine();
            if (checkOptions(choice) == 1) {
                while (loggedIn) {
                    //
                }
            }
        }
    }

    public String[] login() {
        System.out.println("Username:");
        String[] credentials = new String[2];
        credentials[0] = scan.nextLine().trim();
        System.out.println("Password:");
        credentials[1] = scan.nextLine().trim();
        return credentials;
    }

    public LoginInformation register() {
        String username;
        String password;
        System.out.print("Enter a username: ");
        username = scan.nextLine();
        System.out.print("Enter a password");
        password = scan.nextLine();
        return new LoginInformation(username, password);
    }

    public int checkOptions(String choice) {
        switch (choice) {
            case "login":
                login();
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

    public String askUserForData() {
        System.out.println("Next data to add: ");
        return scan.nextLine();
    }

    public void inputInformation() {
        System.out.println("Identifier/Site you would like to save ");
        String id = scan.nextLine();
        System.out.println("How many items are you adding");
        int size = scan.nextInt();
        String[] data = new String[size];
        for (int i = 0; i < size; i++) {
            data[i] = askUserForData();
        }
    }

    public void removeInfo() {
        System.out.println("Identifier for information to remove: ");
        String removedID = scan.nextLine();
        System.out.println("Removing " + removedID);
    }

    public void afterLoginChoice() {
        System.out.println("What would you like to do: ");
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "add":
                inputInformation();
                break;
            case "display":
                //
                break;
            case "remove":
                removeInfo();
                break;
            case "edit":
                //
                break;
            case "logout":
                loggedIn = false;
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}
