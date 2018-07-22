package org.daimhim.mepgenerate.action.mvpsettings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import org.daimhim.mepgenerate.ui.MvpSettingsPanel;

public class MvpSettingsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        MvpSettingsPanel dialog = new MvpSettingsPanel(event.getData(PlatformDataKeys.PROJECT));
        dialog.showMvpSettingsPanel();
    }
}
