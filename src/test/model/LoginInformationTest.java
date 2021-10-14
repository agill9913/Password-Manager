package model;

import org.junit.jupiter.api.BeforeEach;
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
        assertEquals(info1.checkValues("bob123", "no matter the cost"), 0);
        assertEquals(info1.checkValues("esr", "no matter the cost"), -1);
        assertEquals(info1.checkValues("bob123", "esrf"), -1);
        assertEquals(info1.checkValues("esr", "resf"), -1);
        assertEquals(info1.getChecksum(),"6456995169d55fc8115b948d55511f665936578d");
    }

}