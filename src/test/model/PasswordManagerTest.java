package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagerTest {

    PasswordManager manager1;

    @BeforeEach
    public void init() {
        manager1 = new PasswordManager();
    }

    @Test
    public void addUserTest() throws NoSuchAlgorithmException {
        assertFalse(manager1.isLoggedIn());
        manager1.addUser("Bob", "Joe");
        assertTrue(manager1.checkLogin("Bob", "Joe"));
        assertFalse(manager1.checkLogin("awd", "fes"));
        assertTrue(manager1.isLoggedIn());
        manager1.userLoggedOut();
        assertFalse(manager1.isLoggedIn());
    }

    @Test
    public void addingInfoTest() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addSite("google");
        assertEquals("google\n", manager1.displaySites());
        manager1.addInfo("google", "username", "user123");
        assertEquals("username: user123\n", manager1.displayInfo("google"));
        assertEquals("google\n\tusername: user123\n", manager1.displayAllInfo());
        manager1.addSite("amazon");
        assertEquals("amazon\ngoogle\n", manager1.displaySites());
    }

    @Test
    public void addingInfoToEmpty() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addInfo("google", "username", "user123");
        assertEquals("google\n", manager1.displaySites());
        assertEquals("username: user123\n", manager1.displayInfo("google"));
    }

    @Test
    public void removingInfoTest() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addSite("google");
        manager1.addInfo("google", "username", "user123");
        manager1.removeData("google", "username");
        assertEquals("", manager1.displayInfo("google"));
        manager1.addInfo("google", "username", "user123");
        manager1.addInfo("google", "password", "pswd123");
        manager1.removeData("google", "username");
        assertEquals("password: pswd123\n", manager1.displayInfo("google"));
    }

    @Test
    public void removingSiteTest() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addSite("google");
        manager1.addInfo("google", "username", "user123");
        manager1.removeSite("google");
        assertEquals("", manager1.displaySites());
        manager1.addSite("google");
        manager1.addSite("amazon");
        manager1.addInfo("amazon", "username", "user456");
        manager1.removeSite("google");
        assertEquals("amazon\n", manager1.displaySites());
    }

    @Test
    public void editInfoTest() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addSite("google");
        manager1.addInfo("google", "username", "user123");
        manager1.editData("google", "username", "newUser");
        assertEquals("username: newUser\n", manager1.displayInfo("google"));
    }




}
