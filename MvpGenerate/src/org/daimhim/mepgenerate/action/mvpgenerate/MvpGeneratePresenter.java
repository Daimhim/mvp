package org.daimhim.mepgenerate.action.mvpgenerate;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.impl.ModuleManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.help.VirtualFileHelp;

import java.io.IOException;
import java.util.ArrayList;

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

    PsiDirectory mMvpPathfile;

    Project mProject;
    VirtualFile mVirtualFile;

    private MvpGenerateContract.View mView;
    private String mDefClassName;

    public void startView(MvpGenerateContract.View view){
        mView = view;
    }
    public void setTagParameter(Project project, VirtualFile virtualFile, PsiClass tagPsiClass) {
        mProject = project;
        mVirtualFile = virtualFile;
        mVImpl = tagPsiClass;
        mMvpPathfile = PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile).getParentDirectory();
    }
    public String inputMvpName(){
        //找出基类 V 和 P
        ArrayList<VirtualFile> tagVirtualFile = VirtualFileHelp.findTagVirtualFile(mVirtualFile,
                GlobalVariables.IVIEW + GlobalVariables.JAVA,
                GlobalVariables.IPRESENTER + GlobalVariables.JAVA,
                GlobalVariables.BVIEW + GlobalVariables.JAVA,
                GlobalVariables.BPRESENTER + GlobalVariables.JAVA);
        if (tagVirtualFile.isEmpty()){
            Module[] modules = ModuleManagerImpl.getInstanceImpl(mProject).getModules();
            for (int i = 0; i < modules.length; i++) {
                System.out.println(modules[i].getName());
            }
        }
        for (int i = 0; i < tagVirtualFile.size(); i++) {
            VirtualFile file = tagVirtualFile.get(i);
            if ((GlobalVariables.BVIEW + GlobalVariables.JAVA).equals(file.getName())) {
                mIView = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
            if ((GlobalVariables.BPRESENTER + GlobalVariables.JAVA).equals(file.getName())) {
                mIPresenter = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
            if ((GlobalVariables.IVIEW + GlobalVariables.JAVA).equals(file.getName())) {
                mIView = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
            if ((GlobalVariables.IPRESENTER + GlobalVariables.JAVA).equals(file.getName())) {
                mIPresenter = PsiTreeUtil.findChildOfAnyType(PsiManager.getInstance(mProject).findFile(file), PsiClass.class);
            }
        }
        mDefClassName = getViewPsiClassName(mVImpl);
        ArrayList<PsiField> psiFields = checkPastPresenter(mVImpl, mIPresenter);
        while (!isClassNameContains(psiFields, mDefClassName)) {
            mDefClassName = mView.showInputDialog("默认类名错误", "错误：默认类名已被占用");
        }
        return mDefClassName;
    }
    @Override
    public void run() {
        PsiElementFactory elementFactory = JavaPsiFacade
                .getElementFactory(mProject);
        if (!mDefClassName.equals(getViewPsiClassName(mVImpl))){
            try {
                mVirtualFile = mVirtualFile.getParent().createChildDirectory(this,mDefClassName);
                mMvpPathfile = PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //获取包名
            String packageName = VirtualFileHelp.getPackageName(mVirtualFile);
            //创建合约
            mView.showStatusNotice("正在创建合约...");
            mC = JavaDirectoryService.getInstance().createInterface(getDirectory(),
                    mDefClassName + GlobalVariables.CONTRACT);
            mC.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            ((PsiJavaFile) mC.getContainingFile()).setPackageName(packageName);


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
                    mDefClassName + GlobalVariables.BASE_PRESENTER);
            mPImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mP));
            //设置修饰属性
            mPImpl.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            PsiJavaFile containingFile = (PsiJavaFile) mPImpl.getContainingFile();
            containingFile.setPackageName(packageName);
            containingFile.getImportList().add(elementFactory.createImportStatement(mP));
            //V层实现合同
            mVImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mV));
            //增加合同变量到双方
            //添加P变量到V
            mView.showStatusNotice("正在增加合同变量到双方...");
            mVImpl.add(elementFactory.createField("m" + mDefClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mP)));
            if (!mC.getParent().getParent().equals(mVImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mVImpl.getParent().getParent())) {
//                containingFile = (PsiJavaFile) mVImpl.getContainingFile();
//                containingFile.getImportList().add(elementFactory.createImportStatement(mP));
                System.out.println(mC.getParent() + " 123   "+ mVImpl.getParent());
            }
            //添加V变量到P
            mPImpl.add(elementFactory.createField("m" + mDefClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mV)));
            if (!mC.getParent().getParent().equals(mPImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mPImpl.getParent().getParent())) {
//                containingFile = (PsiJavaFile) mPImpl.getContainingFile();
//                containingFile.getImportList().add(elementFactory.createImportStatement(mV));
                System.out.println(mC.getParent() + "  456  "+ mPImpl.getParent());
            }

        } catch (IncorrectOperationException e) {
            mView.showErrorDialog("创建合约失败，为什么失败？我哪知道啊！", "错误：创建合约失败");
        }
    }

    private boolean isClassNameContains(ArrayList<PsiField> psiFields,String className){
        if (null==className || "".equals(className)){
            return true;
        }
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
        return mMvpPathfile;
    }

    private String getViewPsiClassName(PsiClass psiClass) {
        String[] nameSuffix = {
                "Activity",
                "View",
                "Dialog",
                "Fragment",
                "PopupWindow",
                "Action"
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
