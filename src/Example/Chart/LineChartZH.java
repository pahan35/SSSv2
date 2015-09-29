package Example.Chart;

import Example.DB.BDRawData;
import Example.DB.Query;
import Example.DB.QueryList;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
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
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by пк on 10.07.2015.
 */
public class LineChartZH extends ApplicationFrame {

    public static void main(String[] args) {
        QueryList queryList = new QueryList(52, "2015-01-16 00:00:14", "2015-01-16 00:50:14"); //TODO Якось зробити перевірку на валідність введеної дати
        queryList.addQuery("akhz1_data", 1, 0.002D);
        queryList.addQuery("akhz1_data", 2, 0.002);
        queryList.addQuery("amper", 1, 0.0002);
        queryList.addQuery("event_data", 4, 1D);
        queryList.addQuery("event_data", 5, 1D);
        queryList.addQuery("event_data", 6, 1D);
        queryList.addQuery("event_data", 7, 1D);
        BDRawData bdRawData = new BDRawData(queryList);
        LineChartZH lineChartZH = new LineChartZH("Работа системы с " + bdRawData.queries.get(0).source + "_" + bdRawData.queries.get(0).sourceID, bdRawData.queries);
    }

    public LineChartZH(String chartName, ArrayList<Query> queries) { // конструктор групи графіків
        super(chartName);
        String s;
        if(true){}
        JPanel jPanel = createDemoPanel(queries, chartName);
        this.setContentPane(jPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    private void build(String chartName, ArrayList<Query> queries){

    }

    public static JPanel createDemoPanel(ArrayList<Query> queries, String chartName) {// створення панелі графіків
        JFreeChart jFreeChart = createChart(createDataset(queries), chartName);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setMouseWheelEnabled(false);
        return chartPanel;
    }

    private static XYDataset createDataset(ArrayList<Query> queries){ // створення графіків по даним з БД
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

    private static JFreeChart createChart(XYDataset xyDataset, String chartName) {
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(chartName, "Время", "Значение", xyDataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot)jFreeChart.getPlot();
        xyPlot.setDomainPannable(true);
        xyPlot.setRangePannable(true);
        XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
        xyLineAndShapeRenderer.setBaseShapesVisible(false);
        xyLineAndShapeRenderer.setBaseShapesFilled(true);
        NumberAxis numberAxis = (NumberAxis)xyPlot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return jFreeChart;
    }
}
