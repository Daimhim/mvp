package toolbox;


/**
 * 项目名称：basic
 * 项目版本：1.0.0
 * 创建人：Daimhim
 * 创建时间：2017/7/21 13:56
 * 修改人：Daimhim
 * 修改时间：2017/7/21 13:56
 * 类描述：
 * 修改备注：
 */

public class QLog {
    public static void v(String tag, String msg) {
        System.out.println(tag+":"+msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        System.out.println(tag+":"+msg);
        tr.printStackTrace();
    }
}
