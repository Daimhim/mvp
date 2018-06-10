package org.daimhim.mepgenerate.action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiFileEx;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.impl.file.PsiFileImplUtil;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.IncorrectOperationException;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.help.VirtualFileHelp;

import java.util.ArrayList;
import java.util.Objects;

public class MvpGeneratePresenter implements Runnable {
    /**
     * 基类
     */
    private PsiClass mIView;
    private PsiClass mIPresenter;
    /**
     * 刚创建的类
     */
    private PsiClass mV;
    private PsiClass mP;
    private PsiClass mC;
    /**
     * V and P implements
     */
    private PsiClass mPImpl;
    private PsiClass mVImpl;

    Project mProject;
    VirtualFile mVirtualFile;
    PsiClass mTagPsiClass;

    private MvpGenerateContract.View mView;
    public void startView(MvpGenerateContract.View view){
        mView = view;
    }
    public void setTagParameter(Project project, VirtualFile virtualFile, PsiClass tagPsiClass) {
        mProject = project;
        mVirtualFile = virtualFile;
        mTagPsiClass = tagPsiClass;
    }

    public PsiClass createContract(String className, PsiDirectory directory, String packageName) {

        PsiClass psiClass = JavaDirectoryService.getInstance().createInterface(directory, className);
        //设置修饰属性
        Objects.requireNonNull(psiClass.getModifierList()).setModifierProperty(PsiModifier.PUBLIC, true);
        ((PsiJavaFile) psiClass.getContainingFile()).setPackageName(packageName);
        return psiClass;
    }

    public void registrationContract() {

    }

    public void followContract() {

    }

    @Override
    public void run() {
        PsiElementFactory elementFactory = JavaPsiFacade
                .getElementFactory(mProject);
        //找出基类 V 和 P
        ArrayList<VirtualFile> tagVirtualFile = VirtualFileHelp.findTagVirtualFile(mVirtualFile,
                GlobalVariables.IVIEW + GlobalVariables.JAVA,
                GlobalVariables.IPRESENTER + GlobalVariables.JAVA);
        for (int i = 0; i < tagVirtualFile.size(); i++) {
            VirtualFile file = tagVirtualFile.get(i);
            if ((GlobalVariables.IVIEW + GlobalVariables.JAVA).equals(file.getName())) {
                mIView = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
            if ((GlobalVariables.IPRESENTER + GlobalVariables.JAVA).equals(file.getName())) {
                mIPresenter = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
        }
        String defClassName = getViewPsiClassName(mTagPsiClass);
        ArrayList<PsiField> psiFields = checkPastPresenter(mTagPsiClass, mIPresenter);
        while (!isClassNameContains(psiFields,defClassName)) {
            defClassName = mView.showInputDialog("默认类名错误", "错误：默认类名已被占用");
        }
        try {

            //创建合约
            mView.showStatusNotice("正在创建合约...");
            mC = JavaDirectoryService.getInstance().createInterface(getDirectory(),
                    defClassName + GlobalVariables.CONTRACT);
            mC.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            ((PsiJavaFile) mC.getContainingFile()).setPackageName(VirtualFileHelp.getPackageName(mVirtualFile));


            //创建View病实现IView接口
            mView.showStatusNotice("正在定义View接口...");
            mV = elementFactory.createInterface(GlobalVariables.VIEW);
            mV.getExtendsList().add(elementFactory.createClassReferenceElement(mIView));
            mC.add(mV);
            mV = mC.findInnerClassByName(mV.getName(),false);


            //创建Presenter并实现IPresenter接口
            mView.showStatusNotice("正在定义Presenter接口...");
            mP = elementFactory.createInterface(GlobalVariables.PRESENTER);
            mP.getExtendsList().add(elementFactory.createClassReferenceElement(mIPresenter));
            mC.add(mP);
            mP = mC.findInnerClassByName(mP.getName(),false);

            //创建PresenterImpl
            mView.showStatusNotice("正在创建PresenterImpl...");
            mPImpl = JavaDirectoryService.getInstance().createClass(getDirectory(),
                    defClassName + GlobalVariables.BASE_PRESENTER);
            mPImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mP));
            //设置修饰属性
            mPImpl.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            PsiJavaFile containingFile = (PsiJavaFile) mPImpl.getContainingFile();
            containingFile.setPackageName(VirtualFileHelp.getPackageName(mVirtualFile));
            containingFile.getImportList().add(elementFactory.createImportStatement(mP));

            //增加合同变量到双方
            //添加P变量到V
            mView.showStatusNotice("正在增加合同变量到双方...");
            mTagPsiClass.add(elementFactory.createField("m" + defClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mP)));
            if (!mC.getParent().getParent().equals(mTagPsiClass.getParent().getParent())) {
                containingFile = (PsiJavaFile) mTagPsiClass.getContainingFile();
                containingFile.getImportList().add(elementFactory.createImportStatement(mP));
            }
            //添加V变量到P
            mPImpl.add(elementFactory.createField("m" + defClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mV)));
            if (!mC.getParent().getParent().equals(mPImpl.getParent().getParent())) {
                containingFile = (PsiJavaFile) mPImpl.getContainingFile();
                containingFile.getImportList().add(elementFactory.createImportStatement(mV));
            }

        } catch (IncorrectOperationException e) {
            Messages.showErrorDialog("创建合约失败，为什么失败？我哪知道啊！", "错误：创建合约失败");
        }
    }

    private boolean isClassNameContains(ArrayList<PsiField> psiFields,String className){
        for (int i = 0; i < psiFields.size(); i++) {
            if (psiFields.get(i).getName().contains(className)) {
                return false;
            }
        }
        return true;
    }

    private PsiPackage getPsiPackage(PsiClass psiClass){
        return JavaPsiFacade.getInstance(psiClass.getProject())
                .findPackage(((PsiJavaFile) psiClass.getContainingFile()).getPackageName());
    }
    /**
     * 获取当前View所在目录
     *
     * @return
     */
    private PsiDirectory getDirectory() {
        return PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile).getParentDirectory();
    }

    private String getViewPsiClassName(PsiClass psiClass) {
        String[] nameSuffix = {
                "Activity",
                "View",
                "Dialog",
                "Fragment",
                "PopupWindow"
        };
        String name = psiClass.getName();
        for (int i = 0; i < nameSuffix.length; i++) {
            if (null != name && name.endsWith(nameSuffix[i])) {
                return name.substring(0, name.length() - nameSuffix[i].length());
            }
        }
        return name;
    }

    /**
     * 检查当前类中是否存在P层
     *
     * @param psiClass 目标类
     * @param tag      目标类
     * @return 返回值
     */
    private ArrayList<PsiField> checkPastPresenter(PsiClass psiClass, PsiClass tag) {
        ArrayList<PsiField> psiFields = new ArrayList<>();
        PsiField[] fields = psiClass.getFields();
        PsiField field = null;
        PsiClassType type = JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(tag);
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            PsiType type1 = field.getType();
            if (type1.isConvertibleFrom(type)) {
                psiFields.add(field);
            }
        }
        return psiFields;
    }
}
