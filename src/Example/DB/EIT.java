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
* 4) сравнивая каждое значение с верхней и нижней границей мы получаем 0 или 1(попадает в инт.ервал)
* 5) по бинарной матрице считаются средние значения по столбцам: если значение > 0,5 - 0, больше - 1. Так мы получим эталонный вектор
* 6) сравнивая каждую реализацию бинарной матрицы с эталонным вектором, ищем кодовое расстояние(отличие строк бинарной матрицы от эталонного вектора в еденицах).
* 7) То же самое делаем из соседним классом и сравниваем
* 8) Считаем сколько каких реализаций вместилос ьв каждый радиус
* 9) Посчитать КФЕ(по сфотканой формуле)
* 10) Посчитать (2.7.4)
* 11) Построить график 2.7.4 от радиуса
 */
public class EIT {
    ArrayList<ArrayList<Double>> signal = new ArrayList<ArrayList<Double>>(10);
    ArrayList<ArrayList<Long>> time = new ArrayList<ArrayList<Long>>(10);
    ArrayList<Long> timeUp = new ArrayList<Long>();
    ArrayList<Podyom> podyomList = new ArrayList<Podyom>();
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
        BDArray dad = new BDArray(queryList);
/*        LineChartZH lineChartZH = new LineChartZH("Работа системы с " + dad.queries.get(0).source + "_" + dad.queries.get(0).sourceID, dad.queries);
        lineChartZH.pack();
        RefineryUtilities.centerFrameOnScreen(lineChartZH);
        lineChartZH.setVisible(true);*/
        EIT eit = new EIT(-522D, 49D);
        eit.getPodyom(dad);
        eit.getPodyomSignals(dad);
        Podyom podyom = new Podyom(114L, 1113L);


    }
    public EIT(double minimumDownValue, double maximumHighValue){
        this.minimumDownValue = minimumDownValue;
        this.maximumHighValue = maximumHighValue;
    }
    public void getPodyom(BDArray bdArray){
        for (int i = 1; i < bdArray.queries.get(0).valueList.size(); i++) { // Прохід по значенню положення скіпа
            if (bdArray.queries.get(0).valueList.get(i) - bdArray.queries.get(0).valueList.get(i - 1) > 0 && bdArray.queries.get(0).valueList.get(i) > minimumDownValue) { // Умова підйому
                this.podyomList.add(new Podyom());
                podyomList.get(podyomList.size() - 1).startTime = bdArray.queries.get(0).longTimeList.get(i);
                for (int j = i + 1; j < bdArray.queries.get(0).valueList.size(); j++) {
                    if (bdArray.queries.get(0).valueList.get(j) - bdArray.queries.get(0).valueList.get(j - 1) < 0 && bdArray.queries.get(0).valueList.get(j) < maximumHighValue) {// Умова закінчення підйому чи досягення максимального верхнього значення
                        System.out.println(bdArray.queries.get(0).valueList.get(j - 1) + " " + bdArray.queries.get(0).valueList.get(j));
                        podyomList.get(podyomList.size() - 1).stopTime = bdArray.queries.get(0).longTimeList.get(j);
                        i = j;
                        break;
                    }
                }
            }
        }
    }
    public void getPodyomSignals(BDArray bdArray){// Запис сигналів до отриманих підйомів
        int podyomID = 0;
        for (int i = 1; i < bdArray.queries.size(); i++) { // Прохід по масивам сигналаів, отриманих з БД

            for (Podyom podyom : podyomList){ // Створення масивів даних та часу у підйомах
                podyom.signal.add(new ArrayList<Double>());
                podyom.time.add(new ArrayList<Long>());
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
                    if (podyomList.get(podyomID).signal.size() == 0 || j == 0){// Запис початкового значення
                        if (bdArray.queries.get(i).longTimeList.get(j) == podyomList.get(podyomID).startTime) {// при співпаданні часу
                            podyomList.get(podyomID).signal.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                            podyomList.get(podyomID).time.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                        }
                        else {// якщо час відрізняється
                            podyomList.get(podyomID).time.get(i - 1).add(podyomList.get(podyomID).startTime);
                            podyomList.get(podyomID).signal.get(i - 1).add(getYByX(podyomList.get(podyomID).startTime, bdArray.queries.get(i).longTimeList.get(j - 1), bdArray.queries.get(i).longTimeList.get(j), bdArray.queries.get(i).valueList.get(j - 1), bdArray.queries.get(i).valueList.get(j)));
                            //TODO дописати запис сигналу що відрізняється +
                            podyomList.get(podyomID).signal.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                            podyomList.get(podyomID).time.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                        }
                    }
                    else if (bdArray.queries.get(i).longTimeList.get(j + 1) > podyomList.get(podyomID).stopTime && bdArray.queries.get(i).longTimeList.get(j + 1) != podyomList.get(podyomID).stopTime) {//Якщо наступна ітерація перевищить stopTime
                        podyomList.get(podyomID).time.get(i - 1).add(podyomList.get(podyomID).stopTime);
                        podyomList.get(podyomID).signal.get(i - 1).add(getYByX(podyomList.get(podyomID).stopTime, bdArray.queries.get(i).longTimeList.get(j), bdArray.queries.get(i).longTimeList.get(j + 1), bdArray.queries.get(i).valueList.get(j), bdArray.queries.get(i).valueList.get(j + 1)));
                    }
                    else {// Запис проміжних комбінацій
                        podyomList.get(podyomID).signal.get(i - 1).add(bdArray.queries.get(i).valueList.get(j));
                        podyomList.get(podyomID).time.get(i - 1).add(bdArray.queries.get(i).longTimeList.get(j));
                    }
                }
            }
        }

    }

    private double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }

}
