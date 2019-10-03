package calendar;

import calendar.service.BookingService;

public class Scheduler {
    public static void main(String[] args) {
        final BookingService service = new BookingService();
        final String path;
        if (args.length > 0) {
            path = "calendar/samples/example.txt";
        } else {
            path = "src/main/java/calendar/samples/example.txt";
        }
        System.out.println(service.bookRequests(path));
    }
}
