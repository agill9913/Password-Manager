package model;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PasswordManager {
    Scanner scan;
    ArrayList<LoginInformation> credentials;
    HashMap<String, String[]> information;

    public PasswordManager() {
        scan = new Scanner(System.in);
        credentials = new ArrayList<>();
    }

    public void addInfo(String identifier) {
        System.out.println("Identifier/Site you would like to save ");
        String name = scan.nextLine();
        String currData;
        System.out.println("How many items are you adding");
        int size = scan.nextInt();
        String[] data = new String[size];
        for (int i = 0; i < size; i++) {
            currData = scan.nextLine();
            data[i] = currData;
        }
        information.put(name, data);
    }

    public void init() throws NoSuchAlgorithmException {
        System.out.println("Welcome to Password Manager");
        System.out.println("What would you like to do today?");
        String choice = scan.nextLine();
        checkOptions(choice);
    }

    public int login() {
        System.out.println("Username:");
        String username = scan.nextLine().trim();
        System.out.println("Password:");
        String password = scan.nextLine().trim();
        for (LoginInformation currInfo: credentials) {
            if (currInfo.checkValues(username) && currInfo.checkValues(password)) {
                System.out.println("Welcome " + username);
                return 1;
            }
        }
        System.out.println("Invalid credentials");
        return 0;
    }

    public void register() throws NoSuchAlgorithmException {
        String username;
        String password;
        System.out.print("Enter a username: ");
        username = scan.nextLine();
        System.out.print("Enter a password");
        password = scan.nextLine();
        credentials.add(new LoginInformation(username, password));
    }

    public int checkOptions(String choice) throws NoSuchAlgorithmException {
        switch (choice) {
            case "login":
                return login();
            case "register":
                register();
                return 2;
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("login - logs user in with username and password");
                System.out.println("register - creates a new user credential with username and password");
                System.out.println("exit - exits the password manager");
                return 3;
        }
        System.out.println("An error has occurred"); //should never reach but just in case
        return -1;
    }
}
