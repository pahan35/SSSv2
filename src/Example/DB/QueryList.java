package Example.DB;

import java.util.ArrayList;

/**
 * Created by пк on 14.08.2015.
 */
public class QueryList {
    public ArrayList<String> source = new ArrayList<String>();
    public ArrayList<Integer> signallist = new ArrayList<Integer>();
    public String startDateTime;
    public String stopDateTime;
    public int tableID;
    public ArrayList<Double> scale = new ArrayList<Double>();

    public QueryList(int tableID, String startDateTime, String stopDateTime){
        this.tableID = tableID;
        this.startDateTime = startDateTime;
        this.stopDateTime = stopDateTime;
    }

    public void addQuery(String source, Integer signal, Double scale){
        this.source.add(source);
        this.signallist.add(signal);
        this.scale.add(scale);
    }
}
