package bot.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NameProcessorTest {

    @Test
    public void givenSampleNames_expectOutputPredefinedArraysOfStrings() {
        String input = "Al.Zieleniecka";
        List<String> expected = Arrays.asList("al zieleniecka", "aleja zieleniecka", "zieleniecka");
        List<String> output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);

        input = "MURANÓW";
        expected = Arrays.asList("muranow");
        output = NameProcessor.generateAcceptedNames(input);
        assertEquals(expected, output);
    }
}
