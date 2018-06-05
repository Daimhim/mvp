package test;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import general.BaseAnAction;
import toolbox.QLog;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

/**
 * Created by Daimhim on 2017/8/25.
 */
public class TestAction extends BaseAnAction<TestHelp> implements TestContract.Action{
    protected String TAG = getClass().getSimpleName();
    @Override
    public void actionPerformed(AnActionEvent event) {
        super.actionPerformed(event);
        VirtualFile data = event.getData(VIRTUAL_FILE);
        QLog.v(TAG,"data.getName():"+data.getName());
        QLog.v(TAG,"data.getPath():"+data.getPath());
        QLog.v(TAG,"data.getUrl():"+data.getUrl());
        QLog.v(TAG,"data.getPresentableUrl():"+data.getPresentableUrl());
        QLog.v(TAG,"data.getPresentableName():"+data.getPresentableName());
        QLog.v(TAG,"data.getFileType():"+data.getFileType());
        QLog.v(TAG,"data.getParent():"+data.getParent());
        QLog.v(TAG,"data.getDetectedLineSeparator():"+data.getDetectedLineSeparator());

        PsiDirectory directory = PsiDirectoryFactory.getInstance(event.getProject()).createDirectory(data);
        QLog.v(TAG,"directory.getName():"+ directory.getName());
        PsiFile a = directory.findFile("A.java");
        QLog.v(TAG,"a.getName():"+a.getName());
        QLog.v(TAG,"a.getText():"+a.getText());
        PsiClass as = PsiTreeUtil.findChildOfAnyType(a, PsiClass.class);
        QLog.v(TAG,"as.getQualifiedName():"+as.getQualifiedName());
        PsiFile b = directory.findFile("B.java");
        PsiClassType[] superTypes = as.getSuperTypes();
        QLog.v(TAG,"superTypes.length:"+superTypes.length);
        for (int i = 0; i < superTypes.length; i++) {
            QLog.v(TAG,"superTypes.length:"+superTypes[i].getClassName());
        }
        PsiClassType[] implementsListTypes = as.getImplementsListTypes();
        QLog.v(TAG,"implementsListTypes.length:"+implementsListTypes.length);
        for (int i = 0; i < implementsListTypes.length; i++) {
            QLog.v(TAG,"implementsListTypes.length:"+implementsListTypes[i].getClassName());
        }
        PsiField[] allFields = as.getAllFields();
        QLog.v(TAG,"allFields.length:"+allFields.length);
        for (int i = 0; i < allFields.length; i++) {
            QLog.v(TAG,"allFields.length:"+allFields[i].getName().toString().trim());
        }
        PsiClass[] allInnerClasses = as.getAllInnerClasses();
        QLog.v(TAG,"allInnerClasses.length:"+allInnerClasses.length);
        for (int i = 0; i < allInnerClasses.length; i++) {
            QLog.v(TAG,"allInnerClasses.length:"+allInnerClasses[i].getName().toString().trim());
        }
        PsiTypeParameter[] typeParameters = as.getTypeParameters();
        QLog.v(TAG,"typeParameters.length:"+typeParameters.length);
        for (int i = 0; i < typeParameters.length; i++) {
            QLog.v(TAG,"typeParameters.length:"+typeParameters[i].getName().trim());
        }
        PsiMethod[] methods = as.getMethods();
        QLog.v(TAG,"methods.length:"+methods.length);
        for (int i = 0; i < methods.length; i++) {
            QLog.v(TAG,"methods.length:"+methods[i].getModifierList().getText().toString());
        }
//        WriteCommandAction.runWriteCommandAction(event.getProject(), new Runnable() {
//            @Override
//            public void run() {
//                as.getTypeParameterList().delete();
//            }
//        });
//        processor.execute(new LightFieldBuilder(name, psiTypeC, aClass), state)
//        PsiFile[] files = directory.getFiles();
//        for (PsiFile file: files){
//            PsiClass childOfAnyType = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
//            QLog.v(TAG,"childOfAnyType.getImplementsList():"+childOfAnyType.getImplementsList().getText().toString());
//            QLog.v(TAG,"childOfAnyType.getInnerClasses():"+childOfAnyType.getInnerClasses().length);
//            QLog.v(TAG,"childOfAnyType.getExtendsList():"+childOfAnyType.getExtendsList().getText().toString());
//            QLog.v(TAG,"childOfAnyType.getExtendsList():"+childOfAnyType.getExtendsList().add());
//        }

    }


    public PsiType buildGenericType(Project project, String name) {
        int index = name.indexOf("<");
        String genericName = name.substring(0, index).trim();
        String elementName = name.substring(index + 1, name.indexOf(">")).trim();
        if (genericName.equals("List")) genericName = "java.util.List";
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiElementFactory factory = facade.getElementFactory();
        GlobalSearchScope globalsearchscope = GlobalSearchScope.allScope(project);
        PsiClass genericClass = facade.findClass(genericName, globalsearchscope);
        PsiType elementType =  factory.createType(facade.findClass(elementName, globalsearchscope));
        PsiSubstitutor substitutor = PsiSubstitutor.EMPTY.putAll(genericClass, new PsiType[] { elementType });
        return JavaPsiFacade.getElementFactory(project).createType(genericClass, substitutor);
    }
}
