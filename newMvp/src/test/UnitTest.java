package test;

import toolbox.QLog;

/**
 * Created by Daimhim on 2017/8/7.
 */
public class UnitTest {

    protected static String TAG = "UnitTest";

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        QLog.v(TAG,thread.getName());
        Integer i = 127;
        Integer n = 127;
        System.out.println("-----1----" + (i == n));

        Integer k = 128;
        Integer t = 128;
        System.out.println("-----2----" + (k == t));
    }
    private synchronized static void ptint(String s){
        Thread thread = Thread.currentThread();
        QLog.v(TAG,"accept:"+thread.getName());
        QLog.v(TAG,s);
    }
}
