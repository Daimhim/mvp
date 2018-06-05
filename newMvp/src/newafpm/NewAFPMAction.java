package newafpm;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import general.BaseAnAction;
import resources.string;
import toolbox.QLog;

import static general.BaseMVPHelp.classNameCorrection;
import static general.GlobalVariable.MVP_CLASS_SUFFIX;

/**
 * Created by Daimhim on 2017/8/31.
 */
public class NewAFPMAction extends BaseAnAction<NewAFPMHelp> {
    @Override
    public void onCreate(AnActionEvent e) {
        super.onCreate(e);
        String className = detectionClassName(classNameCorrection(Messages.showInputDialog(e.getProject(), string.INPUT_CLASS_NAME, string.PLUGIN_NAME, Messages.getQuestionIcon())));
        QLog.v(TAG,"className:"+className);
        help.setpClassName(className);
    }
    String detectionClassName(String className){
        for (int i = 0; i < MVP_CLASS_SUFFIX.length; i++) {
            if (className.endsWith(MVP_CLASS_SUFFIX[i])){
               return  className.replace(MVP_CLASS_SUFFIX[i],"");
            }
        }
        return className;
    }
}
