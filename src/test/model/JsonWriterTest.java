package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        }
    }


}
