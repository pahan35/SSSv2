package Example.DB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by пк on 04.07.2015.
 */
public class DateZH {


/*    public static void main(String[] args){
        String date = formatBDDateToString("14.01.2015  23:00:10", 553);
        long convertedDate = stringToLong(date);
        System.out.println(date);
        System.out.println(convertedDate);
        String date2 = formatBDDateToString("14.01.2015  23:05:10", 558);
        String date3 = formatBDDateToString("14.01.2015  23:07:40", 555);
        String date4 = formatBDDateToString("14.01.2015  23:09:10", 313);
        ArrayList<String> dateStringList = new ArrayList<String>();
        dateStringList.add(date);
        dateStringList.add(date2);
        dateStringList.add(date3);
        dateStringList.add(date4);
        ArrayList<Double> diff = differenceSecondList(dateStringList);
        for (String str : dateStringList){
            System.out.println(str);
        }
        for (Double dou : diff){
            System.out.println(dou);
        }
    }*/

    public static String formatBDDateToString(String dateTime, int millisecond) {
        // Объединение полей
        dateTime += "." + millisecond;
        return dateTime;
    }
    public static LocalDateTime formatBDDateToDate(String dateTime, int millisecond) {
        // Объединение полей
        dateTime += "." + millisecond;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss.SSS");
        LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
        return date;
    }
    public static LocalDateTime formatStringToDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss.SSS");
        LocalDateTime date = LocalDateTime.parse(inputDate, formatter);
        return date;
    }
    public static long stringToLong(String inputDate){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss.SSS");
        formatter2.setTimeZone(timeZone);
        long l = 0;
        try {
            Date date = formatter2.parse(inputDate);
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }
    public static double relativeTime(ArrayList<String> dateList, int currentIndex){
        LocalDateTime currentDate = formatStringToDate(dateList.get(currentIndex));
        long difference = stringToLong(dateList.get(currentIndex)) - stringToLong(dateList.get(0));
        double d = difference;
        return d;
    }
    public static ArrayList<Double> differenceSecondList(ArrayList<String> dateList){
        int i = 0;
        ArrayList<Double> differenceList = new ArrayList<Double>();
        differenceList.add(0D);
        double firstDate = stringToLong(dateList.get(0));
        for (String date: dateList){

            if (i != 0 && i < dateList.size()){
                differenceList.add(((stringToLong(date) - firstDate)) / 1000);
            }
            i++;
        }
        return differenceList;
    }
}
