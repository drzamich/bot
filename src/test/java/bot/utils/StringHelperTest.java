package bot.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringHelperTest {

    @Test
    public void givenTwoDifferentTimeStrings_expectTimeDifferenceInMilliseconds() {
        String time1 = "16:34:22";
        String time2 = "17:16:39";
        String pattern = "HH:mm:ss";

        long timeDifferenceExpected = 2537000;
        long calculatedTimeDifference = StringHelper.getTimeDifferenceInMilliseconds(time1, time2, pattern);

        assertEquals(timeDifferenceExpected, calculatedTimeDifference);
    }

    @Test
    public void givenTwoIdenticalTimeStrings_expectTimeDifferenceInMillisecondsToBeZero() {
        String time1 = "16:34:22";
        String time2 = "16:34:22";
        String pattern = "HH:mm:ss";

        long timeDifferenceExpected = 0;
        long calculatedTimeDifference = StringHelper.getTimeDifferenceInMilliseconds(time1, time2, pattern);
        assertEquals(timeDifferenceExpected, calculatedTimeDifference);
    }

}