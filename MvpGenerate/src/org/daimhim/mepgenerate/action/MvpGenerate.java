package org.daimhim.mepgenerate.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.JavaPsiFacadeImpl;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilBase;
import groovy.json.internal.ArrayUtils;
import kotlin.reflect.jvm.internal.impl.serialization.jvm.JvmModuleProtoBuf;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.help.VirtualFileHelp;

import java.util.ArrayList;
import java.util.List;

public class MvpGenerate extends BaseGenerateAction {


    private MvpGeneratePresenter mvpGeneratePresenter;

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
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiClass psiClass = getTargetClass(editor, mFile);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);

    }

    //根据路径获取包名
    public String getPackageName(VirtualFile data) {
        if (null == data) return null;
        String path = data.getPath();
        return (path.substring(path.indexOf("java/") + "java/".length(), path.length())).replace("/", ".");
    }

    /**
     * 获取工作目录路径
     * @param data
     * @return
     */
    public String getWorkList(VirtualFile data){
        return data.getPath().substring(0,data.getPath().indexOf("src/main/java"));
    }

    /**
     * 检查当前类中是否存在P层
     * @param psiClass 目标类
     * @return  返回值
     */
    public boolean checkPastPresenter(PsiClass psiClass){
        PsiField[] fields = psiClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            PsiField field = fields[i];
            System.out.println(field.getName()+"  "+ field.getType()+"     "+field.getNameIdentifier().getText());

        }
        return false;
    }

    private static String getPsiClass(PsiClass psiClass){
        String[] nameSuffix = {
                "Activity",
                "View",
                "Dialog",
                "Fragment",
                "PopupWindow"
        };
        String name = psiClass.getQualifiedName();
        for (int i = 0; i < nameSuffix.length; i++) {
            if (null!=name && name.endsWith(nameSuffix[i])){
                return name.substring(0,name.length() - nameSuffix[i].length());
            }
        }
        return name;
    }
}
