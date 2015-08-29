package Example.DB;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeWork {

    public static void main(String[] args) {

        TimeZone timezone = TimeZone.getTimeZone("UTC");

        List<Long> longs = new ArrayList<Long>();
        List<String> strings = new ArrayList<String>();

        //Formatting a date needs a timezone - otherwise the date get formatted to your system time zone.
        //Use 24h format HH. In 12h format hh can be in range 0-11, which makes 12 overflow to 0.
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        formatter.setTimeZone(timezone);

        Date now = new Date();

        //Test dates
        strings.add(formatter.format(now));
        strings.add("01-01-1970 00:00:00.000");
        strings.add("01-01-1970 00:00:01.000");
        strings.add("01-01-1970 00:01:00.000");
        strings.add("01-01-1970 01:00:00.000");
        strings.add("01-01-1970 10:00:00.000");
        strings.add("01-01-1970 12:00:00.000");
        strings.add("01-01-1970 24:00:00.000");
        strings.add("02-01-1970 00:00:00.000");
        strings.add("01-01-1971 00:00:00.000");
        strings.add("01-01-2014 00:00:00.000");
        strings.add("31-12-1969 23:59:59.000");
        strings.add("31-12-1969 23:59:00.000");
        strings.add("31-12-1969 23:00:00.000");

        //Test data
        longs.add(now.getTime());
        longs.add(-1L);
        longs.add(0L); //Long date presentation at - midnight 1/1/1970 UTC - The timezone is important!
        longs.add(1L);
        longs.add(1000L);
        longs.add(60000L);
        longs.add(3600000L);
        longs.add(36000000L);
        longs.add(43200000L);
        longs.add(86400000L);
        longs.add(31536000000L);
        longs.add(1388534400000L);
        longs.add(7260000L);
        longs.add(1417706084037L);
        longs.add(-7260000L);

        System.out.println("===== String to long =====");

        //Show the long value of the date
        for (String string: strings) {
            try {
                Date date = formatter.parse(string);
                System.out.println("Formated date : " + string + " = Long = " + date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        System.out.println("===== Long to String =====");

        //Show the date behind the long
        for (Long lo : longs) {
            Date date = new Date(lo);
            String string = formatter.format(date);
            System.out.println("Formated date : " + string + " = Long = " + lo);
        }
    }
}