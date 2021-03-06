package org.daimhim.mepgenerate.action.newmvp;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.impl.PsiManagerImpl;
import org.daimhim.mepgenerate.ui.NewMvpPanel;

import java.util.List;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static org.daimhim.mepgenerate.action.mvpgenerate.MvpGenerate.getVirtualFile;

public class NewMvpAction extends AnAction implements NewMvpActionContract.View {

    private NewMvpActionPresenterImpl2 mNewMvpActionPresenter = new NewMvpActionPresenterImpl2();
    private Project mProject;

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        //获取Project根目录
        VirtualFile data = e.getData(VIRTUAL_FILE);
        Presentation presentation = e.getPresentation();
        if (null!=data && isNewMVPSimple(data.getPath())) {
            presentation.setEnabledAndVisible(true);
        } else {
            presentation.setEnabledAndVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        mProject = event.getData(PlatformDataKeys.PROJECT);
        mNewMvpActionPresenter.startView(this);
        PsiDirectory directory = null;
        if (null!=virtualFile && virtualFile.isDirectory()) {
            directory = PsiManagerImpl.getInstance(mProject).findDirectory(virtualFile);
        }else if (null!=virtualFile){
            directory = PsiManagerImpl.getInstance(mProject).findDirectory(virtualFile.getParent());
        }
        NewMvpPanel newMvpPanel = new NewMvpPanel(mProject,directory);
        newMvpPanel.showNewMvpPanel();
        mNewMvpActionPresenter.setTagParameter(mProject, directory,newMvpPanel.getMvpParameter());
        String mDefClassName = mNewMvpActionPresenter.initMvpName();
        if (mDefClassName == null || "".equals(mDefClassName)){
            showErrorDialog("请输入包名","请输入包名");
        }else {
            WriteCommandAction.runWriteCommandAction(mProject, mNewMvpActionPresenter);
        }
    }

    public static boolean isNewMVPSimple(String path) {
        if (path.indexOf("/src/") < 0) return false;
        return true;
    }

    @Override
    public String showInputDialog(String message, String title) {
        return Messages.showInputDialog(mProject, message,
                title, Messages.getQuestionIcon());
    }

    @Override
    public void showErrorDialog(String message, String title) {
        Messages.showErrorDialog(message,title);
    }

    @Override
    public void showStatusNotice(String message) {
        WindowManager.getInstance().getStatusBar(mProject).setInfo(message);
    }

    @Override
    public VirtualFile getUserSelectClass(List<VirtualFile> list,String title) {
        return getVirtualFile(list, title, mProject);
    }
}

