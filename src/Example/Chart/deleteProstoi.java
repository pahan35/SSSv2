package Example.Chart;


import java.util.ArrayList;
import java.util.Random;


/**
 * Created by пк on 03.07.2015.
 */
public class deleteProstoi {
    public static void main(String[] args) {
        System.out.println(ri());
        System.out.println(ri());
        System.out.println(ri());

    }

    public static int ri() {
        Random d = new Random();
        int ii = d.nextInt(50);
        return ii;
    }
    public static void deleteProstoi(double[] lDouble, double porog, double lShum){
        for (int i = 0; i < lDouble.length; i++) {
            if(i > 0) {
                if (lDouble[i] < porog && lDouble[i]- lDouble[i-1] < lShum){
                    // write code

                }
            }
        }

    }
}

