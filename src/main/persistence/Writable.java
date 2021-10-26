package persistence;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

//Class is referenced from class demo code taken from
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
public interface Writable {
    JSONObject toJson() throws NoSuchAlgorithmException;
}
