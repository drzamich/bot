package bot.utils;

import com.google.common.collect.ImmutableMap;

import java.util.*;

public class NameProcessor {

    private NameProcessor() {}

    private static ImmutableMap<String, String> s = ImmutableMap.<String,String>builder()
                                                    .put("\\bal\\b", "aleja")
                                                    .put("\\bcm\\b", "cmentarz")
                                                    .put("\\bch\\b", "centrum handlowe")
                                                    .put("\\bdw\\b", "dworzec")
                                                    .put("\\bgen\\b", "generala")
                                                    .put("\\bgl\\b", "glowna")
                                                    .put("\\bim\\b", "imienia")
                                                    .put("\\bks\\b", "ksiedza")
                                                    .put("\\bos\\b", "osiedle")
                                                    .put("\\bpl\\b", "plac")
                                                    .put("\\bpld\\b", "poludnie")
                                                    .put("\\bpln\\b", "polnoc")
                                                    .put("\\bwch\\b", "wchod")
                                                    .put("\\bsw\\b", "swietego")
                                                    .put("\\bzach\\b", "zachod")
                                                    .put("\\bi$", "1")
                                                    .put("\\bii$", "2")
                                                    .put("\\biii$", "3")
                                                    .put("\\biv$", "4")
                                                    .put("\\bv$", "5")
                                                    .put("\\bvi$", "6")
                                                    .put("\\bvii$", "7")
                                                    .put("\\bviii$", "8")
                                                    .put("\\bix$", "9")
                                                    .put("\\bx$", "10")
                                                    .put("\\bmetro\\s", "")
                                                    .build();

    public static Set<String> generateAcceptedNames(String str) {
        Set<String> set = new HashSet<>();
        str = StringHelper.sanitizeInput(str);
        set.add(str);
        Set<String> newSet;

        while(true) {
            newSet = addAcceptedNames(set);
            if(set.containsAll(newSet)) return newSet;
            set = newSet;
        }
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
