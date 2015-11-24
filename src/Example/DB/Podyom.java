package Example.DB;

import Example.Chart.LineChartTest;

import java.util.ArrayList;

/**
 * Created by пк on 18.08.2015.
 */
public class Podyom {
    public long startTime;
    public long stopTime;
    public ArrayList<ArrayList<Double>> signals = new ArrayList<ArrayList<Double>>();
    public ArrayList<ArrayList<Long>> times = new ArrayList<ArrayList<Long>>();
    public ArrayList<ArrayList<Long>> quantTimes = new ArrayList<ArrayList<Long>>();
    public ArrayList<ArrayList<Double>> quantSignals = new ArrayList<ArrayList<Double>>();
    public ArrayList<ArrayList<Double>> relativeSignals = new ArrayList<ArrayList<Double>>();
    public ArrayList<ArrayList<Integer>> binaryMatrixs = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Double> averages = new ArrayList<Double>();

    public static void main(String[] args) {
        Podyom check = new Podyom(0L, 450L);
        check.signals.add(new ArrayList<Double>());
        check.times.add(new ArrayList<Long>());
        for (long i = 0; i < 50; i++) {
            check.signals.get(0).add(50 + Math.random() * i);
            check.times.get(0).add(i * 23);
        }
        check.quant(7L);
        check.getRelative();
        check.getAverage();
        check.getBinaryMatrix(20D);
        ArrayList<Podyom> podyomList = new ArrayList<Podyom>();
        podyomList.add(check);
        LineChartTest lineTest1 = new LineChartTest("Test", podyomList);
    }

    public Podyom(){}

    public Podyom(long startTime, long stopTime){
        this.startTime = startTime;
        this.stopTime = stopTime;
    }
    public void quant(long step){ // TODO Потрібно перевірити
        long currentTime = startTime;
        ArrayList<Integer> currentSignalIndex = new ArrayList<Integer>();
        for (int i = 0; i < signals.size(); i++) {
            quantTimes.add(new ArrayList<Long>());
            quantSignals.add(new ArrayList<Double>());
        }
        for (int i = 0; i < signals.size(); i++) {
            for (int j = 0; j < signals.get(i).size() - 1; j++) {
                while (currentTime <= times.get(i).get(j + 1)){

                    if ((times.get(i).get(j + 1) >= currentTime) /*&& (times.get(i).get(j) <= stopTime)*/){
                        if (currentTime == times.get(i).get(j)){
                            quantTimes.get(i).add(currentTime);
                            quantSignals.get(i).add(signals.get(i).get(j));
                        }
                        else if(currentTime + step > stopTime && j + 1 >= signals.get(i).size() /*&& currentTime != stopTime*/){
                                quantTimes.get(i).add(currentTime);
                                quantSignals.get(i).add(CalculateZH.getYByX(currentTime, times.get(i).get(j - 1), times.get(i).get(j), signals.get(i).get(j - 1), signals.get(i).get(j)));
                                quantTimes.get(i).add(stopTime);
                                quantSignals.get(i).add(signals.get(i).get(j));
                            }
                        else {
                            quantTimes.get(i).add(currentTime);
                            quantSignals.get(i).add(CalculateZH.getYByX(currentTime, times.get(i).get(j), times.get(i).get(j + 1), signals.get(i).get(j), signals.get(i).get(j + 1)));
                            }
                        currentTime += step;
                    }
                }
                    //currentTime = startTime;
            }
            currentTime = startTime;
        }
    }
    public void getRelative(){
        // Знайти максимальне і мінімальне значення датчиків у масиві сигналів
        ArrayList<Double> minList = new ArrayList<Double>();
        ArrayList<Double> maxList = new ArrayList<Double>();
        for (int i = 0; i < quantSignals.size(); i++) {
            minList.add(quantSignals.get(i).get(0));
            maxList.add(quantSignals.get(i).get(0));
            for (int j = 1; j < quantSignals.get(i).size(); j++) {
                if (minList.get(i) > quantSignals.get(i).get(j))
                    minList.set(i, quantSignals.get(i).get(j));
                if (maxList.get(i) < quantSignals.get(i).get(j))
                    maxList.set(i, quantSignals.get(i).get(j));
            }
        }
        // Записати відносне значення датчика до масиву
        for (int i = 0; i < quantSignals.size(); i++) {
            relativeSignals.add(new ArrayList<Double>());
            for (int j = 0; j < quantSignals.get(i).size(); j++) {
                relativeSignals.get(i).add(((quantSignals.get(i).get(j) - minList.get(i)) / (maxList.get(i) - minList.get(i))) * 100);
            }
        }
    }

    public void getAverage(){
        for (ArrayList<Double> dList : relativeSignals) {
            double sum = 0;
            int size = dList.size();
            for (double d : dList) {
                sum += d;
            }
            averages.add(sum/size);
        }
    }

    public void getBinaryMatrix(double porog){ // Отримуємо бінарні матриці з відносних сигналів
        for (int i = 0; i < relativeSignals.size(); i++) {
            this.binaryMatrixs.add(new ArrayList<Integer>());
            for (int j = 0; j < relativeSignals.get(i).size(); j++) {
                if (relativeSignals.get(i).get(j) < averages.get(i) - porog || relativeSignals.get(i).get(j) > averages.get(i) + porog)
                    binaryMatrixs.get(i).add(0);
                else
                    binaryMatrixs.get(i).add(1);
            }
        }
    }
    public Podyom modify(double[][] percentOfMistake){
        Podyom podyom = null;
        try {
            podyom = (Podyom) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // // TODO: 07.11.2015 implement modify fumction
        return podyom;
    }


}
