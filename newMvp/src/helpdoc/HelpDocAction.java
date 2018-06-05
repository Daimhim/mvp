package helpdoc;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * Created by Daimhim on 2017/8/9.
 */
public class HelpDocAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Messages.showErrorDialog(
                "我可以帮你尽快掌握该插件",
                "帮助文档");
    }
}
