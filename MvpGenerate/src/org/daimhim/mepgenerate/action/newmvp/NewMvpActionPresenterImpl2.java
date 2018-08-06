package org.daimhim.mepgenerate.action.newmvp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.help.ClassHelp;
import org.daimhim.mepgenerate.help.QLog;
import org.daimhim.mepgenerate.help.VirtualFileHelp;
import org.daimhim.mepgenerate.model.NewMvpParameter;

import java.io.IOException;
import java.util.Objects;

public class NewMvpActionPresenterImpl2 implements NewMvpActionContract.Presenter, Runnable {
    String TAG = NewMvpActionPresenterImpl2.class.getSimpleName();
    private NewMvpActionContract.View mView;
    /**
     * 基类
     */
    private PsiClass mIView;
    private PsiClass mIPresenter;
    private PsiClass mBasePresenter;
    private PsiClass mBaseView;
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

    private NewMvpParameter mMvpParameter;
    private String mClassName;
    private String mClassSuffix;

    private PsiDirectory mMvpPathfile;
    private PsiDirectory mMvpPsiDirectoryParent;
    private VirtualFile mVirtualFile;
    private Project mProject;
    @Override
    public void run() {
        PsiElementFactory elementFactory = JavaPsiFacade
                .getElementFactory(mProject);
        try {
            mVirtualFile = mMvpPsiDirectoryParent.getVirtualFile().createChildDirectory(this, mClassName.toLowerCase());
            mMvpPathfile = PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile);
            //获取包名
            String packageName = VirtualFileHelp.getPackageName(mVirtualFile);
            //创建合约
            mView.showStatusNotice("正在创建合约...");
            mC = JavaDirectoryService.getInstance().createInterface(mMvpPathfile,
                    mClassName + GlobalVariables.CONTRACT);
            Objects.requireNonNull(mC.getModifierList()).setModifierProperty(PsiModifier.PUBLIC, true);
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
            mPImpl = JavaDirectoryService.getInstance().createClass(mMvpPathfile,
                    mClassName + GlobalVariables.BASE_PRESENTER);
            mPImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mP));
            if (null!=mBasePresenter) {
                mPImpl.getExtendsList().add(elementFactory.createClassReferenceElement(mBasePresenter));
            }
            //设置修饰属性
            mPImpl.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            PsiJavaFile containingFile = (PsiJavaFile) mPImpl.getContainingFile();
            containingFile.setPackageName(packageName);
            containingFile.getImportList().add(elementFactory.createImportStatement(mP));

            //创建ViewImpl
            mView.showStatusNotice("正在创建ViewImpl...");
            mVImpl = JavaDirectoryService.getInstance().createClass(mMvpPathfile,
                    mClassName + mClassSuffix);
            if (null!=mBaseView) {
                mVImpl.getExtendsList().add(elementFactory.createClassReferenceElement(mBaseView));
            }
            mVImpl.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            //V层实现合同
            mVImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mV));

            //增加合同变量到双方
            //添加P变量到V
            mView.showStatusNotice("正在增加合同变量到双方...");
            mVImpl.add(elementFactory.createField("m" + mClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mP)));
            if (!mC.getParent().getParent().equals(mVImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mVImpl.getParent().getParent())) {
            }
            //添加V变量到P
            mPImpl.add(elementFactory.createField("m" + mClassName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mV)));
            if (!mC.getParent().getParent().equals(mPImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mPImpl.getParent().getParent())) {
            }
            //实现方法
            if (mMvpParameter.isImplV()) {
                ClassHelp.abstractImplementation(elementFactory, mV, mVImpl);
                ClassHelp.abstractImplementation(elementFactory, mBaseView, mVImpl);
            }
            if (mMvpParameter.isImplP()) {
                ClassHelp.abstractImplementation(elementFactory, mP, mPImpl);
                ClassHelp.abstractImplementation(elementFactory, mBasePresenter, mPImpl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncorrectOperationException e) {
            mView.showErrorDialog("创建合约失败，为什么失败？我哪知道啊！", "错误：创建合约失败");
        }
    }

    @Override
    public String initMvpName() {
        return mClassName;
    }

    @Override
    public void setTagParameter(Project project, PsiDirectory psidirectory, NewMvpParameter mvpParameter) {
        mProject = project;
        mMvpPsiDirectoryParent = psidirectory;
        this.mMvpParameter = mvpParameter;
        mClassName = mvpParameter.getClassName();
        mClassSuffix = mvpParameter.getClassSuffix();
        QLog.v(TAG,mMvpParameter.toString());
        // 根据类的全限定名查询PsiClass，下面这个方法是查询Project域
        mIPresenter = JavaPsiFacade.getInstance(project).findClass(mvpParameter.getiPRESENTER(),
                GlobalSearchScope.projectScope(project));
        mIView = JavaPsiFacade.getInstance(project).findClass(mvpParameter.getiVIEW(),
                GlobalSearchScope.projectScope(project));


        mBasePresenter = JavaPsiFacade.getInstance(project).findClass(mvpParameter.getbPRESENTER(),
                GlobalSearchScope.projectScope(project));
        mBaseView = JavaPsiFacade.getInstance(project).findClass(mvpParameter.getbVIEW(),
                GlobalSearchScope.projectScope(project));
    }

    @Override
    public void startView(NewMvpActionContract.View view) {
        mView = view;
    }

}
