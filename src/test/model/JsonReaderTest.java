package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testNofile() {
        JsonReader readNothing = new JsonReader("./data/IDontExist.json");
        try {
            PasswordManager testManager = readNothing.read();
            fail("IOException was skipped");
        } catch (IOException e) {
            //This is where I belong
        } catch (NoSuchAlgorithmException e) {
            fail("I shouldn't be here ever, found no such algorithm");
        }
    }

    @Test
    public void testEmptyFile() {
        JsonReader readEmpty = new JsonReader("./data/persistanceTestData/ImEmptyAndLonely.json");
        try {
            PasswordManager testManager = readEmpty.read();
            assertEquals(testManager.userCount(), 0);
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (NoSuchAlgorithmException e) {
            fail("NoSuchAlgorithmException occurred - this shouldn't occur");
        }
    }

    @Test
    public void testNormalFile() {
        JsonReader readFile = new JsonReader("./data/persistanceTestData/data3.json");
        try {
            PasswordManager testManager = readFile.read();
            assertEquals(testManager.userCount(), 1);
            assertTrue(testManager.checkLogin("armaan", "gill"));
            assertEquals(testManager.displayAllInfo(), "google\n\tpassword: pswd4565\n\tusername: ret45\n");
            testManager.userLoggedOut();
            //making sure login works
            assertTrue(testManager.checkLogin("armaan", "gill"));
            assertEquals(testManager.displayAllInfo(), "google\n\tpassword: pswd4565\n\tusername: ret45\n");
        } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | NoSuchPaddingException e) {
            fail("Something went really wrong with AES or hashing - should not be possible");
        } catch (IOException e) {
            fail("IOException with file");
        } catch (Exception e) {
            fail("Deacon 5 something went wrong - this is really bad");
        }
    }
}
