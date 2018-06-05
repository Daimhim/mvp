package org.daimhim.mepgenerate.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.JavaPsiFacadeImpl;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilBase;
import groovy.json.internal.ArrayUtils;
import kotlin.reflect.jvm.internal.impl.serialization.jvm.JvmModuleProtoBuf;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.daimhim.mepgenerate.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class MvpGenerate extends BaseGenerateAction {


    public MvpGenerate() {
        super(null);
    }

    public MvpGenerate(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert org.daimhim.mepgenerate.action logic here
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiClass psiClass = getTargetClass(editor, mFile);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        System.out.println("psiClass.getName:"+psiClass.getName());
        System.out.println("project.getBasePath:"+project.getBasePath());
        System.out.println("virtualFile.getPresentableName:"+virtualFile.getPresentableName());
        System.out.println("virtualFile.getCanonicalPath:"+virtualFile.getCanonicalPath());
        System.out.println("virtualFile.getExtension:"+virtualFile.getExtension());
        System.out.println("virtualFile.getParent:"+virtualFile.getParent());
        System.out.println("virtualFile.getParent.getName():"+virtualFile.getParent().getName());
        System.out.println("virtualFile.getPresentableUrl:"+virtualFile.getPresentableUrl());
        String path = virtualFile.getPath();
        System.out.println("virtualFile.getPath:"+ path);
        System.out.println(getPackageName(virtualFile));
        String substring = path.substring(0, path.indexOf("src/main/java"));
        System.out.println("substring:"+substring);
        System.out.println("project.getProjectFile:"+project.getProjectFile());
        System.out.println("project.getWorkspaceFile:"+project.getWorkspaceFile());
        System.out.println("project.getBaseDir:"+project.getBaseDir());
        System.out.println("project.getProjectFilePath:"+project.getProjectFilePath());
        System.out.println("psiClass.getQualifiedName:"+psiClass.getQualifiedName());
        System.out.println("project.getWorkspaceFile().getPath():"+project.getWorkspaceFile());
//        PsiPackage mvp = JavaPsiFacade.getInstance(project).findPackage("mvp");
//        System.out.println("mvp:"+mvp.containsClassNamed("BaseView"));
//        System.out.println("mvp:"+mvp.getText());
//        DocumentImpl document = new DocumentImpl(substring);
//        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
//        System.out.println("file.getName():"+file.getName());

        Module moduleForFile = ModuleUtil.findModuleForFile(virtualFile, project);

        System.out.println(moduleForFile.getModuleFilePath());
        ArrayList<VirtualFile> mvpfiles = findMvpfiles(null, virtualFile);
        System.out.println(mvpfiles.toArray());

    }
    private ArrayList<VirtualFile> findMvpfiles(ArrayList<VirtualFile> list, VirtualFile virtualFile){
        if (list == null){
            list = new ArrayList<>();
        }
        if ("src".equals(virtualFile.getName())){
            return list;
        }
        if (virtualFile.isDirectory()){
            if ("mvp".equals(virtualFile.getName())){
                list.add(virtualFile);
            }
            for (VirtualFile fileFile :
                    virtualFile.getChildren()) {
                VirtualFile mvp = virtualFile.findChild("mvp");
                if (mvp == null){
                    findMvpfiles(list,virtualFile.getParent());
                }else {
                    findMvpfiles(list,virtualFile.getParent());
                }
            }
        }else {
            return list;
        }
        return list;
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
}
