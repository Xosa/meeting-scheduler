package calendar.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BookingServiceTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src/main/java/calendar/samples/example.txt", "2011-03-21\n" +
                        "09:00 11:00 EMP002\n" +
                        "2011-03-22\n" +
                        "14:00 16:00 EMP003\n" +
                        "16:00 17:00 EMP004\n"},
                {"src/main/java/calendar/samples/example1.txt", "2011-03-22\n" +
                        "14:00 16:00 EMP004\n" +
                        "16:00 17:00 EMP003\n"},
                {"src/main/java/calendar/samples/example2.txt", "2011-03-22\n" +
                        "14:00 15:00 EMP004\n" +
                        "16:00 17:00 EMP006\n"},
                {"src/main/java/calendar/samples/example3.txt", "2011-03-22\n" +
                        "11:00 12:00 EMP006\n" +
                        "12:00 13:00 EMP005\n" +
                        "13:00 14:00 EMP004\n" +
                        "14:00 15:00 EMP003\n" +
                        "15:00 16:00 EMP002\n" +
                        "16:00 17:00 EMP001\n"},
                {"src/main/java/calendar/samples/example4.txt", "2011-03-22\n" +
                        "11:00 15:00 EMP006\n" +
                        "15:00 16:00 EMP002\n" +
                        "16:00 17:00 EMP001\n"},
                {"src/main/java/calendar/samples/example5.txt", "2011-03-22\n" +
                        "11:00 13:00 EMP004\n" +
                        "13:30 14:30 EMP002\n" +
                        "14:00 15:00 EMP003\n" +
                        "16:00 17:00 EMP001\n"},
                {"src/main/java/calendar/samples/example6.txt", "2011-03-22\n" +
                        "11:30 13:30 EMP004\n" +
                        "13:30 14:30 EMP002\n" +
                        "14:30 15:30 EMP006\n" +
                        "16:00 17:00 EMP001\n"}
        });
    }

    private String filePath;

    private String result;

    public BookingServiceTest(String filePath, String result) {
        this.filePath = filePath;
        this.result = result;
    }

    @Test
    public void bookRequests() {
        final BookingService service = new BookingService();
        assertEquals(result, service.bookRequests(filePath));
    }

}