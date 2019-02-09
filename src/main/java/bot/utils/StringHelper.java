package bot.utils;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringHelper {
    private StringHelper() {
    }

    public static String sanitizeInput(String input) {
        input = input.toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        input = input.replaceAll("ł", "l");
        input = input.replaceAll("[()]", "");
        input = input.replaceAll("[.+-]", " ");
        input = input.replaceAll("\\.$", "");
        input = input.replaceAll("\"", "");
        input = removeExcessiveWhitespaces(input);
        input = input.trim();
        return input;
    }


    public static String removeExcessiveWhitespaces(String str) {
        return str.replaceAll("\\s+", " ");
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getTime(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static long getTimeDifferenceInMilliseconds(String time1, String time2, String pattern) {
        long diff;
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date d1 = dateFormat.parse(time1);
            Date d2 = dateFormat.parse(time2);
            diff = d2.getTime() - d1.getTime();
        } catch (Exception e) {
            diff = 0;
        }
        return diff;
    }
}
