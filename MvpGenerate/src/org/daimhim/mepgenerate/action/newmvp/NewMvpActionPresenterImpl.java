package org.daimhim.mepgenerate.action.newmvp;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.action.mvpgenerate.MvpGenerateModel;
import org.daimhim.mepgenerate.help.VirtualFileHelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：org.daimhim.mepgenerate.action.newmvp
 * 项目版本：MvpGenerate
 * 创建时间：2018.06.25 14:08
 * 修改人：Daimhim
 * 修改时间：2018.06.25 14:08
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */

public class NewMvpActionPresenterImpl implements NewMvpActionContract.Presenter, Runnable {
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

    PsiDirectory mMvpPathfile;
    PsiDirectory mMvpPsiDirectoryParent;
    VirtualFile mVirtualFile;
    Project mProject;
    private NewMvpActionContract.View mView;
    private String mDefName;
    private String mDefNameSuffix = "";
    private MvpGenerateModel mModel;
    private String mRawData;

    @Override
    public String initMvpName() {
        mRawData = mView.showInputDialog("请输入新建mvp的名称", "请输入名称");
        while (mMvpPsiDirectoryParent.findFile(getViewPsiClassName(mRawData)) != null) {
            mRawData = mView.showInputDialog("包名重复请重新输入", "请输入名称");
            if (mRawData == null) {
                return null;
            }
        }
        mDefName = getViewPsiClassName(mRawData);
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
        ArrayList<VirtualFile> tagVirtualFile = VirtualFileHelp.findTagVirtualFile(mMvpPsiDirectoryParent.getVirtualFile(),
                findTag);
        VirtualFile tagFile = null;
        List<VirtualFile> presenter = new ArrayList<>();
        List<VirtualFile> view = new ArrayList<>();
        List<VirtualFile> basePresenter = new ArrayList<>();
        List<VirtualFile> baseView = new ArrayList<>();
        for (int i = 0; i < tagVirtualFile.size(); i++) {
            tagFile = tagVirtualFile.get(i);
            if (presenters.contains(tagFile.getName())) {
                //CPresenter
                presenter.add(tagFile);
            } else if (views.contains(tagFile.getName())) {
                //CView
                view.add(tagFile);
            } else if (basePresenters.contains(tagFile.getName())) {
                //BasePresenter
                basePresenter.add(tagFile);
            } else if (baseViews.contains(tagFile.getName())) {
                baseView.add(tagFile);
            }
        }
        mIPresenter = chooseOneMore(presenter, GlobalVariables.IPRESENTER);
        mIView = chooseOneMore(view, GlobalVariables.IVIEW);
        mBasePresenter = chooseOneMore(basePresenter, GlobalVariables.BPRESENTER);
        mBaseView = chooseOneMore(baseView, GlobalVariables.BVIEW);
        if (mBaseView != null) {
            mDefNameSuffix = mModel.getBaseViewSuffix(mBaseView.getName());
        }
        return mRawData;
    }

    @Override
    public void run() {
        PsiElementFactory elementFactory = JavaPsiFacade
                .getElementFactory(mProject);
        try {
            mVirtualFile = mMvpPsiDirectoryParent.getVirtualFile().createChildDirectory(this, mDefName.toLowerCase());
            mMvpPathfile = PsiDirectoryFactory.getInstance(mProject).createDirectory(mVirtualFile);
           //获取包名
            String packageName = VirtualFileHelp.getPackageName(mVirtualFile);
            //创建合约
            mView.showStatusNotice("正在创建合约...");
            mC = JavaDirectoryService.getInstance().createInterface(mMvpPathfile,
                    mDefName + GlobalVariables.CONTRACT);
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
            mPImpl = JavaDirectoryService.getInstance().createClass(mMvpPathfile,
                    mDefName + GlobalVariables.BASE_PRESENTER);
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
                    mDefName + mDefNameSuffix);
            if (null!=mBaseView) {
                mVImpl.getExtendsList().add(elementFactory.createClassReferenceElement(mBaseView));
            }
            mVImpl.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
            //V层实现合同
            mVImpl.getImplementsList().add(elementFactory.createClassReferenceElement(mV));
            //增加合同变量到双方
            //添加P变量到V
            mView.showStatusNotice("正在增加合同变量到双方...");
            mVImpl.add(elementFactory.createField("m" + mDefName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mP)));
            if (!mC.getParent().getParent().equals(mVImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mVImpl.getParent().getParent())) {
//                containingFile = (PsiJavaFile) mVImpl.getContainingFile();
//                containingFile.getImportList().add(elementFactory.createImportStatement(mP));
//                System.out.println(mC.getParent() + " 123   "+ mVImpl.getParent());
            }
            //添加V变量到P
            mPImpl.add(elementFactory.createField("m" + mDefName + GlobalVariables.PRESENTER,
                    elementFactory.createType(mV)));
            if (!mC.getParent().getParent().equals(mPImpl.getParent().getParent())
                    || mC.getParent().getParent().getParent().equals(mPImpl.getParent().getParent())) {
//                containingFile = (PsiJavaFile) mPImpl.getContainingFile();
//                containingFile.getImportList().add(elementFactory.createImportStatement(mV));
//                System.out.println(mC.getParent() + "  456  "+ mPImpl.getParent());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncorrectOperationException e) {
            mView.showErrorDialog("创建合约失败，为什么失败？我哪知道啊！", "错误：创建合约失败");
        }
    }

    private boolean isItRedundant() {
        return false;
    }

    @Override
    public void setTagParameter(Project project, PsiDirectory psidirectory) {
        mProject = project;
        mMvpPsiDirectoryParent = psidirectory;
    }

    @Override
    public void startView(NewMvpActionContract.View view) {
        this.mView = view;
        mModel = new MvpGenerateModel();
    }

    private String getViewPsiClassName(String name) {
        String[] nameSuffix = mModel.getViewNameSuffix();
        for (int i = 0; i < nameSuffix.length; i++) {
            if (null != name && name.endsWith(nameSuffix[i])) {
                return name.substring(0, name.length() - nameSuffix[i].length());
            }
        }
        return name;
    }

    private PsiClass chooseOneMore(List<VirtualFile> list, String title) {
        VirtualFile tagFile = null;
        if (list.size() > 1) {
            tagFile = mView.getUserSelectClass(list, title);
        } else if (list.size() == 1) {
            tagFile = list.get(0);
        }
        if (null != tagFile) {
            return PsiTreeUtil.findChildOfAnyType(
                    PsiManager.getInstance(mProject).findFile(tagFile), PsiClass.class);
        }
        return null;
    }
}
