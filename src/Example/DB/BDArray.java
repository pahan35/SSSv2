package Example.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**
*В queries першим завжди вказувати положення скіпа, якщо буде проводитися обробка по спуску або підйому.
*/
public class BDArray {
    Connection conn = null;
    public ArrayList<Query> queries = new ArrayList<Query>();
    public ArrayList<ResultSet> resultSets = new ArrayList<ResultSet>();
    public ArrayList<ResultSet> nameSet = new ArrayList<ResultSet>();

    public static void main(String[] args) {

    }


    private static void showBD(ArrayList<Object> BD) {

        for (int i = 0; i < BD.size(); i++) {
            System.out.println(BD.get(i));
        }

    }

    private ArrayList<String> formQuery(ArrayList<String> source, int sourceID, ArrayList<Integer> signalID, String startDate, String stopDate) {
        for (int i = 0; i < source.size(); i++) {
            this.queries.add(new Query(source.get(i), sourceID, signalID.get(i), startDate, stopDate));
        }

        ArrayList<String> arrayListQuery = new ArrayList<String>();
        for (Query query : queries) {
               arrayListQuery.add(query.getGetQuery())  ;
        }
        return arrayListQuery;
    }

    private void setConnection(Statement[] statement) throws Exception {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost/export_mybase";
        final String USER = "root";
        final String PASS = "1111";
        Class.forName(JDBC_DRIVER);
        //System.out.println("Connecting to database...");
        this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        //System.out.println("Creating statement...");

        for(int i = 0; i < statement.length; ++i) {
            statement[i] = this.conn.createStatement();
        }
    }

    private void getInfoFromQuery(ArrayList<ResultSet> resultSets, ArrayList<Double> scale) throws Exception {
        for(int i = 0; i < resultSets.size(); ++i) {
            while((resultSets.get(i)).next()) {
                for (int j = 0; j < queries.size(); j++) {
                    queries.get(i).valueList.add((resultSets.get(i).getDouble(this.queryString("Sample_Value_", j + 1))) * scale.get(i)) ;
                    queries.get(i).mSecList.add((resultSets.get(i).getInt(this.queryString("Sample_MSec_", j + 1))));
                    queries.get(i).dateTimeList.add((resultSets.get(i)).getString(this.queryString("Sample_TDate_", j + 1)).substring(0, 19));
                }
            }
        }
    }
    private void getNameFromQuery(ArrayList<ResultSet> nameSet) throws Exception{
        for (int i = 0; i < nameSet.size(); i++) {
            while((nameSet.get(i)).next())
                queries.get(i).targetName = nameSet.get(i).getString(1);
        }
    }

    private void formatDateTime() {
        for (Query query : queries) {
            query.formDateTimeList();
        }
            }

    private void cleanUp(Statement[] statement, ArrayList<ResultSet> resultSets) throws Exception {
        for(int i = 0; i < statement.length; ++i) {
            if (i < 4) {
            (resultSets.get(i)).close();
            }
            statement[i].close();
        }
    }
    private void executeQuery(Statement[] statement, ArrayList<String> queryList) throws Exception {
        for (int i = 0; i < statement.length; ++i) {
            resultSets.add(statement[i].executeQuery(queryList.get(i)));
        }
    }
    private void executeNameQuery(Statement[] statement) throws Exception {
        for (int i = 0; i < statement.length; ++i) {
            nameSet.add(statement[i].executeQuery(queries.get(i).targetNameQuery));
        }
    }
    private Statement[] createStatement(int numbersOfQuery){
        Statement[] statement = new Statement[numbersOfQuery];
        for(int se = 0; se < statement.length; ++se) {
            statement[se] = null;
        }
        return statement;
    }
    public BDArray(ArrayList<String> sourceList, int tableID, ArrayList<Integer> signalList, String startDate, String stopDate, ArrayList<Double> scale) {
        Statement[] statement = createStatement(sourceList.size());
        Statement[] nameStatement = createStatement(sourceList.size());

        try {
            setConnection(statement);
            ArrayList<String> queryList = this.formQuery(sourceList, tableID, signalList, startDate, stopDate);

            executeQuery(statement, queryList);
            getInfoFromQuery(resultSets, scale);
            formatDateTime();
            cleanUp(statement, resultSets);
            setConnection(nameStatement);
            executeNameQuery(nameStatement);
            getNameFromQuery(nameSet);


        }   catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement[0] != null) {
                    statement[0].close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public BDArray(QueryList queryList) {
        Statement[] statement = createStatement(queryList.source.size());
        Statement[] nameStatement = createStatement(queryList.source.size());

        try {
            setConnection(statement);
            ArrayList<String> queryListRun = this.formQuery(queryList.source, queryList.tableID, queryList.signallist, queryList.startDateTime, queryList.stopDateTime);

            executeQuery(statement, queryListRun);
            getInfoFromQuery(resultSets, queryList.scale);
            formatDateTime();
            cleanUp(statement, resultSets);
            setConnection(nameStatement);
            executeNameQuery(nameStatement);
            getNameFromQuery(nameSet);


        }   catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement[0] != null) {
                    statement[0].close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String queryString(String string, int number) { return string + number;    }

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

    private ArrayList<Long> difference(ArrayList<Long> arrayList) {
        ArrayList<Long> difference = new ArrayList<Long>();

        for(int i = 0; i < arrayList.size(); ++i) {
            if(i != 0) {
                difference.add(arrayList.get(i) - arrayList.get(0));
            } else {
                difference.add(0L);
            }
        }

        return difference;
    }

    public static void quantKanat(ArrayList<Long> timeList, ArrayList<Double> kanatValue) {
    }


    private void quantLists(ArrayList<Long> longKanat, ArrayList<Double> kanatValue, ArrayList<Long> longOtklon, ArrayList<Double> otklonValue, ArrayList<Long> longPosition, ArrayList<Double> positionValue, ArrayList<Long> longCurrent, ArrayList<Double> currentValue) {
        Long timeMinimum0 = (Long)longKanat.get(0);
        Long timeMinimum1 = (Long)longOtklon.get(0);
        Long timeMinimum2 = (Long)longPosition.get(0);
        Long timeMinimum3 = (Long)longCurrent.get(0);
        Long timeMinimum = Long.valueOf(this.minimum(timeMinimum0.longValue(), timeMinimum1.longValue(), timeMinimum2.longValue(), timeMinimum3.longValue()));
        Long timeMaximum0 = (Long)longKanat.get(longKanat.size() - 1);
        Long timeMaximum1 = (Long)longOtklon.get(longOtklon.size() - 1);
        Long timeMaximum2 = (Long)longPosition.get(longPosition.size() - 1);
        Long timeMaximum3 = (Long)longCurrent.get(longCurrent.size() - 1);
        Long timeMaximum = Long.valueOf(this.maximum(timeMaximum0.longValue(), timeMaximum1.longValue(), timeMaximum2.longValue(), timeMaximum3.longValue()));
    }

    public long minimum(long a, long b) {
        return a < b?a:b;
    }

    public long minimum(long a, long b, long c) {
        return a < b && a < c?a:(b < a && b < c?b:c);
    }

    public long minimum(long a, long b, long c, long d) {
        return a < b && a < c && a < d?a:(b < a && b < c && b < d?b:(c < a && c < b && c < d?c:d));
    }
    public long maximum(long a, long b, long c, long d) {
        return a > b && a > c && a > d ? a:(b > a && b > c && b > d?b:(c > a && c > b && c > d?c:d));
    }

}
