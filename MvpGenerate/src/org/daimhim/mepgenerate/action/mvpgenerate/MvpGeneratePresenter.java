package org.daimhim.mepgenerate.action.mvpgenerate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.help.VirtualFileHelp;
import org.daimhim.mepgenerate.model.MvpGenerateModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MvpGeneratePresenter implements Runnable {
    /**
     * 基类
     */
    private PsiClass mIView;
    private PsiClass mIPresenter;
    private PsiClass mBasePresenter;
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
    private MvpGenerateModel mModel;
    private String mDefClassName;

    public void startView(MvpGenerateContract.View view){
        mView = view;
    }
    public void setTagParameter(Project project, VirtualFile virtualFile, PsiClass tagPsiClass) {
        mProject = project;
        mVirtualFile = virtualFile;
        mVImpl = tagPsiClass;
        mMvpPathfile = PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile).getParentDirectory();
        mModel = new MvpGenerateModel(mProject);
    }
    public String inputMvpName(){
        mDefClassName = getViewPsiClassName(mVImpl);
        while (!isClassNameContains(mVImpl.getAllFields(), mDefClassName)) {
            mDefClassName = mView.showInputDialog("默认类名错误", "错误：默认类名已被占用");
        }
        if (mDefClassName == null || "".equals(mDefClassName)){
            return null;
        }
        List<String> presenters = mModel.getPresenter();
        List<String> views = mModel.getView();
        List<String> basePresenters = mModel.getBasePresenter();
        List<String> baseViews = mModel.getBaseView();
        ArrayList<String> objects = new ArrayList<>();
        objects.addAll(presenters);
        objects.addAll(views);
        objects.addAll(basePresenters);
        objects.addAll(baseViews);
        String[] findTag = objects.toArray(new String[presenters.size()]);

        //找出基类 V 和 P
        ArrayList<VirtualFile> tagVirtualFile = VirtualFileHelp.findTagVirtualFile(mVirtualFile,
                findTag);

//        if (tagVirtualFile.isEmpty()){
//            Module[] modules = ModuleManagerImpl.getInstanceImpl(mProject).getModules();
//            for (int i = 0; i < modules.length; i++) {
//                System.out.println(modules[i].getName());
//            }
//        }
        VirtualFile tagFile = null;
        List<VirtualFile> presenter = new ArrayList<>();
        List<VirtualFile> view = new ArrayList<>();
        List<VirtualFile> basePresenter = new ArrayList<>();
        for (int i = 0; i < tagVirtualFile.size(); i++) {
            tagFile = tagVirtualFile.get(i);
            if (presenters.contains(tagFile.getName())){
                //CPresenter
                presenter.add(tagFile);
            }else if (views.contains(tagFile.getName())){
                //CView
                view.add(tagFile);
            }else if (basePresenters.contains(tagFile.getName())){
                //BasePresenter
                basePresenter.add(tagFile);
            }
        }
        mIPresenter = chooseOneMore(presenter,GlobalVariables.IPRESENTER);
        mIView = chooseOneMore(view,GlobalVariables.IVIEW);
        mBasePresenter = chooseOneMore(basePresenter,GlobalVariables.BPRESENTER);
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
            if (null!=mIView) {
                mV.getExtendsList().add(elementFactory.createClassReferenceElement(mIView));
            }
            mC.add(mV);
            mV = mC.findInnerClassByName(mV.getName(),false);


            //创建Presenter并实现IPresenter接口
            mView.showStatusNotice("正在定义Presenter接口...");
            mP = elementFactory.createInterface(GlobalVariables.PRESENTER);
            if (null!=mIPresenter) {
                mP.getExtendsList().add(elementFactory.createClassReferenceElement(mIPresenter));
            }
            mC.add(mP);
            mP = mC.findInnerClassByName(mP.getName(),false);

            //创建PresenterImpl
            mView.showStatusNotice("正在创建PresenterImpl...");
            mPImpl = JavaDirectoryService.getInstance().createClass(getDirectory(),
                    mDefClassName + GlobalVariables.BASE_PRESENTER);
            mPImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mP));
            if (null!=mBasePresenter) {
                mPImpl.getExtendsList().add(elementFactory.createClassReferenceElement(mBasePresenter));
            }
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
//                System.out.println(mC.getParent() + " 123   "+ mVImpl.getParent());
            }
            //添加V变量到P
            mPImpl.add(elementFactory.createField("m" + mDefClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mV)));
            if (!mC.getParent().getParent().equals(mPImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mPImpl.getParent().getParent())) {
//                containingFile = (PsiJavaFile) mPImpl.getContainingFile();
//                containingFile.getImportList().add(elementFactory.createImportStatement(mV));
//                System.out.println(mC.getParent() + "  456  "+ mPImpl.getParent());
            }
        } catch (IncorrectOperationException e) {
            mView.showErrorDialog("创建合约失败，为什么失败？我哪知道啊！", "错误：创建合约失败");
        }
    }

    /**
     * 类中是否包含指定类变量
     * @param psiFields 成员变量
     * @param className 目标类
     * @return is
     */
    private boolean isClassNameContains(PsiField[] psiFields,String className){
        if (null==className || "".equals(className)){
            return true;
        }
        for (int i = 0; i < psiFields.length; i++) {
            if (psiFields[i].getName().contains(className)) {
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
        String[] nameSuffix = mModel.getViewNameSuffix();
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

    /**
     * 合并byte数组
     */
    public <T> T[]  unitByteArray(T[] byte1,T[] byte2){
        Object[] unitByte = new Object[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return (T[]) unitByte;
    }

    private PsiClass chooseOneMore(List<VirtualFile> list,String title){
        VirtualFile tagFile = null;
        if (list.size() > 0){
            tagFile = mView.getUserSelectClass(list,title);
        }
        if (null!=tagFile) {
            return PsiTreeUtil.findChildOfAnyType(
                    PsiManager.getInstance(mProject).findFile(tagFile), PsiClass.class);
        }
        return null;
    }
}
