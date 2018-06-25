package org.daimhim.mepgenerate.action.newmvp;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.vfs.VirtualFile;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

public class NewMvpAction extends AnAction implements NewMvpActionContract.View {

    private NewMvpActionContract.Presenter mNewMvpActionPresenter;

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        //获取Project根目录
        VirtualFile data = e.getData(VIRTUAL_FILE);
        Presentation presentation = e.getPresentation();
        if (isNewMVPSimple(data.getPath())) {
            presentation.setEnabledAndVisible(true);
        } else {
            presentation.setEnabledAndVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

    }

    public static boolean isNewMVPSimple(String path) {
        if (path.indexOf("/src/") < 0) return false;
        return true;
    }
}

