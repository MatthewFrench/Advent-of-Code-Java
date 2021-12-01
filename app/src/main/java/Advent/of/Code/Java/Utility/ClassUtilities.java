package Advent.of.Code.Java.Utility;

import Advent.of.Code.Java.Utility.Structures.Day;
import com.google.common.collect.Iterables;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ClassUtilities {
    public static List<Day> getAllDays() throws Exception {
        return getDayClassInstances("Advent.of.Code.Java");
    }

    public static List<Day> getAllDaysInYear(final String year) throws Exception {
        return getDayClassInstances("Advent.of.Code.Java.Year_" + year);
    }

    public static Day getSpecificDay(final String year, final String day) throws Exception {
        final List<Day> days = getDayClassInstances("Advent.of.Code.Java.Year_" + year);
        for (final Day dayObject : days) {
            if (dayObject.getClass().getName().endsWith(day)) {
                return dayObject;
            }
        }
        return null;
    }

    public static List<Day> getDayClassInstances(final String prefix) throws Exception {
        final List<Class<? extends Day>> classes = getDayClasses(prefix);
        final List<Day> days = new ArrayList<>();
        for (final Class<? extends Day> dayClass : classes) {
            days.add(dayClass.getConstructor().newInstance());
        }
        return days;
    }

    // Todo, make the string parsing simpler with an easy library
    // Like "*Year_{1}.Day_{2}.*" and it returns a list with 1 and 2
    public static List<Class<? extends Day>> getDayClasses(final String prefix) {
        Reflections reflections = new Reflections(prefix);
        return reflections.getSubTypesOf(Day.class).stream().sorted((class1, class2) -> {
            // Sort the values by year and day so they are in order
            final int class1Day = Integer.parseInt(
                    Iterables.getLast(
                            StringUtilities.splitStringIntoList(class1.getCanonicalName(), "_")
                    )
            );
            final int class2Day = Integer.parseInt(
                    Iterables.getLast(
                            StringUtilities.splitStringIntoList(class2.getCanonicalName(), "_")
                    )
            );
            final int class1Year = Integer.parseInt(
                    StringUtilities.splitStringIntoList(
                            Iterables.getLast(StringUtilities.splitStringIntoList(class1.getCanonicalName(), ".Year_"))
                            , ".").get(0)
            );
            final int class2Year = Integer.parseInt(
                    StringUtilities.splitStringIntoList(
                            Iterables.getLast(StringUtilities.splitStringIntoList(class2.getCanonicalName(), ".Year_"))
                            , ".").get(0)
            );
            if (class1Year != class2Year) {
                return Integer.compare(class1Year, class2Year);
            }
            return Integer.compare(class1Day, class2Day);
        }).toList();
    }
}
