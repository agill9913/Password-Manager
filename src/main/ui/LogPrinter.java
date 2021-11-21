package ui;

import model.Event;
import model.EventLog;

//Interface for printing the events from
public interface LogPrinter {

    //EFFECTS: Prints all events to the console
    static void printLog() {
        for (Event curr : EventLog.getInstance()) {
            System.out.println(curr.toString());
        }
    }
}
