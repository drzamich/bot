package bot.utils;

import bot.utils.StringHelper;
import lombok.Data;

import java.util.*;

@Data
public class NameProcessor {
    private NameProcessor() {
    }

    private static Map<String, String> s;
    private static Map<String, String> n;

    static {
        s = new HashMap<>();
        s.put("al", "aleja");
        s.put("cm", "cmentarz");
        s.put("dw", "dworzec");
        s.put("gen", "generala");
        s.put("im", "imienia");
        s.put("ks", "ksiedza");
        s.put("os", "osiedle");
        s.put("pl", "plac");
        s.put("pld", "poludnie");
        s.put("pln", "polnoc");
        s.put("wsch", "wschod");
        s.put("zach", "zachod");
    }

    static {
        n = new HashMap<>();
        n.put("i", "1");
        n.put("ii", "2");
        n.put("iii", "3");
        n.put("iv", "4");
        n.put("v", "5");
        n.put("vi", "6");
        n.put("vii", "7");
        n.put("viii", "8");
        n.put("ix", "9");
        n.put("x", "10");
    }

    public static List<String> generateAcceptedNames(String str) {
        Set<String> set = new HashSet<>();
        str = StringHelper.sanitizeInput(str);

        set.add(str);

        for (Map.Entry<String, String> item : s.entrySet()) {
            String regex = "\\b" + item.getKey() + "\\b";
            String newStr = str.replaceAll(regex, item.getValue());
            set.add(newStr);
        }

        for(int i = 0; i<set.size(); i++) {
            str = set.
            for (Map.Entry<String, String> item : n.entrySet()) {
                String regex = "\\b" + item.getKey() + "$";
                String newStr = str.replaceAll(regex, item.getValue());
                set.add(newStr);
            }
        }


        List<String> res = new ArrayList<>();
        res.addAll(set);
        return res;
    }
}
