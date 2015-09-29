package Example.Chart;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;

import Example.DB.Podyom;
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

/**
 * Created by пк on 03.07.2015.
 */
public class LineChartTest extends ApplicationFrame
{

    public static void main(String[] args){
        /*XYDataset line1 = createDataset();
        createChart(line1);
        createDemoPanel();*/
        //ArrayList<Podyom> podyoms = new ArrayList<Podyom>();
        //LineChartTest lineChartTest = new LineChartTest("",podyoms);
        for (int i = 0; i < 50; i++) {
            System.out.print("check.signals.get(0).add(");
            System.out.printf("%2d", Math.random() * i);
            System.out.println(");");
        }
    }

    public LineChartTest(String title, ArrayList<Podyom> podyoms) {
        super(title);
        JPanel jPanel = createDemoPanel(podyoms, title);
        jPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
    public static JPanel createDemoPanel(ArrayList<Podyom> podyoms, String title) { // Создание панельки
        JFreeChart jFreeChart = createChart(createDataset(podyoms), title);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    private static JFreeChart createChart(XYDataset xyDataset, String title) { // Создание графика
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(title, "X", "Y", xyDataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();
        xyPlot.setDomainPannable(true);
        xyPlot.setRangePannable(true);
        XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
        xyLineAndShapeRenderer.setBaseShapesVisible(true);
        xyLineAndShapeRenderer.setBaseShapesFilled(true);
        NumberAxis numberAxis = (NumberAxis) xyPlot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return jFreeChart;
    }
    private static XYDataset createDataset(ArrayList<Podyom> podyoms) { // Загрузка данных
        ArrayList<XYSeries> xySerieses = new ArrayList<XYSeries>();
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        for (int i = 0; i < podyoms.size(); i++) {

            for (int j = 0; j < podyoms.get(i).signals.size() ; j++) {
                if (i == 0) {
                    xySerieses.add(new XYSeries(j));
                    xySerieses.add(new XYSeries("Quant "+ j));
                    xySerieses.add(new XYSeries("Relative "+ j));
                    xySerieses.add(new XYSeries("Binary "+ j));
                }
                for (int k = 0; k < podyoms.get(i).signals.get(j).size(); k++) {
                    xySerieses.get(j * xySerieses.size()).add(podyoms.get(i).times.get(j).get(k), podyoms.get(i).signals.get(j).get(k));
                }
                for (int k = 0; k < podyoms.get(i).quantSignals.get(j).size(); k++) {
                    xySerieses.get(j * xySerieses.size() + 1).add(podyoms.get(i).quantTimes.get(j).get(k), podyoms.get(i).quantSignals.get(j).get(k));
                }
                for (int k = 0; k < podyoms.get(i).quantSignals.get(j).size(); k++) {
                    xySerieses.get(j * xySerieses.size() + 2).add(podyoms.get(i).quantTimes.get(j).get(k), podyoms.get(i).relativeSignals.get(j).get(k));
                }
                for (int k = 0; k < podyoms.get(i).quantSignals.get(j).size(); k++) {
                    xySerieses.get(j * xySerieses.size() + 3).add(podyoms.get(i).quantTimes.get(j).get(k), podyoms.get(i).binaryMatrixs.get(j).get(k));
                }
            }
            if (i == podyoms.size() - 1) {
                for (int j = 0; j < podyoms.get(0).signals.size() * xySerieses.size(); j++) {
                    xySeriesCollection.addSeries(xySerieses.get(j));
                }

            }
        }
        return xySeriesCollection;
    }

}
