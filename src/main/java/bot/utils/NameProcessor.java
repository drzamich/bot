package bot.utils;

import java.util.*;

public class NameProcessor {
    private NameProcessor() {}

    private static Map<String, String> s;

    static {
        s = new HashMap<>();
        s.put("\\bal\\b", "aleja");
        s.put("\\bcm\\b", "cmentarz");
        s.put("\\bch\\b", "centrum handlowe");
        s.put("\\bdw\\b", "dworzec");
        s.put("\\bgen\\b", "generala");
        s.put("\\bgl\\b", "glowna");
        s.put("\\bim\\b", "imienia");
        s.put("\\bks\\b", "ksiedza");
        s.put("\\bos\\b", "osiedle");
        s.put("\\bpl\\b", "plac");
        s.put("\\bpld\\b", "poludnie");
        s.put("\\bpln\\b", "polnoc");
        s.put("\\bwsch\\b", "wschod");
        s.put("\\bsw\\b", "swietego");
        s.put("\\bzach\\b", "zachod");
        s.put("\\bi$", "1");
        s.put("\\bii$", "2");
        s.put("\\biii$", "3");
        s.put("\\biv$", "4");
        s.put("\\bv$", "5");
        s.put("\\bvi$", "6");
        s.put("\\bvii$", "7");
        s.put("\\bviii$", "8");
        s.put("\\bix$", "9");
        s.put("\\bx$", "10");
        s.put("\\bmetro\\s", "");
    }

    public static List<String> generateAcceptedNames(String str) {
        Set<String> set = new HashSet<>();
        str = StringHelper.sanitizeInput(str);
        set.add(str);
        Set<String> newSet;

        while(true) {
            newSet = addAcceptedNames(set);
            if(set.containsAll(newSet)) break;
            set = newSet;
        }

        return new ArrayList<>(set);
    }

    private static Set<String> addAcceptedNames(Set<String> baseSet) {
        Set<String> newSet = new HashSet<>(baseSet);
        for(String str: baseSet) {
            for (Map.Entry<String, String> item : s.entrySet()) {
                String newStr = str.replaceAll(item.getKey(),  item.getValue());
                newSet.add(newStr);
            }
        }
        return newSet;
    }
}
