package Example.DB;

import java.util.ArrayList;

/**
 * Created by пк on 24.11.2015.
 */
public class Container {
    int numbersOfSignals;
    ArrayList<ArrayList<ArrayList<Double>>> cutedSignals = new ArrayList<ArrayList<ArrayList<Double>>>();
    ArrayList<ArrayList<ArrayList<Double>>> relativeCutedSignals = new ArrayList<ArrayList<ArrayList<Double>>>();
    ArrayList<ArrayList<Double>> realizations = new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Integer>> binaryMatrix = new ArrayList<ArrayList<Integer>>();
    ArrayList<Double> averageOfRows = new ArrayList<Double>();
    ArrayList<Integer> etalon = new ArrayList<Integer>();

    public Container (ArrayList<Podyom> podyomList){
        getCutedSignals(podyomList);
        process();
    }
   public void getCutedSignals(ArrayList<Podyom> podyomList){
       // find minimum podyom size
       int minimumPodyomSize = findMinimumPodyomSize(podyomList);
       numbersOfSignals = podyomList.get(0).quantSignals.size();
       for (int i = 0; i < podyomList.size(); i++) {
           cutedSignals.add(new ArrayList<ArrayList<Double>>());
           for (int j = 0; j < minimumPodyomSize; j++) {
               for (int k = 0; k < numbersOfSignals; k++) {
                   if (j == 0)
                       cutedSignals.get(i).add(new ArrayList<Double>());
                   cutedSignals.get(i).get(k).add(podyomList.get(i).quantSignals.get(k).get(j));
               }
           }
       }
   }


    private int findMinimumPodyomSize(ArrayList<Podyom> arrayList){
        int size = arrayList.get(0).quantTimes.get(0).size();
        for (Podyom podyom : arrayList) {
            if (size > podyom.quantTimes.get(0).size())
                size = podyom.quantTimes.get(0).size();
        }
        return size;
    }
    private void findRelative(){
        // Знайти максимальне і мінімальне значення датчиків у масиві сигналів
        ArrayList<Double> minList = new ArrayList<Double>();
        ArrayList<Double> maxList = new ArrayList<Double>();
        for (int i = 0; i < numbersOfSignals; i++) {
            minList.add(cutedSignals.get(0).get(i).get(0));
            maxList.add(cutedSignals.get(0).get(i).get(0));
            for (int j = 0; j < cutedSignals.size(); j++) {
                for (int k = 0; k < cutedSignals.get(j).get(i).size(); k++) {
                    if (minList.get(i) > cutedSignals.get(j).get(i).get(k))
                        minList.set(i, cutedSignals.get(j).get(i).get(k));
                    if (maxList.get(i) < cutedSignals.get(j).get(i).get(k))
                        maxList.set(i, cutedSignals.get(j).get(i).get(k));
                }
            }
        }
        for (int i = 0; i < cutedSignals.size(); i++) {
            relativeCutedSignals.add(new ArrayList<ArrayList<Double>>());
            for (int j = 0; j < cutedSignals.get(i).size(); j++) {
                relativeCutedSignals.get(i).add(new ArrayList<Double>());
                for (int k = 0; k < cutedSignals.get(i).get(j).size(); k++) {
                    relativeCutedSignals.get(i).get(j).add(((cutedSignals.get(i).get(j).get(k) - minList.get(j)) / (maxList.get(j) - minList.get(j))) * 100);
                }
            }
        }
    }

    private void findAverage(){
        ArrayList<Double> sumOfRows = new ArrayList<Double>();
        for (int i = 0; i < realizations.get(0).size(); i++) {
            sumOfRows.add(realizations.get(0).get(i));
            for (int j = 1; j < realizations.size(); j++) {
                sumOfRows.add(sumOfRows.get(i) + realizations.get(j).get(i));
            }
        }
        for (int i = 0; i < sumOfRows.size(); i++) {
            averageOfRows.add(sumOfRows.get(i) / realizations.size());
        }
    }
    private void makeRealizations(){
        for (int currentRealization = 0; currentRealization < relativeCutedSignals.size(); currentRealization++) {
            realizations.add(new ArrayList<Double>());
            for (int currentSygnalIndex = 0; currentSygnalIndex < relativeCutedSignals.get(currentRealization).get(0).size(); currentSygnalIndex++) {
                for (int currentSygnal = 0; currentSygnal < relativeCutedSignals.get(currentRealization).size(); currentSygnal++) {
                    realizations.get(currentRealization).add(relativeCutedSignals.get(currentRealization).get(currentSygnal).get(currentSygnalIndex));
                }
            }
        }
    }
    private void makeBinaryMatrix(Double difference){
        for (int i = 0; i < realizations.size(); i++) {
            binaryMatrix.add(new ArrayList<Integer>());
            for (int j = 0; j < realizations.get(i).size(); j++) {
                if((realizations.get(i).get(j) < averageOfRows.get(j) - difference) || (realizations.get(i).get(j) > averageOfRows.get(j) + difference))
                    binaryMatrix.get(i).add(0);
                else binaryMatrix.get(i).add(1);
            }
        }
    }
    private void makeEtalon(){

        for (int i = 0; i < binaryMatrix.get(0).size(); i++) {
            int sum = 0;
            for (int j = 0; j < binaryMatrix.size(); j++) {
                sum += binaryMatrix.get(j).get(i);
            }
            if (sum <= binaryMatrix.size() / 2)
                etalon.add(0);
            else etalon.add(1);
        }
    }
    private void calculate01InEtalon(){
        int zeroes = 0;
        int ones = 0;
        for (int i = 0; i < etalon.size(); i++) {
            if(etalon.get(i) == 0)
                zeroes++;
            else if (etalon.get(i) == 1)
                ones++;
        }
        System.out.println("0 = "+zeroes);
        System.out.println("1 = " + ones);
    }

    public void process(){
        findRelative();
        makeRealizations();
        findAverage();
        makeBinaryMatrix(20D);
        makeEtalon();
        calculate01InEtalon();
    }
}
