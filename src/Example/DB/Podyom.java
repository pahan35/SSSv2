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
                    if ((time.get(i).get(j) >= currentTime) && (time.get(i).get(j) <= stopTime)){
                        if (currentTime == time.get(i).get(j)){
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(signal.get(i).get(j));
                            currentTime += step;
                        }
                        else if(currentTime + step > stopTime && currentTime != stopTime){
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(getYByX(currentTime, time.get(i).get(j), time.get(i).get(j + 1), signal.get(i).get(j), signal.get(i).get(j + 1)));
                            currentTime += step;
                        }
                        else {
                            quantTime.get(i).add(currentTime);
                            quantSignal.get(i).add(getYByX(currentTime,time.get(i).get(j - 1), time.get(i).get(j), signal.get(i).get(j - 1), signal.get(i).get(j)));
                            currentTime += step;
                        }
                    }

                }
                currentTime = startTime;
            }
        }

    }
    private double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }
}
