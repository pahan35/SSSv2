package Example.DB;
import java.util.ArrayList;

/**
 * Created by пк on 01.07.2015.
 */
public class DBLoad {
    public static void main(String[] args){
        long l= 10007000;
        System.out.println(timeDifference(l));
    }


    public static String timeDifference(long timeDifference1) {
            long timeDiff = timeDifference1/1000;
            int h = (int) (timeDiff / (3600));
            int m = (int) ((timeDiff - (h * 3600)) / 60);
            int s = (int) (timeDiff - (h * 3600) - m * 60);
            System.out.println(String.format("%02d:%02d:%02d", h,m,s));
            return String.format("%02d:%02d:%02d", h,m,s);

        }

}
