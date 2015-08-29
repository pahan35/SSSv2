package Example.DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by пк on 04.07.2015.
 */
public class CalculateZH {
    public static void main(String [] args){

        /*ArrayList<Double> listX = new ArrayList<Double>();
        ArrayList<Double> listY = new ArrayList<Double>();
        listX.add(1.0D);
        listX.add(4.0D);
        listX.add(9.5D);
        listX.add(10.0D);
        listX.add(13.0D);
        listY.add(10D);
        listY.add(40D);
        listY.add(95D);
        listY.add(100D);
        listY.add(130D);
        ArrayList<Double> resultList = quantList(listX,listY,1D);
        showList(resultList);*/
        double[] normD1 = createDList(50,500D);
        double[] normD2 = createDList(50,150D);
        double otklonD1 = 175;
        double otklonD2 = 50;
        double[] somethingWrongD1 = transformDVector(normD1, 1.5D);
        double[] crashD1 = transformDVector(normD1, 2D);
        int[] resIVectorNormD1 = fill01ByInputDVector(normD1, otklonD1);
        int[] resIVectorSomethingWrongD1 = fill01ByInputDVector(somethingWrongD1, otklonD1);
        int[] resIVectorCrashD1 = fill01ByInputDVector(crashD1, otklonD1);
        double[] somethingWrongD2 = transformDVector(normD2, 1.5D);
        double[] crashD2 = transformDVector(normD2, 2D);
        int[] resIVectorNormD2 = fill01ByInputDVector(normD2, otklonD2);
        int[] resIVectorSomethingWrongD2 = fill01ByInputDVector(somethingWrongD2, otklonD2);
        int[] resIVectorCrashD2 = fill01ByInputDVector(crashD2, otklonD2);
        /*showList(resIVectorCrash);
        showList(resIVectorNorm);
        showList(resIVectorSomethingWrong);*/
        /*int experiment1 = compareList(resIVectorCrash, resIVectorNorm);
        int experiment2 = compareList(resIVectorCrash, resIVectorSomethingWrong); */
/*        show3List(resIVectorNormD1,resIVectorSomethingWrongD1,resIVectorCrashD1);
        System.out.println(averageOfVector(resIVectorNormD1));
        System.out.println(averageOfVector(resIVectorSomethingWrongD1));
        System.out.println(averageOfVector(resIVectorCrashD1));*/
        int[] clusterNorm = getCluster(normD1, otklonD1, normD2, otklonD2);
        int[] clusterSomethingWrong = getCluster(somethingWrongD1, otklonD1, somethingWrongD2, otklonD2);
        int[] clusterCrash = getCluster(crashD1, otklonD1, crashD2, otklonD2);
        showCluster(clusterNorm);
        showCluster(clusterSomethingWrong);
        showCluster(clusterCrash);


    }
    public static double[] createDList(int length, double maxD){ // генерирует рандомный дабл-список длины переменной  диапазоне от 0 до maxD
        Random random = new Random();
        double[] dList = new double[length];
        for (int i = 0; i < length; i++) {
            dList[i] = random.nextDouble() * maxD;
        }
        return dList;
    }
    public static double[] transformDVector(double[] inputDVector, double multiplier){
        double[] resultDVector = new double[inputDVector.length];
        for (int i = 0; i < inputDVector.length; i++) {
            resultDVector[i] = inputDVector[i] * multiplier;
        }
        return resultDVector;
    }
    public static int[] fill01ByInputDVector (double[] inputVector, double otklon){
        double averageD = averageOfVector(inputVector);
        int[] resultIVector = new int[inputVector.length];
        for (int i = 0; i < inputVector.length; i++) {
            if ((inputVector[i] > averageD + otklon) || (inputVector[i] < averageD - otklon)){
                resultIVector[i] = 1;
            }
            else {
                resultIVector[i] = 0;
            }
        }
        return resultIVector;
    }
    public static double sumVector (double[] vector){ // подсчитывает сумму списка
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum;
    }
    public static double sumVector (int[] vector){ // подсчитывает сумму списка
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum;
    }
    public static double averageOfVector(double[] vector){ // Подсчитывает среднее значение списка
        double average = sumVector(vector)/vector.length;
        return average;
    }
    public static double averageOfVector(int[] vector){ // Подсчитывает среднее значение списка
        double average = sumVector(vector)/vector.length;
        return average;
    }

    public static void showList(double[] list){ // Выводит список
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }

    public static void show3List(int[] array1, int[] array2, int[] array3){
        for (int i = 0; i < array1.length; i++) {
            System.out.print(array1[i] + " ");
            System.out.print(array2[i] + " ");
            System.out.println(array3[i]);
        }
    }
    public static void showList(ArrayList list){ // выводит еррейлист
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
    public static void showList(int[] array){ // выводит еррейлист
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
    public static void showCluster(int[] cluster){
        System.out.print(cluster[0] + " ");
        System.out.println(cluster[1]);
    }
        /*public static int compareList(int[] array1, int[] array2){ // сравнение совпадания списков
        int result = 0;
        *//*for (int i = 0; i < array1.length; i++) {
            if (array1[i] == array2[i]) {
                System.out.println("Списки не совпадают");
                result = 1;
            }
        }*//*
        if (array1.equals(array2)){
            System.out.println("Списки совпадают");
        }
        return result;
    }*/
    public static int[] getCluster(double[] arrayD1, double otklonD1, double[] arrayD2, double otklonD2){
        int[] cluster = new int[2];
        int[] array01D1 = fill01ByInputDVector(arrayD1,otklonD1);
        int[] array01D2 = fill01ByInputDVector(arrayD2,otklonD2);
        if (averageOfVector(array01D1) < 0.5D){
            cluster[0] = 0;
        }
        else {
            cluster[0] = 1;
        }
        if (averageOfVector(array01D2) < 0.5D){
            cluster[1] = 0;
        }
        else {
            cluster[1] = 1;
        }
        return cluster;
    }
    public static double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }
    public static ArrayList<Double> quantList (ArrayList<Double> inputXList, ArrayList<Double> inputYList, double step){ //квантирует список с заданным шагом
        ArrayList<Double> quantXList = new ArrayList<Double>();
        ArrayList<Double> quantYList = new ArrayList<Double>();
        double quantX0 = inputXList.get(0);
        int i = 0;
        double quantX = quantX0;
        double previousY = inputYList.get(0), currentY, previousX = quantX0;
        for (Double timeInList : inputXList){

            while ((quantX <= timeInList)){

                System.out.println("X = " + quantX);
                quantXList.add(quantX);
                if (i == 0){
                    currentY = inputYList.get(0);
                    quantYList.add(currentY);
                }
                else {
                currentY = getYByX(quantX,previousX,inputXList.get(i),previousY,inputYList.get(i));
                quantYList.add(currentY);}
                previousX = quantX;
                previousY = currentY;
                quantX += step;
                }
            if (i < inputXList.size() - 1)
            i++;
            System.out.println("i = " + i);
            }
        return quantYList;
        }

    }

