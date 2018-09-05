package bot;

import java.text.Normalizer;
import java.util.List;

public class Utilities {
    public static String parseInput(String input) {
        input = input.toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return input;
    }


    public static Platform getPlatformByDirection(List<Platform> platforms, String dirName) {
        for (Platform platform : platforms) {
            List<Direction> directions = platform.getDirections();
            for (Direction direction : directions) {
                if (direction.getDirNameAccepted().equals(dirName)) {
                    return platform;
                }
            }
        }
        return null;
    }
}