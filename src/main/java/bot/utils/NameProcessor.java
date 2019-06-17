package bot.utils;

import bot.utils.StringHelper;
import lombok.Data;

import java.util.*;

@Data
public class NameProcessor {
    private NameProcessor() {}

    public static List<String> generateAcceptedNames(String str) {
        List<String> shortcutsShort = Arrays.asList("^al", "^os", "^gen", "im", "^dw", "^zaj", "^ks", "pld", "pln",
                "zach", "wsch ", "^pl", "^zajezdnia", "^cm");
        List<String> shortcutsLong = Arrays.asList("aleja", "osiedle", "generala", "imienia", "dworzec", "zajezdnia",
                "ksiedza", "poludnie", "polnoc", "zachodni", "wschodni", "plac", "zaj", "cmentarz");
        List<String> repetitiveNames = Arrays.asList("metro", "pl", "al", "plac", "dworzec", "dw", "im");

        List<String> extrasSource = Arrays.asList(" ii", "jana pawla ii", "wschodni");
        List<String> extrasGoal = Arrays.asList(" 2", "jp2", "wsch");

        str = StringHelper.sanitizeInput(str);

        List<String> res = new ArrayList<>();
        res.add(str);

        int len = shortcutsLong.size();
        for (int i = 0; i < len; i++) {
            String key = shortcutsShort.get(i);
            String val = shortcutsLong.get(i);
            String str2 = str.replaceAll(key + " ", val + " ");
            if (!res.contains(str2)) {
                res.add(str2);
            }
        }

        len = repetitiveNames.size();
        for (int i = 0; i < len; i++) {
            String key = repetitiveNames.get(i);
            String str2 = str.replaceAll(key + " ", "");
            if (!res.contains(str2)) {
                res.add(str2);
            }
        }

        for (ListIterator<String> iterator = res.listIterator(); iterator.hasNext();) {
            String a = iterator.next();
            len = extrasSource.size();
            for (int i = 0; i < len; i++) {
                String key = extrasSource.get(i);
                String val = extrasGoal.get(i);
                String str2 = a.replaceAll(key, val);
                if (!res.contains(str2)) {
                    iterator.add(str2);
                }
            }
        }
        return res;
    }
}
