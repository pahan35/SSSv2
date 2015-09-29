package Example.DB;

import Example.Chart.LineChartZH;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
/** Created by пк on 14.08.2015.

/**
 * 1-ий крок: виділити підйоми з датчиків позиціювання
 * 2-ий крок: записати дані з інших датчиків до масивів, відповідно до часу підйому.
 */
/*
TODO определить время подъёма +
2 записать показания датчиков во время подъема +
3 дискретизировать показания +
4 перевести в стобальную шкалу +
** первая матрица - один из узлов работает плохо
** вторая - следующий из узлов работает плохо
** будет 40 подъёмов, около 70% которых принадлежат только своей матрице
** производится обработка классов:
* 1) считаются средние значения по каждому из столбцов
* 2) получить средние значения каждого столбца
* 3) задать контрольний допуск (первый раз на глаз (-+20)
* 4) сравнивая каждое значение с верхней и нижней границей мы получаем 0 или 1(попадает в интервал)
* 5) по бинарной матрице считаются средние значения по столбцам: если значение > 0,5 - 0, больше - 1. Так мы получим эталонный вектор
* 6) сравнивая каждую реализацию бинарной матрицы с эталонным вектором, ищем кодовое расстояние(отличие строк бинарной матрицы от эталонного вектора в еденицах).
* 7) То же самое делаем из соседним классом и сравниваем
* 8) Считаем сколько каких реализаций вместилось в каждый радиус
* 9) Посчитать КФЕ(по сфотканой формуле)
* 10) Посчитать (2.7.4)
* 11) Построить график 2.7.4 от радиуса
 */
public class EIT {
    ArrayList<ArrayList<Double>> signal = new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Long>> time = new ArrayList<ArrayList<Long>>();
    ArrayList<Long> timeUp = new ArrayList<Long>();
    ArrayList<Podyom> podyomList = new ArrayList<Podyom>();
    ArrayList<Integer> sumOfRows = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> realisations = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> etalon = new ArrayList<Integer>();
    ArrayList<Integer> distances = new ArrayList<Integer>();
    double minimumDownValue;
    double maximumHighValue;
    long dopusk;
    public static void main(String[] args) {
        QueryList queryList = new QueryList(52, "2015-01-16 00:10:14", "2015-01-16 01:30:14");
        queryList.addQuery("akhz1_data", 1, 1D); // позиция скипа
        queryList.addQuery("akhz1_data", 2, 1D);
        queryList.addQuery("amper", 1, 1D);
        queryList.addQuery("amper", 2, 1D);
        queryList.addQuery("event_data", 6, 1D);
        queryList.addQuery("event_data", 7, 1D);
        BDRawData bdRawData = new BDRawData(queryList);
        //LineChartZH lineChartZH = new LineChartZH("Работа системы с " + bdRawData.queries.get(0).source + "_" + bdRawData.queries.get(0).sourceID, bdRawData.queries);

        EIT norm = new EIT(-522D, 49D);
        norm.getPodyom(bdRawData);
        norm.getPodyomSignals(bdRawData);
        for(Podyom podyom : norm.podyomList){
            podyom.quant(100L);
            podyom.getRelative();
            podyom.getAverage();
            podyom.getBinaryMatrix(20D);
        }
        norm.makeRealisations();
        norm.getEtalon();
        norm.getDistance();
        System.out.println("I`m OK)");


    }
    public EIT(double minimumDownValue, double maximumHighValue){
        this.minimumDownValue = minimumDownValue;
        this.maximumHighValue = maximumHighValue;
    }
    public EIT(EIT eit, ArrayList<Double> wrongProcent){//TODO зробити "запис" "поганого" класу

    }
    public void getPodyom(BDRawData bdRawData){
        for (int i = 1; i < bdRawData.queries.get(0).valueList.size(); i++) { // Прохід по значенню положення скіпа
            if (bdRawData.queries.get(0).valueList.get(i) - bdRawData.queries.get(0).valueList.get(i - 1) > 0 && bdRawData.queries.get(0).valueList.get(i) > minimumDownValue) { // Умова підйому
                this.podyomList.add(new Podyom());
                podyomList.get(podyomList.size() - 1).startTime = bdRawData.queries.get(0).longTimeList.get(i);
                for (int j = i + 1; j < bdRawData.queries.get(0).valueList.size(); j++) {
                    if (bdRawData.queries.get(0).valueList.get(j) - bdRawData.queries.get(0).valueList.get(j - 1) < 0 && bdRawData.queries.get(0).valueList.get(j) < maximumHighValue) {// Умова закінчення підйому чи досягення максимального верхнього значення
                        System.out.println(bdRawData.queries.get(0).valueList.get(j - 1) + " " + bdRawData.queries.get(0).valueList.get(j));
                        podyomList.get(podyomList.size() - 1).stopTime = bdRawData.queries.get(0).longTimeList.get(j);
                        i = j;
                        break;
                    }
                }
            }
        }
    }
    public void getPodyomSignals(BDRawData bdArray){// Запис сигналів до отриманих підйомів
        int podyomID = 0;
        for (int i = 1; i < bdArray.queries.size(); i++) { // Прохід по масивам сигналаів, отриманих з БД

            for (Podyom podyom : podyomList){ // Створення масивів даних та часу у підйомах
                podyom.signals.add(new ArrayList<Double>());
                podyom.times.add(new ArrayList<Long>());
            }

            for (int j = 0; j < bdArray.queries.get(i).longTimeList.size(); j++) {// Прохід по значеннях сигналів у поточному масиві
                if (bdArray.queries.get(i).longTimeList.get(j) > podyomList.get(podyomID).stopTime) {// Переходить до наступного підйому, якщо час перевищив значення поточного підйому
                    if(podyomID < podyomList.size() - 1)
                    podyomID++;
                    else {
                        podyomID = 0;// Не пам'ятаю чи потрібно
                        break;
                    }
                }
                if (bdArray.queries.get(i).longTimeList.get(j) >= podyomList.get(podyomID).startTime) {// Якщо значення часу перевищує початкове значення виконується перевірка на метод запису сигналу до підйому
                    //TODO Дописать запись начального значения сигнала в зависимости от времени +
                    if (podyomList.get(podyomID).signals.size() == 0 || j == 0){// Запис початкового значення
                        if (bdArray.queries.get(i).longTimeList.get(j) == podyomList.get(podyomID).startTime) {// при співпаданні часу
                            podyomList.get(podyomID).signals.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                            podyomList.get(podyomID).times.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                        }
                        else {// якщо час відрізняється
                            podyomList.get(podyomID).times.get(i - 1).add(podyomList.get(podyomID).startTime);
                            podyomList.get(podyomID).signals.get(i - 1).add(getYByX(podyomList.get(podyomID).startTime, bdArray.queries.get(i).longTimeList.get(j - 1), bdArray.queries.get(i).longTimeList.get(j), bdArray.queries.get(i).valueList.get(j - 1), bdArray.queries.get(i).valueList.get(j)));
                            //TODO дописати запис сигналу що відрізняється +
                            podyomList.get(podyomID).signals.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                            podyomList.get(podyomID).times.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                        }
                    }
                    else if (bdArray.queries.get(i).longTimeList.get(j + 1) > podyomList.get(podyomID).stopTime && bdArray.queries.get(i).longTimeList.get(j + 1) != podyomList.get(podyomID).stopTime) {//Якщо наступна ітерація перевищить stopTime
                        podyomList.get(podyomID).times.get(i - 1).add(podyomList.get(podyomID).stopTime);
                        podyomList.get(podyomID).signals.get(i - 1).add(getYByX(podyomList.get(podyomID).stopTime, bdArray.queries.get(i).longTimeList.get(j), bdArray.queries.get(i).longTimeList.get(j + 1), bdArray.queries.get(i).valueList.get(j), bdArray.queries.get(i).valueList.get(j + 1)));
                    }
                    else {// Запис проміжних комбінацій
                        podyomList.get(podyomID).signals.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                        podyomList.get(podyomID).times.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                    }
                }
            }
        }
    }

