package bot.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NameProcessorTest {

    private String input;
    private List<String> expected;
    private List<String> output;

    @Test
    public void givenSampleName_expectArrayOfStrings001() {
        input = "Al.Zieleniecka";
        expected = Arrays.asList("al zieleniecka", "aleja zieleniecka", "zieleniecka");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings002() {
        input = "MURANÓW";
        expected = Arrays.asList("muranow");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings003() {
        input = "Marymont-Potok";
        expected = Arrays.asList("marymont potok");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings004() {
        input = "Metro Ratusz Arsenał";
        expected = Arrays.asList("metro ratusz arsenal", "ratusz arsenal");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings005() {
        input = "Małcużyńskiego";
        expected = Arrays.asList("malcuzynskiego");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings006() {
        input = "Cm.Żydowski";
        expected = Arrays.asList("cm zydowski", "cmentarz zydowski");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings007() {
        input = "Al.Piłsudskiego";
        expected = Arrays.asList("al pilsudskiego", "aleja piłsudskiego");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings008() {
        input = "Grupy AK \"Północ\"";
        expected = Arrays.asList("grupy ak polnoc");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings009() {
        input = "Al.Jana Pawła II";
        expected = Arrays.asList("al jana pawla ii, al jana pawla 2, jana pawla 2, jana pawla ii");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }

    @Test
    public void givenSampleName_expectArrayOfStrings010() {
        input = "Armii Krajowej";
        expected = Arrays.asList("armii krajowej");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }
}
