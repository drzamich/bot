package bot.externalservice.general;

import bot.processor.Utilities;
import lombok.Data;

import java.util.*;

@Data
public class NameProcessor {
    private List<String> acceptedNames;

    public NameProcessor(String str){
        this.acceptedNames = generateAcceptedNames(str);
    }

    private List<String> generateAcceptedNames(String str) {
        List<String> shortcutsShort = Arrays.asList("^al", "^os", "^gen", "im", "^dw", "^zaj", "^ks", "pld", "pln",
                "zach", "wsch ", "^pl", "^zajezdnia", "^cm");
        List<String> shortcutsLong = Arrays.asList("aleja", "osiedle", "generala", "imienia", "dworzec", "zajezdnia",
                "ksiedza", "poludnie", "polnoc", "zachodni", "wschodni", "plac", "zaj", "cmentarz");
        List<String> repetitiveNames = Arrays.asList("metro", "pl", "al", "plac", "dworzec", "dw", "im");

        List<String> extrasSource = Arrays.asList(" ii", "jana pawla ii", "wschodni");
        List<String> extrasGoal = Arrays.asList(" 2", "jp2","wsch");

        str = Utilities.parseInput(str);



        List<String> res = new ArrayList<>();
        res.add(str);


        int len = shortcutsLong.size();
        for (int i = 0; i < len; i++) {
            String key = shortcutsShort.get(i);
            String val = shortcutsLong.get(i);
            String str2 = str.replaceAll(key+" ", val+" ");
            if (!res.contains(str2)) {
                res.add(str2);
            }
//            if (!res.contains(str3)) {
//                res.add(str3);
//            }
        }

        len = repetitiveNames.size();
        for (int i = 0; i < len; i++) {
            String key = repetitiveNames.get(i);
            String str2 = str.replaceAll(key+" ", "");
            //String str3 = str.replaceAll(key,"");
            if (!res.contains(str2)) {
                res.add(str2);
            }
        }



        for (ListIterator<String> iterator = res.listIterator(); iterator.hasNext(); ) {
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
