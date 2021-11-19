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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


//Initializes a GUI to use Password Manager with
public class ManagerGUI extends JFrame {
    private static final String JSON_PATH = "./data/PManager.json";
    private JPanel masterPanel;
    private CardLayout cardLayout;

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
    private JButton search;
    private JButton logout;
    private JButton userInfo;

    public static void main(String[] args) {
        new ManagerGUI();
    }

    //EFFECTS: Creates a GUI for Password Manager and initialize the panels for GUI
    //MODIFIES: this
    ManagerGUI() {
        JsonReader readFile = new JsonReader(JSON_PATH);
        try {
            this.manager =  readFile.read();
        } catch (IOException evv) {
            this.manager = new PasswordManager();
        } catch (NoSuchAlgorithmException eb) {
            JOptionPane.showMessageDialog(loginPanel, "An error has occurred, loading default",
                    "Error", JOptionPane.WARNING_MESSAGE);
            this.manager = new PasswordManager();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLoginPanel();
        setRunningPanel();
        setMasterPanel();
        setTitle("Password Manager Login");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.getWidth() / 4.0), (int)(screenSize.getHeight() / 4.0));
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./data/tobs.jpg").getImage());
        addWindowListener(new CloseListener());
        setVisible(true);
    }

    private void setMasterPanel() {
        this.cardLayout = new CardLayout();
        this.masterPanel = new JPanel(cardLayout);
        this.masterPanel.add(this.loginPanel, "LOGIN");
        this.masterPanel.add(this.runningPanel, "RUNNING");
        add(this.masterPanel, BorderLayout.CENTER);
    }

    //EFFECTS: Adds components to running toolbar
    //MODIFIES: this
    private void setToolBar() {
        toolBar.setRollover(true);
        toolBar.add(this.add);
        toolBar.add(this.edit);
        toolBar.add(this.remove);
        toolBar.add(this.save);
        toolBar.add(this.search);
        toolBar.add(this.logout);
        toolBar.add(this.userInfo);
        toolBar.addSeparator();
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
        this.search = new JButton("Search");
        this.search.addActionListener(new SearchListener());
        this.logout = new JButton("Logout");
        this.logout.addActionListener(new LogoutListener());
        this.runningPanel = new JPanel(new BorderLayout());
        this.userInfo = new JButton("User List");
        this.userInfo.addActionListener(new UserListListener());
        this.toolBar = new JToolBar();
        setToolBar();
        runningPanel.add(this.toolBar, BorderLayout.NORTH);
        runningPanel.add(this.dataOutput, BorderLayout.CENTER);
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

    //EFFECTS: Saves manager to file destination
    private void saveData(String destFile) {
        JsonWriter writer = new JsonWriter(destFile);
        try {
            writer.open();
            writer.write(manager);
            writer.close();
            JOptionPane.showMessageDialog(runningPanel, "File written to: " + destFile, "File Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException eb) {
            JOptionPane.showMessageDialog(runningPanel, "Can't write to file", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException et) {
            JOptionPane.showMessageDialog(runningPanel, "An error has occurred", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Listener for when save button clicked, saves Manager to JSON file
    private class SaveListener implements ActionListener {
        @Override
        //EFFECTS: Display a file chooser for user to input file to save manager to and save it to said file
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser("./data");
            if (fileChooser.showSaveDialog(masterPanel) == JFileChooser.APPROVE_OPTION) {
                saveData(fileChooser.getSelectedFile() + ".json");
            }
        }
    }

    //Listener for when remove is clicked, removes entire site or specific data from site
    private class RemoveListener implements ActionListener {

        //EFFECTS: Displays popup about which data to delete and removes it from manager
        //MODIFIES: manager
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] choices = {"Site", "Data"};
            int option = JOptionPane.showOptionDialog(runningPanel, "Remove site or data?", "Remove",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            JComboBox sites = new JComboBox(manager.getSites());
            JOptionPane.showMessageDialog(runningPanel, sites, "Sites", JOptionPane.QUESTION_MESSAGE);
            String userSite = sites.getSelectedItem().toString();
            if (option == 0) {
                manager.removeSite(userSite);
            } else if (option == 1) {
                JComboBox keys = new JComboBox(manager.getData(userSite));
                JOptionPane.showMessageDialog(runningPanel, keys, "Saved values", JOptionPane.QUESTION_MESSAGE);
                manager.removeData(userSite, keys.getSelectedItem().toString());
            }
            dataOutput.setText(manager.displayAllInfo());
        }
    }

    //Listener for when edit is clicked, edits existing data in manager with new data
    private class EditListener implements ActionListener {

        //EFFECTS: creates a popup to get info and which data to edit and edit manager accordingly
        //MODIFIES: manager
        @Override
        public void actionPerformed(ActionEvent e) {
            final JComboBox sites = new JComboBox(manager.getSites());
            sites.setSelectedIndex(0);
            JOptionPane.showMessageDialog(runningPanel, sites, "Sites", JOptionPane.QUESTION_MESSAGE);
            String siteChoice = sites.getSelectedItem().toString();
            final JComboBox data = new JComboBox(manager.getData(siteChoice));
            data.setSelectedIndex(0);
            JOptionPane.showMessageDialog(runningPanel, data, "Data", JOptionPane.QUESTION_MESSAGE);
            String dataChoice = data.getSelectedItem().toString();
            String newData = JOptionPane.showInputDialog(runningPanel, "Input new data value: ",
                    "Input Data", JOptionPane.QUESTION_MESSAGE);
            data.addItem(newData);
        }
    }

    //Listener for when add is clicked, adds either a site or data to an existing site
    private class AddListener implements ActionListener {

        //EFFECTS: Get choice if data or site choosen and return it
        private int chooseAdd() {
            String[] choices = {"Site", "Data"};
            return JOptionPane.showOptionDialog(runningPanel, "Add site or data?", "Add",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        }

        //EFFECTS: add components to dialog popup depending on choice
        //modifies: this
        private void setPopup(int choice, JTextField siteField, JDialog addPopup, JTextField dataKeyField,
                              JTextField dataField, JButton addButton) {
            addPopup.setLayout(new FlowLayout());
            addPopup.add(new JLabel("Site"));
            addPopup.add(siteField);
            if (choice == 1) {
                addPopup.add(new JLabel("Data Name"));
                addPopup.add(dataKeyField);
                addPopup.add(new JLabel("Data value"));
                addPopup.add(dataField);
            }
            chooseOption(choice, addPopup, dataKeyField, dataField, siteField, addButton);
            addPopup.add(addButton);
            addPopup.setSize(300, 300);
            addPopup.setVisible(true);
        }

        //EFFECTS:checks if site or data choosen and data fields to panel and add data to manager
        //MODIFIES: this, manager
        private void chooseOption(int option, JDialog addPopup, JTextField dataKeyField, JTextField dataField,
                                  JTextField siteField, JButton addData) {
            if (option == 0) {
                addData.addActionListener(new ActionListener() {

                    //EFFECTS: add site to manager
                    //MODIFIES: manager
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manager.addSite(siteField.getText());
                        addPopup.setVisible(false);
                        dataOutput.setText(manager.displayAllInfo());
                    }
                });
            } else if (option == 1) {
                addData.addActionListener(new ActionListener() {

                    //EFFECTS: add site and data to manager
                    //MODIFIES: manager
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manager.addData(siteField.getText(), dataKeyField.getText(), dataField.getText());
                        addPopup.setVisible(false);
                        dataOutput.setText(manager.displayAllInfo());
                    }
                });
            }
        }

        //EFFECTS: creates popup to ask for data to add
        //MODIFIES: this
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog addPopup = new JDialog(ManagerGUI.this, "AddPopup", true);
            JTextField siteField = new JTextField(25);
            JTextField dataKeyField = new JTextField(25);
            JTextField dataField = new JTextField(25);
            JButton addData = new JButton("Add");
            int option = chooseAdd();
            setPopup(option, siteField, addPopup, dataKeyField, dataField, addData);
        }
    }

    //Listener for when load is clicked, loads data to manager from a file user inputs
    private class LoadListener implements ActionListener {

        //EFFECTS: gets file to load and load from said file to manager
        //MODIFIES: manager
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser("./data");
            if (fileChooser.showOpenDialog(masterPanel) == JFileChooser.APPROVE_OPTION) {
                JsonReader readFile = new JsonReader(fileChooser.getSelectedFile().getPath());
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
    }

    //Listener for when register is clicked, create and add a new user to manager
    private class RegisterListener implements ActionListener {

        //EFFECTS: adds user to manager
        //MODIFIES: manager
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

        //EFFECTS: logs user in by checking login credentials and change frame
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (!manager.checkLogin(username.getText(), new String(password.getPassword()))) {
                    password.setText("");
                    JOptionPane.showMessageDialog(loginPanel, "Unable to login user");
                } else {
                    cardLayout.show(masterPanel, "RUNNING");
                    dataOutput.setText(manager.displayAllInfo());
                }
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
                    | NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(loginPanel, "Error has occurred");
            }
        }
    }

    //Listener for when search is pushed
    private class SearchListener implements ActionListener {

        //EFFECTS: Creates a new frame with the data from a site
        @Override
        public void actionPerformed(ActionEvent e) {
            final JComboBox sites = new JComboBox(manager.getSites());
            sites.setSelectedIndex(0);
            JOptionPane.showMessageDialog(runningPanel, sites, "Sites", JOptionPane.QUESTION_MESSAGE);
            String siteChoice = sites.getSelectedItem().toString();
            JFrame output = new JFrame();
            JTextField text = new JTextField();
            text.setText(manager.displayInfo(siteChoice));
            text.setEditable(false);
            output.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            output.setTitle("Search");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            output.setSize((int)(screenSize.getWidth() / 4.0), (int)(screenSize.getHeight() / 4.0));
            output.setLocationRelativeTo(null);
            output.add(text);
            output.setVisible(true);
        }
    }

    //listener for when logout is pushed
    private class LogoutListener implements ActionListener {

        //EFFECTS: logs user out and change panel to login
        //MODIFIES: manager
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                manager.userLoggedOut();
                cardLayout.show(masterPanel, "LOGIN");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginPanel, "An error has occurred", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Listener for when userlist button pushed
    private class UserListListener implements ActionListener {

        //EFFECTS: Display popup of list of hashed user credentials
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame userFrame = new JFrame();
            JTextField users = new JTextField();
            users.setText(manager.userStringList());
            users.setEditable(false);
            userFrame.add(users);
            userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userFrame.setTitle("User List");
        }
    }

    //Listener for windows events
    private class CloseListener implements WindowListener {

        //EEFECTS: does something when window is opened
        @Override
        public void windowOpened(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }

        //EFFECTS: When window is closing, log user out and autosaves password manager
        //MODIFIES: manager
        @Override
        public void windowClosing(WindowEvent e) {
            try {
                manager.userLoggedOut();
                cardLayout.show(masterPanel, "LOGIN");
                saveData(JSON_PATH);
            } catch (Exception er) {
                JOptionPane.showMessageDialog(ManagerGUI.this, "An error occured", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        //EFFECTS: does something when window is closed
        @Override
        public void windowClosed(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }

        //EFFECTS: does something when window is iconified
        @Override
        public void windowIconified(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }

        //EFFECTS: does something when window is deiconified
        @Override
        public void windowDeiconified(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }

        //EFFECTS: does something when window is activated
        @Override
        public void windowActivated(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }

        //EFFECTS: does something when window is deactivated
        @Override
        public void windowDeactivated(WindowEvent e) {
            //not used but required to be implemented for WindowListener
        }
    }
}