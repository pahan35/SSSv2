package Example.DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Query {
    public String source;
    public int sourceID;
    public int target;
    public String dateTimeBegin;
    public String dateTimeEnd;
    public String targetNameQuery;
    public String targetName;
    private String getQuery;
    public ArrayList<Double> valueList = new ArrayList<Double>();
    public ArrayList<Integer> mSecList = new ArrayList<Integer>();
    public ArrayList<String> dateTimeList = new ArrayList<String>();
    public ArrayList<String> dateTimeStringList = new ArrayList<String>();
    public ArrayList<Long> longTimeList = new ArrayList<Long>();

    public String getGetQuery() {
        return getQuery;
    }

    public Query(String source, int sourceID, int target,  String dateTimeBegin, String dateTimeEnd){
        this.dateTimeEnd = dateTimeEnd;
        this.dateTimeBegin = dateTimeBegin;
        this.target = target;
        this.source = source;
        this.sourceID = sourceID;
        this.targetNameQuery = "SELECT Description FROM " + source + "_tags WHERE Tag_Index = " + target;
        this.getQuery = "SELECT * FROM " +  source + "_" + sourceID + " WHERE Signal_Index = " + target + " AND Sample_TDate_1 BETWEEN '" + dateTimeBegin + "' AND '" + dateTimeEnd + "'";
    }
    public void formDateTimeList(){
        for (int i = 0; i < dateTimeList.size(); i++) {
            dateTimeStringList.add(DateZH.formatBDDateToString(this.dateTimeList.get(i), this.mSecList.get(i)));
            longTimeList.add(stringToLong(dateTimeStringList.get(i)));
        }
    }
    public static long stringToLong(String inputDate) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        formatter2.setTimeZone(timeZone);
        long l = 0L;

        try {
            Date e = formatter2.parse(inputDate);
            l = e.getTime();
        } catch (ParseException var6) {
            var6.printStackTrace();
        }
        return l;
    }
}
