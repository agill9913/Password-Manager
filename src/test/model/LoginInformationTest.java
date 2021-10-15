package model;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(info1.checkValues("bob123", "no matter the cost"), 0);

        //messing with various wrong inputs
        assertEquals(info1.checkValues("esr", "no matter the cost"), -1);
        assertEquals(info1.checkValues("bob123", "esrf"), -1);
        assertEquals(info1.checkValues("esr", "resf"), -1);


        //checks if data integrity is maintained
        assertEquals(info1.getChecksum(),"6456995169d55fc8115b948d55511f665936578d");

        assertTrue(info1.checksumCheck("bob123" + "no matter the cost"));

    }

}