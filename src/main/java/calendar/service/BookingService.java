package calendar.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import calendar.model.BookingRequest;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;

public class BookingService {

    private static final DateTimeFormatter SUBMISSION_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter MEETING_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String bookRequests(final String path) {
        final Map<LocalDate, List<BookingRequest>> groupedByMeetingDate = extractRequests(path).stream().collect(groupingBy(BookingRequest::getMeetingDate, Collectors.mapping(Function.identity(), Collectors.toList())));
        final SortedSet<LocalDate> sortedByMeetingDate = new TreeSet<>(groupedByMeetingDate.keySet());
        final StringBuilder sb = new StringBuilder();

        for (LocalDate date : sortedByMeetingDate) {
            sb.append(date.format(DISPLAY_DATE_FORMAT)).append("\n");

            final List<BookingRequest> sortedByMeetingTime = groupedByMeetingDate.get(date).stream().sorted(Comparator.comparing(BookingRequest::getMeetingStartTime)).collect(Collectors.toList());
            final ListIterator<BookingRequest> it = sortedByMeetingTime.listIterator();

            final LinkedHashSet<BookingRequest> bookings = new LinkedHashSet<>();

            while (it.hasNext()) {
                final BookingRequest current = it.next();
                if (it.hasNext()) {
                    final BookingRequest next = it.next();
                    final int overlap = new BookingRequest.OverlapComparator().compare(current, next);
                    if (overlap == 0) {
                        it.previous();
                        it.remove();
                        it.previous();
                    } else if (overlap > 0) {
                        it.previous();
                        it.previous();
                        it.remove();
                        bookings.remove(current);
                        while (it.hasPrevious()) {
                            it.previous();
                        }
                    } else {
                        bookings.addAll(asList(current, next));
                        it.previous();
                        it.previous();
                        it.remove();
                        it.next();
                    }
                } else {
                    it.previous();
                    if (it.hasPrevious()) {
                        it.previous();
                    }
                    final BookingRequest previous = it.next();
                    final int overlap = new BookingRequest.OverlapComparator().compare(previous, current);
                    if (overlap > 0) {
                        bookings.remove(previous);
                        bookings.add(current);
                    } else if (overlap < 0) {
                        bookings.add(current);
                    }
                    if (it.hasNext())
                        it.next();
                }
            }

            for (BookingRequest request : bookings) {
                sb.append(request.getDescription()).append("\n");
            }
        }
        return sb.toString();
    }

    private List<BookingRequest> extractRequests(final String path) {
        final List<BookingRequest> list = new ArrayList<>();
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(path));
            String line = bufferReader.readLine();

            final String[] officeHours = line.split(" ");
            final LocalTime officeHoursStart = LocalTime.parse(officeHours[0], DateTimeFormatter.ofPattern("HHmm"));
            final LocalTime officeHoursEnd = LocalTime.parse(officeHours[1], DateTimeFormatter.ofPattern("HHmm"));

            while ((line = bufferReader.readLine()) != null) {
                final LocalDateTime submissionTime = LocalDateTime.parse(line.substring(0, line.lastIndexOf(" ")), SUBMISSION_FORMAT);
                final String employeeId = line.substring(line.lastIndexOf(" ") + 1);
                line = bufferReader.readLine();
                final LocalDateTime meetingStartTime = LocalDateTime.parse(line.substring(0, line.lastIndexOf(" ")), MEETING_FORMAT);
                final Long duration = Long.valueOf(line.substring(line.lastIndexOf(" ") + 1));
                if (meetingStartTime.toLocalTime().compareTo(officeHoursStart) >= 0 && meetingStartTime.plusHours(duration).toLocalTime().compareTo(officeHoursEnd) <= 0) {
                    list.add(new BookingRequest(employeeId, submissionTime, meetingStartTime, duration));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}