    private void makeRealisations(){ // Обрізає по найменшому. Можливо треба переписати
        int minimumPodyomQuantsize = 0;
        for (int i = 0; i < podyomList.size(); i++) {
            if (i == 0){
                minimumPodyomQuantsize = podyomList.get(0).binaryMatrixs.get(0).size();
            }
            else {
                if (minimumPodyomQuantsize > podyomList.get(i).binaryMatrixs.get(0).size())
                minimumPodyomQuantsize = podyomList.get(i).binaryMatrixs.get(0).size();
            }
        }
        for (int i = 0; i < podyomList.size(); i++) {
            realisations.add(new ArrayList<Integer>());
            for (int j = 0; j < minimumPodyomQuantsize; j++) {
                for (int k = 0; k < podyomList.get(i).binaryMatrixs.size(); k++) {
                    realisations.get(i).add(podyomList.get(i).binaryMatrixs.get(k).get(j));
                }
            }
        }
    }

    private void getEtalon(){
        for (int i = 0; i < realisations.get(0).size(); i++) {
            int sumOfRows = 0;
            for (ArrayList<Integer> realisation : realisations) {
                sumOfRows += realisation.get(i);
            }
            if (sumOfRows >= realisations.size() / 2) // Возможно надо поменять условие
                etalon.add(1);
            else
                etalon.add(0);
        }
    }

    private void getDistance(){
        for (int i = 0; i < realisations.size(); i++) {
            distances.add(0);
            for (int j = 0; j < realisations.get(i).size(); j++) {
                if(!realisations.get(i).get(j).equals(etalon.get(j)))
                    distances.set(i, distances.get(i) + 1);
            }
        }
    }

    private double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }

}
