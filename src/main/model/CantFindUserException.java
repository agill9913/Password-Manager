package model;

public class CantFindUserException extends Exception {

    public CantFindUserException(String user) {
        super(user);
    }
}
