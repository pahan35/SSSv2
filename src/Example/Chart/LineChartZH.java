package Example.Chart;

import Example.DB.BDArray;
import Example.DB.Query;
import Example.DB.QueryList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import javax.xml.namespace.QName;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by пк on 10.07.2015.
 */
public class LineChartZH extends ApplicationFrame {
    public static void main(String[] var0) {
/*        ArrayList<String> sourcelist = new ArrayList<String>();
        int tableID = 52;
        ArrayList<Integer> signallist = new ArrayList<Integer>();
        String startDateTime = "2015-01-16 00:10:14";
        String stopDateTime = "2015-01-16 02:30:14";
        ArrayList<Double> scale = new ArrayList<Double>();
        sourcelist.add("akhz1_data");
        sourcelist.add("akhz1_data");
        sourcelist.add("event_data");
        sourcelist.add("event_data");
        sourcelist.add("event_data");
        sourcelist.add("event_data");
        signallist.add(1);
        signallist.add(2);
        signallist.add(4);
        signallist.add(5);
        signallist.add(6);
        signallist.add(7);
        scale.add(0.002);
        scale.add(0.002);
        scale.add(1D);
        scale.add(1D);
        scale.add(1D);
        scale.add(1D);*/
        QueryList queryList = new QueryList(50, "2015-01-14 00:00:14", "2015-01-14 00:50:14");
        queryList.addQuery("akhz1_data", 1, 1D);
        //queryList.addQuery("akhz1_data", 2, 1D);
        queryList.addQuery("amper", 1, 1D);
/*        queryList.addQuery("event_data", 4, 1D);
        queryList.addQuery("event_data", 5, 1D);
        queryList.addQuery("event_data", 6, 1D);
        queryList.addQuery("event_data", 7, 1D);*/
        BDArray dad = new BDArray(queryList);


        LineChartZH lineChartZH = new LineChartZH("Работа системы с " + dad.queries.get(0).source + "_" + dad.queries.get(0).sourceID, dad.queries);
        lineChartZH.pack();
        RefineryUtilities.centerFrameOnScreen(lineChartZH);
        lineChartZH.setVisible(true);
    }
    public static JPanel createDemoPanel() {
        JFreeChart jFreeChart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    public static JPanel createDemoPanel(ArrayList<Double> arrayListTime, ArrayList<Double> arrayListValueD1) {
        JFreeChart jFreeChart = createChart(createDataset(arrayListTime, arrayListValueD1));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
/*        for (double d: arrayListValueD4){
            System.out.println(d);
        }*/
        chartPanel.setMouseWheelEnabled(false);
        return chartPanel;
    }
    public static JPanel createDemoPanel(ArrayList<Double> arrayListTime, ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3, ArrayList<Double> arrayListValueD4) {
        JFreeChart jFreeChart = createChart(createDataset(arrayListTime, arrayListValueD1, arrayListValueD2, arrayListValueD3, arrayListValueD4));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
/*        for (double d: arrayListValueD4){
            System.out.println(d);
        }*/
        chartPanel.setMouseWheelEnabled(false);
        return chartPanel;
    }
    public static JPanel createDemoPanel(ArrayList<Double> arrayListTime, ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3) {
        JFreeChart jFreeChart = createChart(createDataset(arrayListTime, arrayListValueD1, arrayListValueD2, arrayListValueD3));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
/*        for (double d: arrayListValueD4){
            System.out.println(d);
        }*/
        chartPanel.setMouseWheelEnabled(false);
        return chartPanel;
    }
    public static JPanel createDemoPanel(ArrayList<Query> queries) {
        JFreeChart jFreeChart = createChart(createDataset(queries));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
/*        for (double d: arrayListValueD4){
            System.out.println(d);
        }*/
        chartPanel.setMouseWheelEnabled(false);
        return chartPanel;
    }
    private static JFreeChart createChart(XYDataset var0) {
        JFreeChart var1 = ChartFactory.createXYLineChart("Работа системы", "Время", "Значение", var0, PlotOrientation.VERTICAL, true, true, false);
        XYPlot var2 = (XYPlot)var1.getPlot();
        var2.setDomainPannable(true);
        var2.setRangePannable(true);
        XYLineAndShapeRenderer var3 = (XYLineAndShapeRenderer)var2.getRenderer();
        var3.setBaseShapesVisible(false);
        var3.setBaseShapesFilled(true);
        NumberAxis var4 = (NumberAxis)var2.getRangeAxis();
        var4.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return var1;
    }
    public LineChartZH(String chartName) {
        super(chartName);
        JPanel var2 = createDemoPanel();
        var2.setPreferredSize(new Dimension(800, 600));
        this.setContentPane(var2);
    }
    public LineChartZH(String chartName, ArrayList<Double> arrayListTime,ArrayList<Double> arrayListValueD1) {
        super(chartName);
        JPanel jPanel = createDemoPanel(arrayListTime, arrayListValueD1);
        for (double d : arrayListValueD1) {
            System.out.println(d);
        }
        jPanel.setPreferredSize(new Dimension(800, 600));
        this.setContentPane(jPanel);
    }
    public LineChartZH(String chartName, ArrayList<Double> arrayListTime,ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3) {
        super(chartName);
        JPanel jPanel = createDemoPanel(arrayListTime, arrayListValueD1, arrayListValueD2, arrayListValueD3);
        for (double d: arrayListValueD3){
            System.out.println(d);
        }
        jPanel.setPreferredSize(new Dimension(800, 600));
        this.setContentPane(jPanel);
    }
    public LineChartZH(String chartName, ArrayList<Query> queries) {
        super(chartName);
        JPanel jPanel = createDemoPanel(queries);
        this.setContentPane(jPanel);
    }

    public LineChartZH(String chartName, ArrayList<Double> arrayListTime,ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3, ArrayList<Double> arrayListValueD4) {
        super(chartName);
        JPanel jPanel = createDemoPanel(arrayListTime, arrayListValueD1, arrayListValueD2, arrayListValueD3, arrayListValueD4);
/*        for (double d: arrayListValueD4){
            System.out.println(d);
        }*/
        jPanel.setPreferredSize(new Dimension(800, 600));
        this.setContentPane(jPanel);
    }

    private static XYDataset createDataset() {
        XYSeries line1 = new XYSeries("Положение западного скипа");
        line1.add(1.0D, 1.0D);
        line1.add(2.0D, 4.0D);
        line1.add(3.0D, 3.0D);
        line1.add(4.0D, 5.0D);
        line1.add(5.0D, 5.0D);
        line1.add(6.0D, 7.0D);
        line1.add(7.0D, 7.0D);
        line1.add(8.0D, 8.0D);
        XYSeries line2 = new XYSeries("Ток якоря");
        line2.add(1.0D, 5.0D);
        line2.add(2.0D, 7.0D);
        line2.add(3.0D, 6.0D);
        line2.add(4.0D, 8.0D);
        line2.add(5.0D, 4.0D);
        line2.add(6.0D, 4.0D);
        line2.add(7.0D, 2.0D);
        line2.add(8.0D, 1.0D);
        XYSeries line3 = new XYSeries("Путь проскальзывания");
        line3.add(3.0D, 4.0D);
        line3.add(4.0D, 3.0D);
        line3.add(5.0D, 2.0D);
        line3.add(6.0D, 3.0D);
        line3.add(7.0D, 6.0D);
        line3.add(8.0D, 3.0D);
        line3.add(9.0D, 4.0D);
        line3.add(10.0D, 3.0D);
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(line1);
        xySeriesCollection.addSeries(line2);
        xySeriesCollection.addSeries(line3);
        return xySeriesCollection;
    }
    private static XYDataset createDataset(ArrayList<Double> arrayListTime,ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3, ArrayList<Double> arrayListValueD4) {
        int minimumMaxIndex = minimum(arrayListTime.size(),arrayListValueD1.size(),arrayListValueD2.size(),arrayListValueD4.size());

        XYSeries line1 = new XYSeries("Скорость скипа на канатоведущем шкиве");
        XYSeries line2 = new XYSeries("Скорость скипа на отклоняющем шкиве");
        XYSeries line3 = new XYSeries("Положение западного скипа");
        XYSeries line4 = new XYSeries("Ток якоря двигателя");
        for (int i = 0; i < minimumMaxIndex; i++) {
            line1.add(arrayListTime.get(i), arrayListValueD1.get(i));
            //System.out.println(arrayListValueD3.get(i));
        }
        for (int i = 0; i < minimumMaxIndex; i++) {
            line2.add(arrayListTime.get(i), arrayListValueD2.get(i));
        }
        for (int i = 0; i < minimumMaxIndex; i++) {
            line3.add(arrayListTime.get(i), arrayListValueD3.get(i));
        }
        for (int i = 0; i < minimumMaxIndex; i++) {
            line4.add(arrayListTime.get(i), arrayListValueD4.get(i));
        }
//        for (double d: arrayListValueD4){
//            System.out.println(d);
//        }
//        for (int i = 0; i < arrayListValueD4.size(); i++) {
//            System.out.println(line4.toString());
//        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(line1);
        xySeriesCollection.addSeries(line2);
        xySeriesCollection.addSeries(line3);
        xySeriesCollection.addSeries(line4);
        return xySeriesCollection;
    }

    private static XYDataset createDataset(ArrayList<Double> arrayListTime,ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListValueD2, ArrayList<Double> arrayListValueD3) {

        XYSeries line1 = new XYSeries("Скорость скипа на канатоведущем шкиве");
        XYSeries line2 = new XYSeries("Скорость скипа на отклоняющем шкиве");
        XYSeries line3 = new XYSeries("Положение западного скипа");
        for (int i = 0; i < arrayListTime.size(); i++) {
            line1.add(arrayListTime.get(i), arrayListValueD1.get(i));
            System.out.println(arrayListValueD3.get(i));
        }
        for (int i = 0; i < arrayListTime.size(); i++) {
            line2.add(arrayListTime.get(i), arrayListValueD2.get(i));
        }
        for (int i = 0; i < arrayListTime.size(); i++) {
            line3.add(arrayListTime.get(i), arrayListValueD3.get(i));
        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(line1);
        xySeriesCollection.addSeries(line2);
        xySeriesCollection.addSeries(line3);
        return xySeriesCollection;
    }
    private static XYDataset createDataset(ArrayList<Query> queries){
        ArrayList<XYSeries> xySerieses = new ArrayList<XYSeries>();
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        for (int i = 0; i < queries.size(); i++) {
            xySerieses.add(new XYSeries(queries.get(i).targetName));
            for (int j = 0; j < queries.get(i).valueList.size() ; j++) {
                xySerieses.get(i).add(queries.get(i).longTimeList.get(j),queries.get(i).valueList.get(j));
            }
            xySeriesCollection.addSeries(xySerieses.get(i));
        }
        return xySeriesCollection;
    }
    private static XYDataset createDataset(ArrayList<Double> arrayListValueD1, ArrayList<Double> arrayListTime) {
        XYSeries line1 = new XYSeries("Скорость скипа на канатоведущем шкиве");
        for (int i = 0; i < arrayListTime.size(); i++) {
            line1.add(arrayListValueD1.get(i), arrayListTime.get(i));
        }
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(line1);
        return xySeriesCollection;
    }
    public static int minimum(int a, int b, int c, int d){
        if (a < b && a < c && a < d)
            return a;
        else if(b > a && b < c && b < d)
            return b;
        else if(c < a && c < b && c < d)
            return c;
        else
            return d;
    }
}
