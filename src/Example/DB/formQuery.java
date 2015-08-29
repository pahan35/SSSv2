package Example.DB;

/**
 * Created by пк on 03.07.2015.
 */
public class formQuery {
    public static void main(String[] args){
        String ss =  formQuery("da1", "da2", "da3", "da4", "da5");
        System.out.println(ss);

    }
    public static String formQuery(Query queryObject){
        return "SELECT * FROM " + queryObject.source + " WHERE Signal_Index = " + queryObject.target + " AND Sample_TDate_1 BETWEEN '" + queryObject.dateTimeBegin + "' AND '" + queryObject.dateTimeEnd + "'";
    }
    public static String formQuery(String source, String field1, String field2, String startValueField2, String endValueField2){
        String fQuery = "SELECT * FROM " + source + " WHERE " + field1 + " AND " + field2 + " BETWEEN '" + startValueField2 + "' AND '" + endValueField2 + "'";
        return fQuery;
    }
}
