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
public class BDRawData {
    Connection conn = null;
    public ArrayList<Query> queries = new ArrayList<Query>();
    public ArrayList<ResultSet> resultSets = new ArrayList<ResultSet>();
    public ArrayList<ResultSet> nameSet = new ArrayList<ResultSet>();

    public static void main(String[] args) {

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

    private void setConnection(Statement[] statement) throws Exception { // Установка з'єднання з БД
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

    private void getInfoFromQuery(ArrayList<ResultSet> resultSets, ArrayList<Double> scale) throws Exception { // Витягування інформації з запитів
        for(int i = 0; i < resultSets.size(); ++i) {
            while((resultSets.get(i)).next()) {
                for (int j = 0; j < 36/*queries.size()*/; j++) { //TODO Потрібно якось визначати кількість колонок параметрів
                    queries.get(i).valueList.add((resultSets.get(i).getDouble("Sample_Value_" + (j + 1))) * scale.get(i)) ;
                    queries.get(i).mSecList.add((resultSets.get(i).getInt("Sample_MSec_" + (j + 1))));
                    queries.get(i).dateTimeList.add((resultSets.get(i)).getString("Sample_TDate_" + (j + 1)).substring(0, 19));
                }
            }
        }
    }

    private void formatDateTime() { // Отримання значення дати
        for (Query query : queries) {
            query.formDateTimeList();
        }
    }

    private void getNameFromQuery(ArrayList<ResultSet> nameSet) throws Exception{ // Отримання назви графіків
        for (int i = 0; i < nameSet.size(); i++) {
            while((nameSet.get(i)).next())
                queries.get(i).targetName = nameSet.get(i).getString(1);
        }
    }

    private void cleanUp(Statement[] statement, ArrayList<ResultSet> resultSets) throws Exception { // Закриття з'єднань
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

    public BDRawData(QueryList queryList) {
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
            cleanUp(nameStatement, nameSet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally { // Закриття з'єднань
            try {
                for (Statement s : statement) {
                    if(s != null) {
                        s.close();
                    }
                }

                if(this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static long stringToLong(String inputDate) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        formatter2.setTimeZone(timeZone);
        long l = 0L;

        try {
            Date e = formatter2.parse(inputDate);
            l = e.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return l;
    }
}
