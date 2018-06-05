package general;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

/**
 * Created by Daimhim on 2017/8/31.
 */
public abstract class BaseAnAction<H extends BaseMVPHelp> extends AnAction {
    protected String TAG = getClass().getSimpleName();
    protected H help;

    @Override
    public void update(AnActionEvent event) {
        //获取Project根目录
        boolean newMVP = BaseMVPHelp.isNewMVPSimple(event.getData(VIRTUAL_FILE).getPath());
        if (newMVP) {
            Type t = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) t).getActualTypeArguments();
            Class<H> cls = (Class<H>) params[0];
            help = ClassHelp.INSTANCE.create(cls);
        }
        event.getPresentation().setEnabledAndVisible(newMVP);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        onCreate(e);
        init(e);
    }
    public void init(AnActionEvent e){
        help.setAction(this);
        help.onCreate(e);
    }
    public void onCreate(AnActionEvent e){

    }
}
