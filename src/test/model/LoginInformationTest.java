package model;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class LoginInformationTest {

    LoginInformation info1;

    @Test
    public void testIfHashed() {
        try {
            info1 = new LoginInformation("bob123", "no matter the cost");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //is a match possible
        assertTrue(info1.checkValues("bob123", "no matter the cost"));

        //messing with various wrong inputs
        assertFalse(info1.checkValues("esr", "no matter the cost"));
        assertFalse(info1.checkValues("bob123", "esrf"));
        assertFalse(info1.checkValues("esr", "resf"));

    }

}