package general;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by Daimhim on 2017/8/31.
 */
public interface CreateHelp {
    void onCreate(AnActionEvent anActionEvent);
    void onPrepare();
    void onBackstage();
    void onStop();
}
