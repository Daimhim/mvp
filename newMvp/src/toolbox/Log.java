package toolbox;

/**
 * Created by Daimhim on 2017/7/7.
 */
public class Log {
    public static void i(String text, String content) {
        System.out.println(text + ":" + content);
    }
    public static void i(String content) {
        System.out.println("Log" + ":" + content);
    }

}
