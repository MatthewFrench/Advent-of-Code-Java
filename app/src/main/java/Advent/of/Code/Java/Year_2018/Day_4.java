package Advent.of.Code.Java.Year_2018;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day_4 implements Day {
    public void run() throws Exception {
        final String originalInput = LoadUtilities.loadTextFileAsString("input/2018/4/input.txt");
        String[] splitInput = originalInput.split("\n");

        Arrays.sort(splitInput, (string1, string2) -> {
            String year1 = string1.substring(1, string1.indexOf("-"));
            string1 = string1.substring(string1.indexOf("-") + 1);
            String month1 = string1.substring(0, string1.indexOf("-"));
            string1 = string1.substring(string1.indexOf("-") + 1);
            String day1 = string1.substring(0, string1.indexOf(" "));
            string1 = string1.substring(string1.indexOf(" ") + 1);
            String hour1 = string1.substring(0, string1.indexOf(":"));
            string1 = string1.substring(string1.indexOf(":") + 1);
            String minute1 = string1.substring(0, string1.indexOf("]"));

            String year2 = string2.substring(1, string2.indexOf("-"));
            string2 = string2.substring(string2.indexOf("-") + 1);
            String month2 = string2.substring(0, string2.indexOf("-"));
            string2 = string2.substring(string2.indexOf("-") + 1);
            String day2 = string2.substring(0, string2.indexOf(" "));
            string2 = string2.substring(string2.indexOf(" ") + 1);
            String hour2 = string2.substring(0, string2.indexOf(":"));
            string2 = string2.substring(string2.indexOf(":") + 1);
            String minute2 = string2.substring(0, string2.indexOf("]"));

            return LocalDateTime.parse(year1+"-"+month1+"-"+day1+"T"+hour1+":"+minute1+":00")
                    .compareTo(LocalDateTime.parse(year2+"-"+month2+"-"+day2+"T"+hour2+":"+minute2+":00"));
        });

        //Now that the array is sorted
        //Find the guard with the most sleep
        //Then find the minute that the guard sleeps the most
        //Then multiply the guard id by the minute

        //Guard ID, Minute, Number of Times
        HashMap<String, HashMap<String, Integer>> guardMap = new HashMap<>();

        /*
        [1518-02-03 23:57] Guard #509 begins shift
        [1518-02-04 00:13] falls asleep
        [1518-02-04 00:14] wakes up
        [1518-02-04 00:24] falls asleep
        [1518-02-04 00:49] wakes up
         */

        String guardID = "";
        String sleepStartHour = "";
        String sleepStartMinute = "";
        for (String piece : splitInput) {
            if (piece.contains("begins shift")) {
                guardID = piece.substring(piece.indexOf("#")+1, piece.indexOf(" ", piece.indexOf("#")));
            }
            if (piece.contains("falls asleep")) {
                sleepStartHour = piece.substring(piece.indexOf(" ")+1, piece.indexOf(":"));
                sleepStartMinute = piece.substring(piece.indexOf(":")+1, piece.indexOf("]"));
            }
            if (piece.contains("wakes up")) {
                String sleepEndHour = piece.substring(piece.indexOf(" ")+1, piece.indexOf(":"));
                String sleepEndMinute = piece.substring(piece.indexOf(":")+1, piece.indexOf("]"));

                int sleepStartHourInt = Integer.parseInt(sleepStartHour);
                int sleepStartMinuteInt = Integer.parseInt(sleepStartMinute);
                int sleepEndHourInt = Integer.parseInt(sleepEndHour);
                int sleepEndMinuteInt = Integer.parseInt(sleepEndMinute);
                while(sleepStartHourInt != sleepEndHourInt || sleepStartMinuteInt != sleepEndMinuteInt) {
                    if (!guardMap.containsKey(guardID)) {
                        guardMap.put(guardID, new HashMap<>());
                    }
                    HashMap<String, Integer> minuteMap = guardMap.get(guardID);
                    if (!minuteMap.containsKey(sleepStartMinuteInt+"")) {
                        minuteMap.put(sleepStartMinuteInt+"", 1);
                    } else {
                        minuteMap.put(sleepStartMinuteInt+"", minuteMap.get(sleepStartMinuteInt+"") + 1);
                    }
                    //Mark sleep
                    sleepStartMinuteInt++;
                    if (sleepStartMinuteInt >= 60) {
                        sleepStartMinuteInt -= 60;
                        sleepStartHourInt++;
                        if (sleepStartHourInt >= 24) {
                            sleepStartHourInt -= 24;
                        }
                    }
                }
            }
        }

        LogUtilities.log("Number of guards: " + guardMap.size());

        LogUtilities.log("Part 1");

        String mostSleepyGuardId = "";
        int maxMostSleptMinute = 0;
        int maxMostSleptMinuteTimes = 0;
        int maxMostSleptMinutesTotal = 0;

        for (Map.Entry<String, HashMap<String, Integer>> e : guardMap.entrySet()) {
            String guardId = e.getKey();
            HashMap<String, Integer> sleepMap = e.getValue();
            int minutesAsleep = 0;
            int minuteMostSlept = 0;
            int minuteMostSleptTimes = 0;
            for (Map.Entry<String, Integer> e2 : sleepMap.entrySet()) {
                String minute = e2.getKey();
                int timesAsleep = e2.getValue();
                minutesAsleep += timesAsleep;
                if (timesAsleep > minuteMostSleptTimes) {
                    minuteMostSleptTimes = timesAsleep;
                    minuteMostSlept = Integer.parseInt(minute);
                }
            }
            if (minutesAsleep > maxMostSleptMinutesTotal) {
                maxMostSleptMinutesTotal = minutesAsleep;
                maxMostSleptMinuteTimes = minuteMostSleptTimes;
                maxMostSleptMinute = minuteMostSlept;
                mostSleepyGuardId = guardId;
            }
        }

        LogUtilities.log("Most sleep guard: " + mostSleepyGuardId + " with minute: " + maxMostSleptMinute +
                " with sleep time for that mintues: " + maxMostSleptMinuteTimes + ", with total sleep time: " + maxMostSleptMinutesTotal);
        LogUtilities.log("Id of guard * minute = " + (Integer.parseInt(mostSleepyGuardId) * maxMostSleptMinute));



        LogUtilities.log("Part 2");

        mostSleepyGuardId = "";
        maxMostSleptMinute = 0;
        maxMostSleptMinuteTimes = 0;
        maxMostSleptMinutesTotal = 0;

        for (Map.Entry<String, HashMap<String, Integer>> e : guardMap.entrySet()) {
            String guardId = e.getKey();
            HashMap<String, Integer> sleepMap = e.getValue();
            int minutesAsleep = 0;
            int minuteMostSlept = 0;
            int minuteMostSleptTimes = 0;
            for (Map.Entry<String, Integer> e2 : sleepMap.entrySet()) {
                String minute = e2.getKey();
                int timesAsleep = e2.getValue();
                minutesAsleep += timesAsleep;
                if (timesAsleep > minuteMostSleptTimes) {
                    minuteMostSleptTimes = timesAsleep;
                    minuteMostSlept = Integer.parseInt(minute);
                }
            }
            if (minuteMostSleptTimes > maxMostSleptMinuteTimes) {
                maxMostSleptMinutesTotal = minutesAsleep;
                maxMostSleptMinuteTimes = minuteMostSleptTimes;
                maxMostSleptMinute = minuteMostSlept;
                mostSleepyGuardId = guardId;
            }
        }

        LogUtilities.log("Most minute sleep guard: " + mostSleepyGuardId + " with minute: " + maxMostSleptMinute +
                " with sleep time for that mintues: " + maxMostSleptMinuteTimes + ", with total sleep time: " + maxMostSleptMinutesTotal);
        LogUtilities.log("Id of guard * minute = " + (Integer.parseInt(mostSleepyGuardId) * maxMostSleptMinute));
    }
}