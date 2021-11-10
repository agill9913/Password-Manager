package ui;

import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


//Initializes a GUI to use Password Manager with
public class ManagerGUI extends JFrame {
    private static final String JSON_PATH = "./data/PManager.json";
    private final JPanel masterPanel;
    private final CardLayout cardLayout;

    //login fields
    private PasswordManager manager;
    private JPanel loginPanel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton register;
    private JButton load;

    //running app fields
    private JPanel runningPanel;
    private JToolBar toolBar;
    private JTextPane dataOutput;
    private JButton add;
    private JButton remove;
    private JButton edit;
    private JButton save;

    public static void main(String[] args) {
        new ManagerGUI();
    }

    //EFFECTS: Creates a GUI for Password Manager and initialize the panels for GUI
    //MODIFIES: this
    ManagerGUI() {
        //this.manager = new PasswordManager();
        cardLayout = new CardLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLoginPanel();
        setRunningPanel();
        this.masterPanel = new JPanel(cardLayout);
        this.masterPanel.add(this.loginPanel, "LOGIN");
        this.masterPanel.add(this.runningPanel, "RUNNING");
        add(this.masterPanel, BorderLayout.CENTER);
        setTitle("Password Manager Login");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.getWidth() / 4.0), (int)(screenSize.getHeight() / 4.0));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //EFFECTS: Adds components to running toolbar
    //MODIFIES: this
    private void setToolBar() {
        toolBar.add(this.add);
        toolBar.add(this.edit);
        toolBar.add(this.remove);
        toolBar.add(this.save);
    }

    //EFFECTS: Initialize running panel components and add to running panel
    //MODIFIES: this
    private void setRunningPanel() {
        this.dataOutput = new JTextPane();
        this.dataOutput.setText("Hello world");
        this.save = new JButton("Save");
        this.save.addActionListener(new SaveListener());
        this.edit = new JButton("Edit");
        this.edit.addActionListener(new EditListener());
        this.remove = new JButton("Remove");
        this.remove.addActionListener(new RemoveListener());
        this.add = new JButton("Add");
        this.add.addActionListener(new AddListener());
        this.runningPanel = new JPanel();
        this.toolBar = new JToolBar();
        setToolBar();
        runningPanel.add(this.toolBar);
        runningPanel.add(this.dataOutput);
    }

    //EFFECTS: Initialize login panel components and add to log in panel
    //MODIFIES: this
    private void setLoginPanel() {
        this.usernameLabel = new JLabel();
        this.usernameLabel.setText("Username: ");
        this.username = new JTextField(25);
        this.passwordLabel = new JLabel();
        this.passwordLabel.setText("Password: ");
        this.password = new JPasswordField(25);
        this.login = new JButton("Login");
        this.login.addActionListener(new LoginListener());
        this.register = new JButton("Register");
        this.register.addActionListener(new RegisterListener());
        this.load = new JButton("Load");
        this.load.addActionListener(new LoadListener());
        this.loginPanel = makeLoginPanel();
    }

    //EFFECTS: Initialize login panel and add components to login panel
    //MODIFIES: this
    private JPanel makeLoginPanel() {
        JPanel retPanel = new JPanel(new GridLayout(4, 1));
        retPanel.add(this.usernameLabel);
        retPanel.add(this.username);
        retPanel.add(this.passwordLabel);
        retPanel.add(this.password);
        retPanel.add(this.login);
        retPanel.add(this.register);
        retPanel.add(this.load);
        return retPanel;
    }

    //Listener for when save button clicked, saves Manager to JSON file
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String destFile = "./data/" + JOptionPane.showInputDialog("File name: ") + ".json";
            JsonWriter writer = new JsonWriter(destFile);
            try {
                writer.open();
                writer.write(manager);
                writer.close();
                System.out.println("File saved to: " + destFile);
            } catch (FileNotFoundException eb) {
                JOptionPane.showMessageDialog(runningPanel, "Can't write to file",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException
                    | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException et) {
                JOptionPane.showMessageDialog(runningPanel, "An error has occurred",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    //Listener for when remove is clicked, removes entire site or specific data from site
    private class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] choices = {"Site", "Data"};
            int option = JOptionPane.showOptionDialog(runningPanel, "Remove site or data?", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            String site = JOptionPane.showInputDialog("Site: ");
            if (option == 0) {
                manager.removeSite(site);
            } else if (option == 1) {
                String key = JOptionPane.showInputDialog("Data name: ");
                manager.removeData(site, key);
            }
        }
    }

    //Listener for when edit is clicked, edits existing data in manager with new data
    private class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String site = JOptionPane.showInputDialog("Site: ");
            String key = JOptionPane.showInputDialog("Data name: ");
            String newData = JOptionPane.showInputDialog("Enter new value: ");
            manager.editData(site, key, newData);
        }
    }

    //Listener for when add is clicked, adds either a site or data to an existing site
    private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] choices = {"Site", "Data"};
            int option = JOptionPane.showOptionDialog(runningPanel, "Add site or data?", "Add",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            String site = JOptionPane.showInputDialog("Site: ");
            if (option == 0) {
                manager.addSite(site);
            } else if (option == 1) {
                String key = JOptionPane.showInputDialog("Data name: ");
                String data = JOptionPane.showInputDialog("Data Value: ");
                manager.addData(site, key, data);
            }
        }
    }

    //Listener for when load is clicked, loads data to manager from a file user inputs
    private class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String srcFile = "./data/" + JOptionPane.showInputDialog("File name with no extension: ") + ".json";
            JsonReader readFile = new JsonReader(srcFile);
            try {
                manager = readFile.read();
            } catch (IOException evv) {
                JOptionPane.showMessageDialog(loginPanel, "Unable to read file",
                        "File Error", JOptionPane.WARNING_MESSAGE);
            } catch (NoSuchAlgorithmException eb) {
                JOptionPane.showMessageDialog(loginPanel, "An error has occurred",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Listener for when register is clicked, create and add a new user to manager
    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                manager.addUser(username.getText(), new String(password.getPassword()));
            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(loginPanel, "Error has occurred");
            }
        }
    }

    //Listener for when login is clicked, checks given username and password against existing credentials and change
    //panel if correct
    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (!manager.checkLogin(username.getText(), new String(password.getPassword()))) {
                    password.setText("");
                    JOptionPane.showMessageDialog(loginPanel, "Unable to login user");
                } else {
                    cardLayout.show(masterPanel, "RUNNING");
                }
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
                    | NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(loginPanel, "Error has occurred");
            }
        }
    }

}