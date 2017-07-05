/**
 * Anomaly is a class to detect if a purchase is anomaly
 * Anomaly definition: user's purchase amount is greater than mean + 3 * standard deviation
 * Mean: the average of all friends' purchases
 * Standard deviation: the standard deviation of all friends' purchases
 * when a user's purchase and his/her friends' purchase array are given.
 * It has methods to calculate the mean and standard deviation of his/her friends' purchase array
 */

public class Anomaly {

    //true: it is an anomaly
    //false: it is NOT an anomaly
    public static boolean detect(float mypurchase, float[]friendpurchase) {
        if (friendpurchase.length == 0) {
            throw new IllegalArgumentException("FRIEND PURCHASE is ZERO");
        }
        if (friendpurchase.length < 2) {
            return false;
        }
        float mean = meanValue(friendpurchase);
        float std = stdValue(mean, friendpurchase);
        if (mypurchase > mean + 3 * std) {
            return true;
        }
        return false;
    }

    //calculate the mean value of friendpurchase array
    private static float meanValue(float[] friendpurchase) {
        float mean = 0;
        for (int i = 0; i < friendpurchase.length; i++) {
            mean += friendpurchase[i];
        }
        mean = mean / friendpurchase.length;
        return mean;
    }

    //calculate the standard deviation of friendpurchase array
    private static float stdValue(float mean, float[] friendpurchase) {
        float std = 0;
        for (int i = 0; i < friendpurchase.length; i++) {
            float a = mean - friendpurchase[i];
            std += a * a;
        }
        std = (float) Math.sqrt(std / friendpurchase.length);
        return std;
    }

    public static float getMean(float[] friendpurchase) {
        return meanValue(friendpurchase);
    }

    public static float getStd(float[] friendpurchase) {
        return stdValue(meanValue(friendpurchase), friendpurchase);
    }
}
