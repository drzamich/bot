package bot.externalservice.general;

import bot.processor.Utilities;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class NameProcessor {
    private List<String> acceptedNames;


    public NameProcessor(String str){
        this.acceptedNames = generateAcceptedNames(str);
    }

    private List<String> generateAcceptedNames(String str) {
        List<String> shortcutsShort = Arrays.asList("^al", "^os", "^gen", "im ", "dw", "^zaj", "^ks", "pld", "pln", "zach", "wsch",
                "ii", "iii", "vi", "jana pawla ii", "jana pawla 2", "zajezdnia");
        List<String> shortcutsLong = Arrays.asList("aleja", "osiedle", "generala", "imienia ", "dworzec", "zajezdnia",
                "ksiedza", "poludnie", "polnoc", "zachodnia", "wschodni", "2", "3", "6", "jp2",
                "jp2", "zaj");
        List<String> repetitiveNames = Arrays.asList("metro", "pl", "al", "aleja", "plac", "dworzec");

        str = Utilities.parseInput(str);
        List<String> res = new ArrayList<>();
        res.add(str);
        String str3 = str;
        int len = shortcutsLong.size();
        for (int i = 0; i < len; i++) {
            String key = shortcutsShort.get(i);
            String val = shortcutsLong.get(i);
            String str2 = str.replaceAll(key, val);
            str3 = str3.replaceAll(key, val);
            if (!res.contains(str2)) {
                res.add(str2);
            }
            if (!res.contains(str3)) {
                res.add(str3);
            }
        }

        int len2 = res.size();
        for (int i = 0; i < len2; i++) {
            for (String key : repetitiveNames) {
                String str2 = res.get(i).replaceAll(key + " ", "").trim();
                if (!res.contains(str2) && !str.equals(str2)) {
                    res.add(str2);
                }
            }
        }

        return res;
    }
}
