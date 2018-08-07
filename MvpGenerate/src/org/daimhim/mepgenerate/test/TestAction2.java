package org.daimhim.mepgenerate.test;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;


public class TestAction2 extends BaseGenerateAction {
    public TestAction2() {
        super(null);
    }

    public TestAction2(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiClass psiClass = getTargetClass(editor, mFile);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        PsiMethod[] allMethods = psiClass.getMethods();
        for (int i = 0; i < allMethods.length; i++) {
            System.out.println(allMethods[i].getName() + "   " +
                    psiClass.findMethodBySignature(allMethods[i],false));
        }
        Collection<HierarchicalMethodSignature> visibleSignatures = psiClass.getVisibleSignatures();
        Iterator<HierarchicalMethodSignature> iterator = visibleSignatures.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().getName());
        }
    }

}
