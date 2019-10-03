package calendar.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class BookingRequestTest {

    @Test
    public void compareToWhenOverlapsReturns0() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 11, 00, 00), 1L);

        assertEquals(0, request1.compareTo(request2));
    }

    @Test
    public void compareToWhenDoesNotOverlapReturns1() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 12, 30, 00), 1L);

        assertEquals(1, request1.compareTo(request2));
    }

    @Test
    public void compareToEdgeCaseReturns1() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 11, 30, 00), 1L);

        assertEquals(1, request1.compareTo(request2));
    }

    @Test
    public void compareToWhenExactlyTheSameTimeReturns0() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);

        assertEquals(0, request1.compareTo(request2));
    }

    @Test
    public void overlapAndFirstHasPriorityReturnsO() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 11, 00, 00), 1L);

        final int overlap = new BookingRequest.OverlapComparator().compare(request1, request2);
        assertEquals(0, overlap);
    }

    @Test
    public void overlapAndSecondHasPriorityReturns1() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 11, 00, 00), 1L);

        final int overlap = new BookingRequest.OverlapComparator().compare(request1, request2);
        assertEquals(1, overlap);
    }

    @Test
    public void noOverlapReturnsMinus1() {
        final BookingRequest request1 = new BookingRequest("EMP001", LocalDateTime.of(2019, 9, 28, 11, 00, 00), LocalDateTime.of(2019, 9, 30, 10, 30, 00), 1L);
        final BookingRequest request2 = new BookingRequest("EMP002", LocalDateTime.of(2019, 9, 28, 10, 00, 00), LocalDateTime.of(2019, 9, 30, 11, 30, 00), 1L);

        final int overlap = new BookingRequest.OverlapComparator().compare(request1, request2);
        assertEquals(-1, overlap);
    }
}