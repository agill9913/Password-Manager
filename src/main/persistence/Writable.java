package persistence;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public interface Writable {
    JSONObject toJson() throws NoSuchAlgorithmException;
}
