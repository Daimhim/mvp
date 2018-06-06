package toolbox;

import resources.string;


/**
 * Created by Daimhim on 2017/8/10.
 */
public class TextUtils {

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }
    public static <T> T checkNull(Object object){
        if (object == null){
            new NullPointerException(object.getClass().getSimpleName()+ string.NULL_ERROR);
        }
        return (T)object;
    }
}
