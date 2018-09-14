package bot;

import java.text.Normalizer;

public class Utilities {
    public static String parseInput(String input) {
        input = input.toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return input;
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}