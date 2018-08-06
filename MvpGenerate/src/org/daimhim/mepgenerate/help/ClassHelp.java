package org.daimhim.mepgenerate.help;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiMethodImpl;

public class ClassHelp {
    static String TAG = ClassHelp.class.getSimpleName();
    //添加抽象方法
    public static void abstractImplementation(PsiElementFactory elementFactory, PsiClass absClass, PsiClass impClass) {
        //添加抽象方法
        PsiMethod method = null;
        //构造方法
        for (PsiMethod constructorMethod : absClass.getConstructors()) {
            //已有 不需要创建
            if (null == findMethod(constructorMethod, impClass)) {    //创建一个新的
                method = elementFactory.createConstructor(constructorMethod.getName(),impClass);
                method.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
                PsiParameter[] parameters = constructorMethod.getParameterList().getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    method.getParameterList().add(elementFactory.createParameter(parameters[i].getName(),parameters[i].getType(),impClass));
                    QLog.v(TAG,"getName:"+parameters[i].getName());
                    QLog.v(TAG,parameters[i].getTypeElement().getText()+"   parameters:"+parameters[i].getName());
                }
                impClass.add(method);
                findMethod(constructorMethod,impClass);
            }
            method = findMethod(constructorMethod, impClass);
            //复制注释
            if (null != constructorMethod.getDocComment() && null == method.getDocComment()) {
                method.addRangeBefore(constructorMethod.getDocComment(), constructorMethod.getDocComment(), method.getFirstChild());
            }
        }
        //抽象方法
        PsiType returnType = null;
        for (PsiMethod absMethod : absClass.getAllMethods()){
            if (absMethod.getModifierList().hasExplicitModifier(PsiModifier.ABSTRACT)
                    || (absClass.isInterface() && findMethod(absMethod,impClass)==null)) {
                //已有 不需要创建
                if (null == findMethod(absMethod, impClass)) {    //创建一个新的
                    method = elementFactory.createMethod(absMethod.getName(), absMethod.getReturnType(), impClass);
                    method.getModifierList().addAnnotation("Override");
                    //复制方法参数
                    for (PsiParameter parameters : absMethod.getParameterList().getParameters()) {
                        method.getParameterList().add(parameters);
                    }
                    //复制方法异常
                    for (PsiJavaCodeReferenceElement element : absMethod.getThrowsList().getReferenceElements()) {
                        method.getThrowsList().add(element);
                    }
                    //添加默认返回值
                    returnType = method.getReturnType();
                    if (!returnType.equalsToText("void")){
                        if (returnType.equalsToText("int") || returnType.equalsToText("Integer")
                                || returnType.equalsToText("Double") || returnType.equalsToText("double")
                                || returnType.equalsToText("float") || returnType.equalsToText("Float")
                                || returnType.equalsToText("Long") || returnType.equalsToText("Long")){

                            method.getBody().add(elementFactory.createStatementFromText("return 0;", method));
                        }else {
                            method.getBody().add(elementFactory.createStatementFromText("return null;",method));
                        }
                    }
                    impClass.add(method);
                }
                method = findMethod(method, impClass);
                //复制注释
                if (null != absMethod.getDocComment() && null == method.getDocComment()) {
                    method.addRangeBefore(absMethod.getDocComment(), absMethod.getDocComment(), method.getFirstChild());
                }
            }
        }
    }

    private static PsiMethod findMethod(PsiMethod absMethod, PsiClass impClass) {
        for (PsiMethod method :
                impClass.findMethodsByName(absMethod.getName(), false)) {
            if (absMethod.getParameterList().getParametersCount() == method.getParameterList().getParametersCount()
                    && absMethod.getReturnType().getCanonicalText().equals(method.getReturnType().getCanonicalText())) {
                return method;
            }
        }
        return null;
    }
}
