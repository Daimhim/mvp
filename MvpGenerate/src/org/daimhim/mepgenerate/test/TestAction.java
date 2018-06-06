package org.daimhim.mepgenerate.test;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

public class TestAction extends BaseGenerateAction {
    public TestAction() {
        super(null);
    }

    public TestAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        super.actionPerformed(event);
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

    }

}
