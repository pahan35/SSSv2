package Work.DB;

import Example.DB.BDArray;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by пк on 08.07.2015.
 */
        public class DBLoad {
            public static void main(String[] args) {
                /*BDArray connectBD = new BDArray();
                //connectBD.BDArray();
                ArrayList<Long> timeKanat = connectBD.getLongKanat();
                ArrayList<Long> timeKanatOrigin = timeKanat;
                ArrayList<Long> timeOtklon = connectBD.getLongOtklon();
                ArrayList<Long> timeOtklonOrigin = timeOtklon;
                ArrayList<Double> valueKanat = connectBD.valueKanat();
                ArrayList<Double> valueOtklon = connectBD.valueOtklon();
                //podgonTimeArray(timeKanat,timeOtklon,valueKanat,valueOtklon);
                for (int i = 0; i < timeOtklon.size(); i++) {
                    System.out.println(valueOtklon.get(i) + " " + valueKanat.get(i));
                }
                System.out.println(timeKanat.get(timeKanat.size() - 1) + " " + timeOtklon.get(timeOtklon.size() - 1));
                System.out.println(valueKanat.get(timeKanat.size() - 1) + " " + valueOtklon.get(timeOtklon.size() - 1));*/
            }

    public static double getYByX(double x, double xStart, double xEnd, double yStart, double yEnd) // считает значение у по х и у0
    {
        return (((x - xStart)/(xEnd - xStart)) * (yEnd - yStart)) + yStart;
    }
    public static void podgonTimeArray(ArrayList<Long> timeKanat, ArrayList<Long> timeOtklon, ArrayList<Double> valueKanat, ArrayList<Double> valueOtklon){
        if (timeKanat.get(0) < timeOtklon.get(0)){
            long previousX = timeKanat.get(0);
            double previousY = valueKanat.get(0);
            while (timeKanat.get(0) < timeOtklon.get(0)){
                timeKanat.remove(0);
                valueKanat.remove(0);
            }
            if (!timeKanat.get(0).equals(timeOtklon.get(0))){
                valueKanat.add(0, getYByX(timeOtklon.get(0), previousX, timeKanat.get(0), previousY, valueKanat.get(0)));
                timeKanat.add(timeOtklon.get(0));}
        }
        else {
            long previousX = timeOtklon.get(0);
            double previousY = valueOtklon.get(0);

            while (timeKanat.get(0) < timeOtklon.get(0)){
                previousX = timeOtklon.get(0);
                previousY = valueOtklon.get(0);
                timeKanat.remove(0);
                valueKanat.remove(0);
            }
            if (!timeKanat.get(0).equals(timeOtklon.get(0))){
                valueKanat.add(0,getYByX(timeKanat.get(0),previousX,timeOtklon.get(0),previousY,valueOtklon.get(0)));
                timeOtklon.add(timeKanat.get(0)); }
        }
        if (!timeKanat.get(timeKanat.size() - 1).equals(timeOtklon.get(timeOtklon.size() - 1))){
            int OtklonSize = timeOtklon.size() - 1;
            int KanatSize = timeKanat.size() - 1;
            if (timeKanat.get(KanatSize) < (timeOtklon.get(OtklonSize))){
                long previousX = timeOtklon.get(OtklonSize);
                double previousY = valueOtklon.get(OtklonSize);
                while (timeKanat.get(KanatSize) < (timeOtklon.get(OtklonSize))){
                    previousX = timeOtklon.get(OtklonSize);
                    previousY = valueOtklon.get(OtklonSize);
                    timeOtklon.remove(OtklonSize);
                    valueOtklon.remove(OtklonSize);
                    OtklonSize = valueOtklon.size() - 1;
                }
                if (timeKanat.get(KanatSize) > (timeOtklon.get(OtklonSize))){
                    valueOtklon.add(getYByX(timeKanat.get(KanatSize),timeOtklon.get(OtklonSize), previousX, valueOtklon.get(OtklonSize), previousY));
                    timeOtklon.add(timeKanat.get(KanatSize));
                }
            }
            else if (timeKanat.get(KanatSize) > (timeOtklon.get(OtklonSize))){
                long previousX = timeKanat.get(KanatSize);
                double previousY = valueKanat.get(KanatSize);
                while (timeKanat.get(KanatSize) < (timeOtklon.get(OtklonSize))){
                    previousX = timeKanat.get(KanatSize);
                    previousY = valueKanat.get(KanatSize);
                    timeKanat.remove(KanatSize);
                    valueKanat.remove(KanatSize);
                    KanatSize = valueKanat.size() - 1;
                }
                if (timeKanat.get(KanatSize) < (timeOtklon.get(OtklonSize))){
                    valueKanat.add(getYByX(timeOtklon.get(OtklonSize),timeKanat.get(KanatSize), previousX, valueKanat.get(KanatSize), previousY));
                    timeKanat.add(timeOtklon.get(OtklonSize));
                }
            }
        }


    }
}

