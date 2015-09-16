package Example.DB;

import java.util.ArrayList;

/**
 * Created by пк on 18.08.2015.
 */
public class Podyom {
    public long startTime;
    public long stopTime;
    public ArrayList<ArrayList<Double>> signal = new ArrayList<ArrayList<Double>>();
    public ArrayList<ArrayList<Long>> time = new ArrayList<ArrayList<Long>>();
    public ArrayList<ArrayList<Long>> quantTime = new ArrayList<ArrayList<Long>>();
    public ArrayList<ArrayList<Double>> quantSignal = new ArrayList<ArrayList<Double>>();
    public ArrayList<ArrayList<Double>> relativeSignal = new ArrayList<ArrayList<Double>>();

    public Podyom(){}

    public Podyom(long startTime, long stopTime){
        this.startTime = startTime;
        this.stopTime = stopTime;
    }
    public void quant(long step){
        long currentTime = startTime;
        ArrayList<Integer> currentSignalIndex = new ArrayList<Integer>();
        for (int i = 0; i < signal.size(); i++) {
            quantTime.add(new ArrayList<Long>());
            quantSignal.add(new ArrayList<Double>());
        }
        for (int i = 0; i < signal.size(); i++) {
            while (currentTime <= stopTime){
                for (int j = 0; i < signal.get(i).size(); i++) {
                    if ((time.get(i).get(j) >= currentTime) /*&& (time.get(i).get(j) <= stopTime)*/){
                        if (currentTime == time.get(i).get(j)){
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(signal.get(i).get(j));
                        }
                        /*else if(currentTime + step > stopTime && j + 1 < signal.get(i).size() *//*&& currentTime != stopTime*//*){
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(getYByX(currentTime, time.get(i).get(j), time.get(i).get(j + 1), signal.get(i).get(j), signal.get(i).get(j + 1)));
                        }*/
                        else {
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(getYByX(currentTime,time.get(i).get(j - 1), time.get(i).get(j), signal.get(i).get(j - 1), signal.get(i).get(j)));
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
        for (int i = 0; i < quantSignal.size(); i++) {
            minList.add(quantSignal.get(i).get(0));
            maxList.add(quantSignal.get(i).get(0));
            for (int j = 1; j < quantSignal.get(i).size(); j++) {
                if (minList.get(i) > quantSignal.get(i).get(j))
                    minList.set(i,quantSignal.get(i).get(j));
                if (maxList.get(i) < quantSignal.get(i).get(j))
                    maxList.set(i,quantSignal.get(i).get(j));
            }
        }
        // Записати відносне значення датчика до масиву
        for (int i = 0; i < quantSignal.size(); i++) {
            relativeSignal.add(new ArrayList<Double>());
            for (int j = 0; j < quantSignal.get(i).size(); j++) {
                relativeSignal.get(i).add((quantSignal.get(i).get(j) - minList.get(i)) / (maxList.get(i) - minList.get(i)));
            }
        }
    }
    private double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }
}
