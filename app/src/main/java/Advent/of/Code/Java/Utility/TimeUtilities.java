package Advent.of.Code.Java.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtilities {
    final static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static String getTimeAsString() {
        final Date date = new Date();
        return formatter.format(date);
    }

    public static long getCurrentMilliseconds() {
        return System.currentTimeMillis();
    }

    public static String getMillisecondTimeAsString(long milliseconds) {
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(min);
        long ms = milliseconds - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec);

        String minString = min < 10 ? "0" + min : "" + min;
        String secString = sec < 10 ? "0" + sec : "" + sec;

        return minString + ":" + secString + "s " + ms + "ms";
    }
}
