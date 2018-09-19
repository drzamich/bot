package bot.processor;

import java.text.Normalizer;

public final class Utilities {
    private Utilities() {
        throw new UnsupportedOperationException();
    }
    public static String parseInput(String input) {
        input = input.toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        input = input.replaceAll("ł","l");
        input = input.replaceAll("[()]","");
        input = input.replaceAll("[.+-]"," ");
        input = input.replaceAll(".$","");
        input = removeExcessiveWhitespaces(input);
        input = input.trim();
        return input;
    }


    public static String removeExcessiveWhitespaces(String str){
        return str.replaceAll("\\s+"," ");
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}