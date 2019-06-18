package bot.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class NameProcessorTest {

    private String input;
    private List<String> expected;
    private List<String> output;

    private void runTest() {
        output = NameProcessor.generateAcceptedNames(input);
        Collections.sort(expected);
        Collections.sort(output);
        assertEquals(output.containsAll(expected), expected.containsAll(output));
//        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings001() {
        input = "Al.Zieleniecka";
        expected = Arrays.asList("al zieleniecka", "aleja zieleniecka");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings002() {
        input = "MURANÓW";
        expected = Arrays.asList("muranow");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings003() {
        input = "Marymont-Potok";
        expected = Arrays.asList("marymont potok");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings004() {
        input = "Metro Ratusz Arsenał";
        expected = Arrays.asList("metro ratusz arsenal", "ratusz arsenal");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings005() {
        input = "Małcużyńskiego";
        expected = Arrays.asList("malcuzynskiego");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings006() {
        input = "Cm.Żydowski";
        expected = Arrays.asList("cm zydowski", "cmentarz zydowski");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings007() {
        input = "Al.Piłsudskiego";
        expected = Arrays.asList("al pilsudskiego", "aleja pilsudskiego");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings008() {
        input = "Grupy AK \"Północ\"";
        expected = Arrays.asList("grupy ak polnoc");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings009() {
        input = "Al.Jana Pawła II";
        expected = Arrays.asList("al jana pawla ii, al jana pawla 2, aleja jana pawla 2, aleja jana pawla ii");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings010() {
        input = "Armii Krajowej";
        expected = Arrays.asList("armii krajowej");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings011() {
        input = "Pl.Jana Pawła II";
        expected = Arrays.asList("pl jana pawla ii", "plac jana pawla ii", "pl jana pawla 2", "plac jana pawla 2");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings012() {
        input = "Pl.Unii Lubelskiej";
        expected = Arrays.asList("pl unii lubelskiej", "plac unii lubelskiej");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings013() {
        input = "Praga-Płd.-Ratusz";
        expected = Arrays.asList("praga pld ratusz", "praga poludnie ratusz");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings014() {
        input = "Powązki-Cm.Wojskowy";
        expected = Arrays.asList("powazki cm wojskowy", "powazki cmentarz wojskowy");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings015() {
        input = "Przemysława II";
        expected = Arrays.asList("przemyslawa ii", "przemyslawa 2");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings016() {
        input = "Rondo Ks.J.Popieluszki";
        expected = Arrays.asList("rondo ks j popieluszki", "rondo ksiedza j popieluszki");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings017() {
        input = "KS Polonez";
        expected = Arrays.asList("ks polonez", "ksiedza polonez");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings018() {
        input = "Dw.Centralny";
        expected = Arrays.asList("dw centralny", "dworzec centralny");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings019() {
        input = "Dw.Wschodni (Kijowska)";
        expected = Arrays.asList("dw wschodni kijowska", "dworzec wschodni kijowska");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings020() {
        input = "Os.Lewandów II";
        expected = Arrays.asList("os lewandow ii", "osiedle lewandow ii", "os lewandow 2", "osiedle lewandow 2");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings021() {
        input = "Św.A.Boboli";
        expected = Arrays.asList("sw a boboli", "swietego a boboli");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings022() {
        input = "CH Blue City";
        expected = Arrays.asList("ch blue city", "centrum handlowe blue city");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings023() {
        input = "Os.Derby III";
        expected = Arrays.asList("os derby iii", "osiedle derby iii", "os derby 3", "osiedle derby 3");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings024() {
        input = "Cm.Południowy-Brama Gł.";
        expected = Arrays.asList("cm poludniowy brama gl", "cmentarz poludniowy brama gl", "cmentarz poludniowy brama glowna", "cm poludniowy brama glowna");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings025() {
        input = "Cm.Południowy-Brama Płd.";
        expected = Arrays.asList("cm poludniowy brama pld", "cmentarz poludniowy brama pld", "cm poludniowy brama poludnie", "cmentarz poludniowy brama poludnie");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings026() {
        input = "Ratuszowa-ZOO";
        expected = Arrays.asList("ratuszowa zoo");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings027() {
        input = "Ogród Działkowy Im.Warneńczyka";
        expected = Arrays.asList("ogrod dzialkowy im warnenczyka", "ogrod dzialkowy imienia warnenczyka");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings028() {
        input = "Os.Derby I";
        expected = Arrays.asList("os derby i", "os derby 1", "osiedle derby i", "osiedle derby 1");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings029() {
        input = "Warszawa Żwirki I Wigury";
        expected = Arrays.asList("warszawa zwirki i wigury");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings030() {
        input = "Gen.Zajączka";
        expected = Arrays.asList("gen zajaczka", "generala zajaczka");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings031() {
        input = "Zajezdnia Ursus Płn.";
        expected = Arrays.asList("zajezdnia ursus pln", "zajezdnia ursus polnoc");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings032() {
        input = "MERA-PNEFAL";
        expected = Arrays.asList("mera pnefal");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings033() {
        input = "Ku Słońcu";
        expected = Arrays.asList("ku sloncu");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings034() {
        input = "ks.Skorupki";
        expected = Arrays.asList("ks skorupki", "ksiedza skorupki");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings035() {
        input = "Dywizjonu 303";
        expected = Arrays.asList("dywizjonu 303");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings036() {
        input = "1 Sierpnia";
        expected = Arrays.asList("1 sierpnia");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings037() {
        input = "1.Praskiego Pułku";
        expected = Arrays.asList("1 praskiego pulku");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings038() {
        input = "Osmańska-DHL";
        expected = Arrays.asList("osmanska dhl");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings039() {
        input = "PKP Wola (Wolska)";
        expected = Arrays.asList("pkp wola wolska");
        runTest();
    }

    @Test
    public void givenSampleName_expectArrayOfStrings040() {
        input = "Cmentarz 1920 r.";
        expected = Arrays.asList("cmentarz 1920 r");
        runTest();
    }
}
