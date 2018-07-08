package org.daimhim.mepgenerate.action.mvpgenerate;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.SelectFromListDialog;
import com.intellij.openapi.vcs.changes.ui.SelectFilesDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class MvpGenerate extends BaseGenerateAction implements MvpGenerateContract.View{

    private ArrayList<PsiField> mPsiFields;
    private MvpGeneratePresenter mvpGeneratePresenter;
    private Project mProject;

    public MvpGenerate() {
        super(null);
    }

    public MvpGenerate(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert org.daimhim.mepgenerate.action logic here
        mvpGeneratePresenter = new MvpGeneratePresenter();
        mvpGeneratePresenter.startView(this);
        mProject = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, mProject);
        PsiClass psiClass = getTargetClass(editor, mFile);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        mvpGeneratePresenter.setTagParameter(mProject,virtualFile,psiClass);
        String mDefClassName = mvpGeneratePresenter.inputMvpName();
        if (mDefClassName == null || "".equals(mDefClassName)){
            showErrorDialog("请输入类名","请输入类名");
        }else {
            WriteCommandAction.runWriteCommandAction(mProject, mvpGeneratePresenter);
        }
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

    @Nullable
    public static VirtualFile getVirtualFile(List<VirtualFile> list, String title, Project mProject) {
        SelectFromListDialog demo = new SelectFromListDialog(mProject, list.toArray(), new SelectFromListDialog.ToStringAspect() {
            @Override
            public String getToStirng(Object o) {
                return ((VirtualFile) o).getName();
            }
        }, title, ListSelectionModel.SINGLE_SELECTION);
        demo.pack();
        demo.show();
        Object[] selection = demo.getSelection();
        if (null == selection || selection.length == 0){
            return null;
        }
        return (VirtualFile) selection[0];
    }


}
