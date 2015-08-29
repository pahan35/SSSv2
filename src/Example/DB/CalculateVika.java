package Example.DB;

import java.util.Hashtable;

/**
 * Created by пк on 03.07.2015.
 */
public class CalculateVika {


            public static void main(String [] args)
        {
            int[][] x = new int[][]{
                    {4, 5, 6, 10, 11, 12, 100, 101, 200, 250, 300, 301, 400},
                    {6, 9, 7, 11, 12, 13, 110, 111, 210, 260, 310, 311, 411}};

            int[][] y = new int[][]{
                    {5, 8, 9, 10, 12, 13, 15, 14, 48, 50, 70, 88, 101, 123},
                    {10, 16, 18, 20, 24, 26, 30, 28, 96, 100, 140, 176, 199, 220}};

            for(int[] row : y)
            {
                for (int p : row)
                {
                    System.out.print(p);
                    System.out.print("\t");
                }
                System.out.println();
            }

            System.out.println();

            Hashtable<Long, double[]> data = new Hashtable<Long, double[]>();

            int sensorsAmount = 2;
            int[] curIndex = new int[sensorsAmount];
            for(int c = 0; c < curIndex.length; c++)
            {
                curIndex[c] = 1;
            }
            long inTime = 250;

            long curTime = inTime;

            boolean time = true;
            while(time)
            {
                double[] temp = new double[sensorsAmount];
                for(int s = 0; s < sensorsAmount; s++)
                {
                    while(curIndex[s] < x[s].length)
                    {
                        if(x[s][curIndex[s]] >= curTime)
                        {
                            temp[s] = getYByX(curTime, x[s][curIndex[s] - 1], y[s][curIndex[s] - 1], x[s][curIndex[s]],
                                    y[s][curIndex[s]]);
                            break;
                        }
                        else
                        {
                            curIndex[s]++;
                        }
                    }
                    if(curIndex[s] >= x[s].length)
                    {
                        time = false;
                    }
                }
                data.put(curTime, temp);
                curTime += 100;
            }

            for(int i = 0; i < data.size(); i++)
            {
                long ct = i * 100 + inTime;
                System.out.printf("curTime: %d; sensor1: %.2f; sensor2: %.2f", ct, data.get(ct)[0], data.get(ct)[1]);
                System.out.println();
            }
        }

        public static double getYByX(double x, double xs, double ys, double xe, double ye)
        {
            return (((x - xs)/(xe - xs)) * (ye - ys)) + ys;
        }
    }

