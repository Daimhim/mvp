package org.daimhim.mepgenerate.action;

import com.intellij.psi.*;

import java.util.Objects;

public class MvpGeneratePresenter {

    public PsiClass createContract(String className,PsiDirectory directory,String packageName){

        PsiClass psiClass = JavaDirectoryService.getInstance().createInterface(directory, className);
        //设置修饰属性
        Objects.requireNonNull(psiClass.getModifierList()).setModifierProperty(PsiModifier.PUBLIC, true);
        ((PsiJavaFile) psiClass.getContainingFile()).setPackageName(packageName);
        return psiClass;
    }

    public void registrationContract(){

    }

    public void followContract(){

    }


}
