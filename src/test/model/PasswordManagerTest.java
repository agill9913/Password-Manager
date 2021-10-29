package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagerTest {

    PasswordManager manager1;

    @BeforeEach
    public void runBefore() {
        manager1 = new PasswordManager();
    }

    @Test
    public void testAddUser() throws Exception {
        assertFalse(manager1.isLoggedIn());
        manager1.addUser("Bob", "Joe");
        assertTrue(manager1.checkLogin("Bob", "Joe"));
        assertFalse(manager1.checkLogin("awd", "fes"));
        assertTrue(manager1.isLoggedIn());
        manager1.userLoggedOut();
        assertFalse(manager1.isLoggedIn());
    }

    @Test
    public void testDifferentUsers() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.addUser("Jane", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addInfo("google", "username", "user123");
        manager1.addInfo("google", "password", "pswd123");
        manager1.userLoggedOut();
        manager1.checkLogin("Jane", "Joe");
        manager1.addInfo("Amazon", "username", "user987");
        assertEquals("Amazon\n\tusername: user987\n", manager1.displayAllInfo());
        manager1.userLoggedOut();
        manager1.checkLogin("Bob", "Joe");
        assertEquals("google\n\tpassword: pswd123\n\tusername: user123\n", manager1.displayAllInfo());
    }

    @Test
    public void testAddingInfo() throws Exception {
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
    public void testAddSameData() {
        try {
            manager1.addUser("Bob", "Joe");
            manager1.checkLogin("Bob", "Joe");
            manager1.addSite("google");
            manager1.addInfo("google", "username", "user1");
            manager1.addInfo("google", "username", "user2");
            manager1.addInfo("google", "username", "user3");
            assertEquals("google\n\tusername3: user3\n\tusername2: user2\n\tusername: user1\n",
                    manager1.displayAllInfo());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            fail("something went wrong");
        }
    }

    @Test
    public void testAddSameSite() {
        try {
            manager1.addUser("Bob", "Joe");
            manager1.checkLogin("Bob", "Joe");
            manager1.addSite("google");
            manager1.addSite("google");
            assertEquals("google\n", manager1.displaySites());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            fail("something went wrong");
        }
    }

    @Test
    public void testAddingInfoToEmpty() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addInfo("google", "username", "user123");
        assertEquals("google\n", manager1.displaySites());
        assertEquals("username: user123\n", manager1.displayInfo("google"));
    }

    @Test
    public void testRemovingInfo() throws Exception {
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
    public void testRemovingSite() throws Exception {
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
    public void testEditInfo() throws Exception {
        manager1.addUser("Bob", "Joe");
        manager1.checkLogin("Bob", "Joe");
        manager1.addSite("google");
        manager1.addInfo("google", "username", "user123");
        manager1.editData("google", "username", "newUser");
        assertEquals("username: newUser\n", manager1.displayInfo("google"));
    }

    @Test
    public void testMultipleUserstoJson() {
        try {
            manager1.addUser("Bob", "Joe");
            manager1.addUser("Jane", "Doe");
            JSONObject testObj = manager1.toJson();
            JSONArray testArray = testObj.getJSONArray("accounts");
            assertEquals(testArray.getJSONObject(0).getJSONObject("LoginInfo").get("username"),
                    "da6645f6e22bf5f75974dc7eed5fcd6160d6b51e");
            assertEquals(testArray.getJSONObject(0).getJSONObject("LoginInfo").get("password"),
                    "89661149f1b62ff47dd5a6fe4f979c9f53f619b6");
            assertEquals(testArray.getJSONObject(1).getJSONObject("LoginInfo").get("username"),
                    "1b78097fcf82ab0445ae892d380c29ccb5405620");
            assertEquals(testArray.getJSONObject(1).getJSONObject("LoginInfo").get("password"),
                    "c947ad320e66fc64998e86a55c0da210c8c1d81a");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
                | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            fail("You were not supposed to reach this");
        }
    }

    @Test
    public void testLoginThenLogout() {
        String username = "Bob";
        String password = "Joe";
        try {
            manager1.addUser(username, password);
            manager1.checkLogin(username, password);
            manager1.addInfo("google", "username", "user23");
            manager1.userLoggedOut();
            manager1.checkLogin(username, password);
            assertEquals("google\n\tusername: user23\n", manager1.displayAllInfo());
        } catch (NoSuchAlgorithmException e) {
            fail("Couldn't find a algorithm exception");
        } catch (Exception e) {
            fail("Something went wrong in AES or Hashing");
        }
    }
}
