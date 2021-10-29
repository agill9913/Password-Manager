package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    public void testInvalidFile() {
        try {
            JsonWriter testWriter = new JsonWriter("./data/my\0illegal:fileName.json");
            testWriter.open();
            fail("We should get an IOException");
        } catch (FileNotFoundException e) {
            //We've failed successfully
        }
    }

    @Test
    public void testEmptyFile() {
        String path = "./data/persistanceTestData/emptyReaderTest.json";
        PasswordManager testManager = new PasswordManager();
        try {
            JsonWriter testWriter = new JsonWriter(path);
            testWriter.open();
            testWriter.write(testManager);
            testWriter.close();

            JsonReader testReader = new JsonReader(path);
            testManager = testReader.read();
            assertEquals(testManager.userCount(), 0);
        } catch (FileNotFoundException e) {
            fail("File couldn't be found");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (NoSuchAlgorithmException e) {
            fail("Something happened in AES or Hashing");
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            fail("Something happened in toJson");
        }
    }

    @Test
    public void testSwitchingUsers() {
        String[] username = {"Bob", "John"};
        String[] password = {"Joe", "Doe"};
        PasswordManager manager1 = new PasswordManager();
        try {
            manager1.addUser(username[0], password[0]);
            assertTrue(manager1.checkLogin(username[0], password[0]));
            manager1.addInfo("google", "username", "user");
            manager1.addInfo("google", "password", "pswd");
            manager1.userLoggedOut();
            manager1.addUser(username[1], password[1]);
            assertTrue(manager1.checkLogin(username[1], password[1]));
            manager1.addInfo("amazon", "credit card number", "234234242");
            JsonWriter testWriter = new JsonWriter("./data/persistanceTestData/multi.json");
            testWriter.open();
            testWriter.write(manager1);
            testWriter.close();
            manager1.userLoggedOut();
            assertTrue(manager1.checkLogin(username[0], password[0]));
            assertEquals("google\n\tpassword: pswd\n\tusername: user\n", manager1.displayAllInfo());
        } catch (Exception e) {
            fail("Something went really wrong - nuclear explosion wrong");
        }
    }

}
