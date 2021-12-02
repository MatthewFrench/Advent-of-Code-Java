package Advent.of.Code.Java.Utility.Structures;

public interface DayWithExecute extends Day {
    void run() throws Exception;
    void executeWithInput(final String fileName) throws Exception;
}
