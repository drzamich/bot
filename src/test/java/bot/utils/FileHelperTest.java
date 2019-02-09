package bot.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FileHelperTest {

    @Test
    public void whenFileExists_readFileAndIgnoreComments() {
        List<String> expected = Arrays.asList("Centrum", "Muranów");
        List<String> result = FileHelper.readFile("src/test/resources/filehelper/fetchedStations");
        assertEquals(expected, result);
    }

    @Test
    public void whenFileDoesNotExist_expectEmptyContent() {
        List<String> expected = Collections.emptyList();
        List<String> result = FileHelper.readFile("src/test/resources/filehelper/nonExistingFile");
        assertEquals(expected, result);
    }

}