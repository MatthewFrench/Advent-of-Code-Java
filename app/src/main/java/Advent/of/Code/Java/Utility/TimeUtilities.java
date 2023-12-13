package Advent.of.Code.Java.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilities {
    final static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static String getTimeAsString() {
        final Date date = new Date();
        return formatter.format(date);
    }
}
