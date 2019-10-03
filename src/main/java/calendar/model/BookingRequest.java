package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class BookingRequest implements Comparable<BookingRequest> {

    private static final DateTimeFormatter DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private String employeeId;
    private LocalDateTime submissionTime;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
    private LocalDate meetingDate;

    public BookingRequest() {
    }

    public BookingRequest(final String employeeId, final LocalDateTime submissionTime, final LocalDateTime meetingStartTime, final Long duration) {
        this.employeeId = employeeId;
        this.submissionTime = submissionTime;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingStartTime.plusHours(duration);
        this.meetingDate = meetingStartTime.toLocalDate();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public LocalDateTime getMeetingStartTime() {
        return meetingStartTime;
    }

    public LocalDateTime getMeetingEndTime() {
        return meetingEndTime;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public String getDescription() {
        return this.getMeetingStartTime().format(DISPLAY_TIME_FORMAT) + " " + this.getMeetingEndTime().format(DISPLAY_TIME_FORMAT) + " " + this.getEmployeeId();
    }

    @Override
    public int compareTo(final BookingRequest request) {
        if ((meetingStartTime.compareTo(request.getMeetingEndTime()) <= 0 && meetingEndTime.compareTo(request.getMeetingStartTime()) >= 0)) {
            if (meetingEndTime.compareTo(request.getMeetingStartTime()) == 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public static class OverlapComparator implements Comparator<BookingRequest> {
        public int compare(BookingRequest first, BookingRequest second) {
            if (first.compareTo(second) == 0 && first.getSubmissionTime().isBefore(second.getSubmissionTime())) {
                return 0;
            } else if (first.compareTo(second) == 0 && second.getSubmissionTime().isBefore(first.getSubmissionTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
