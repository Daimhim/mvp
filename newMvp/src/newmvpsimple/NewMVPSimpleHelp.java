package newmvpsimple;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import resources.string;
import toolbox.QLog;
import toolbox.TextUtils;

import static general.GlobalVariable.*;


/**
 * Created by Daimhim on 2017/8/9.
 */
public class NewMVPSimpleHelp {
    protected String TAG = getClass().getSimpleName();
    SimpleView simpleView;

    public NewMVPSimpleHelp(SimpleView simpleView) {
        this.simpleView = simpleView;
    }


    public void createClass(Project project, PsiDirectory directory, String packageName, String className) {
        WriteCommandAction.runWriteCommandAction(project, new CreateClass(MVP_CLASS_SUFFIX, directory, packageName, className));
    }

    //检查当前包下是否有重复
    public PsiClass checkRepeat(PsiFile[] files, String className, String suffix) {
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName().replace(".java", "");
            if (name.endsWith(suffix) && name.equals(className + suffix)) {
                return PsiTreeUtil.findChildOfAnyType(files[i], PsiClass.class);
            }
        }
        return null;
    }

    //根据路径获取包名
    public String getPackageName(VirtualFile data) {
        if (null == data) return null;
        String path = data.getPath();
        return (path.substring(path.indexOf("java/") + "java/".length(), path.length())).replace("/", ".");
    }

    //根据类名返回 PsiClass
    public PsiClass findPsiClassFromPsiFile(PsiFile[] files, String className) {
        for (PsiFile itemFile : files) {
            if (TextUtils.equals(itemFile.getName(), className + ".java")) {
                return PsiTreeUtil.findChildOfAnyType(itemFile, PsiClass.class);
            }
        }
        return null;
    }

    private String getClassName(String className, String suffix) {
        return className + suffix;
    }

    class CreateClass implements Runnable {
        String[] suffix;
        PsiDirectory directory;
        String packageName;
        String className;

        public CreateClass(String[] suffix, PsiDirectory directory, String packageName, String className) {
            this.suffix = suffix;
            this.directory = directory;
            this.packageName = packageName;
            this.className = className;
        }

        @Override
        public void run() {
            PsiFile[] files = directory.getFiles();
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(directory.getProject());
            PsiClass psiClass = null;   //MVP
            PsiClass classByName = null;  //C
            StringBuffer stringBuffer = new StringBuffer();
            for (String itemSuffix : suffix) {
                psiClass = checkRepeat(files, className, itemSuffix);
                if (null == psiClass) {
                    if (TextUtils.equals(itemSuffix, MVP_CONTRACT)) {  //创建合约
                        psiClass = JavaDirectoryService.getInstance().createInterface(directory, className + itemSuffix);
                        //设置修饰属性
                        psiClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
                        ((PsiJavaFile) psiClass.getContainingFile()).setPackageName(packageName);
                    } else {  //创建 mvp
                        psiClass = JavaDirectoryService.getInstance().createClass(directory, className + itemSuffix);
                        //设置修饰属性
                        psiClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
                        ((PsiJavaFile) psiClass.getContainingFile()).setPackageName(packageName);
                    }
                }else {
                    stringBuffer.append(psiClass.getName()).append(",");
                }
                switch (itemSuffix) {
                    case MVP_CONTRACT:
                        if (psiClass.findInnerClassByName(className+MVP_VIEW, true) == null) {
                            classByName = elementFactory.createInterface(className+MVP_VIEW);
                            psiClass.add(classByName);
                        }
                        if (psiClass.findInnerClassByName(className+MVP_PRESENTER, true) == null) {
                            classByName = elementFactory.createInterface(className+MVP_PRESENTER);
                            psiClass.add(classByName);
                        }
                        if (psiClass.findInnerClassByName(className+MVP_MODULE, true) == null) {
                            classByName = elementFactory.createInterface(className+MVP_MODULE);
                            psiClass.add(classByName);
                        }
                        break;
                    case MVP_PRESENTERIMPL:
                        classByName = findPsiClassFromDirectory(directory,className+MVP_CONTRACT,packageName)
                                .findInnerClassByName(className + MVP_PRESENTER, true);
                        if (null != classByName && isImplements(psiClass,classByName)) {
                            QLog.v(TAG,"isImplements:"+isImplements(psiClass,classByName));
                            classByName = findPsiClassFromDirectory(directory, className + MVP_PRESENTER, packageName);
                            ((PsiJavaFile) classByName.getContainingFile()).getImportList().add(elementFactory.createImportStatement(classByName));
                            psiClass.getImplementsList().add(elementFactory.createClassReferenceElement(
                                    classByName));
                        }
                        break;
                    case MVP_FRAGMENT:
                        classByName = findPsiClassFromDirectory(directory,className+MVP_CONTRACT,packageName)
                                .findInnerClassByName(className + MVP_VIEW, true);
                        if (null != classByName && isImplements(psiClass,classByName)) {
                            QLog.v(TAG,"isImplements:"+isImplements(psiClass,classByName));
                            classByName = findPsiClassFromDirectory(directory, className + MVP_VIEW, packageName);
                            ((PsiJavaFile) classByName.getContainingFile()).getImportList().add(elementFactory.createImportStatement(classByName));
                            psiClass.getImplementsList().add(elementFactory.createClassReferenceElement(
                                    classByName));
                        }
                        break;
                    case MVP_MODULEIMPL:
                        classByName = findPsiClassFromDirectory(directory,className+MVP_CONTRACT,packageName)
                                .findInnerClassByName(className + MVP_MODULE, true);
                        if (null != classByName && isImplements(psiClass,classByName)) {
                            QLog.v(TAG,"isImplements:"+isImplements(psiClass,classByName));
                            classByName = findPsiClassFromDirectory(directory, className + MVP_MODULE, packageName);
                            ((PsiJavaFile) classByName.getContainingFile()).getImportList().add(elementFactory.createImportStatement(classByName));
                            psiClass.getImplementsList().add(elementFactory.createClassReferenceElement(
                                    classByName));
                        }
                        break;
                    case MVP_ACTIVITY:
                        break;
                    default:
                        break;
                }
            }
            if (stringBuffer.length() > 0){
                simpleView.showErrorDialog(string.DUPLICATE_CLASS_NAME,stringBuffer.substring(0,stringBuffer.length()-1)+";");
            }
        }
    }
    public PsiClass findPsiClassFromDirectory(PsiDirectory directory,String className,String packageName){
        PsiClass[] classesByName = PsiShortNamesCache.getInstance(directory.getProject()).getClassesByName(className, directory.getResolveScope());
        for (PsiClass itemClass :
                classesByName) {
            if (TextUtils.equals(((PsiJavaFile) itemClass.getContainingFile()).getPackageName(),packageName)){
                return itemClass;
            }
        }
        return null;
    }
    //该类是否实现了另一个
    private boolean isImplements(PsiClass psiClass,PsiClass classByName){
        PsiJavaCodeReferenceElement[] referenceElements = psiClass.getImplementsList().getReferenceElements();
        for (int i = 0; i < referenceElements.length; i++) {
            if (TextUtils.equals(referenceElements[i].getReferenceName(),classByName.getName().replace(".java",""))){
                return false;
            }
        }
        return true;
    }
    public interface SimpleView{
        void showErrorDialog(String title,String content);
    }
}
