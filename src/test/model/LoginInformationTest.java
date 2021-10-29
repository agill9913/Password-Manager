package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class LoginInformationTest {

    LoginInformation info1;
    LoginInformation info2;
    String username;
    String pswd;

    @BeforeEach
    public void runBefore() {
        username = "bob123";
        pswd = "no matter the cost";
        try {
            info1 = new LoginInformation(username, pswd, true);
            info2 = new LoginInformation(username, pswd, false);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIfHashed() {
        //is a match possible
        assertFalse(info2.checkValues(username, pswd));
        assertTrue(info1.checkValues(username, pswd));

        //messing with various wrong inputs
        assertFalse(info1.checkValues("esr", pswd));
        assertFalse(info1.checkValues(username, "esrf"));
        assertFalse(info1.checkValues("esr", "resf"));

        assertNotEquals(info2.getHash(), username);
        assertNotEquals(info2.getHash(), pswd);
    }

    @Test
    public void testHashing() {
        String hash = "6456995169d55fc8115b948d55511f665936578d";
        assertNotEquals(info2.getHash(), username);
        assertNotEquals(info2.getHash(), pswd);
        assertEquals(info2.getHash(), hash);
        assertNotEquals(info1.getHash(), hash);
        assertNotEquals(info1.getHash(), hash);
        assertNotEquals(info1.getHash(), hash);
    }

    @Test
    public void testJSON() {
        JSONObject testobj = info1.toJson();
        assertEquals(testobj.get("username"), "0a42b6b9dcd569f990dcde40f4ff73c5a24eb904");
        assertEquals(testobj.get("password"), "7bbfcd98eeed66951a5169afb3fac771e19e7fde");

    }

}