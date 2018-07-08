package org.daimhim.mepgenerate.action.newmvp;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.action.mvpgenerate.MvpGenerateModel;
import org.daimhim.mepgenerate.help.VirtualFileHelp;

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

public class NewMvpActionPresenterImpl implements NewMvpActionContract.Presenter,Runnable {
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

    Project mProject;
    private NewMvpActionContract.View mView;
    private String mDefName;
    private MvpGenerateModel mModel;
    private String mRawData;
    @Override
    public String initMvpName() {
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
        List<VirtualFile> baseview = new ArrayList<>();
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
            } else if (baseViews.contains(tagFile.getName())) {
                baseview.add(tagFile);
            }
        }
        mIPresenter = chooseOneMore(presenter,GlobalVariables.IPRESENTER);
        mIView = chooseOneMore(view,GlobalVariables.IVIEW);
        mBasePresenter = chooseOneMore(basePresenter,GlobalVariables.BPRESENTER);
        mBaseView = chooseOneMore(basePresenter,GlobalVariables.BVIEW);
        mRawData = mView.showInputDialog("请输入新建mvp的名称","请输入名称");
        while (mMvpPsiDirectoryParent.findFile(getViewPsiClassName(mRawData)) == null){
            mRawData = mView.showInputDialog("包名重复请重新输入","请输入名称");
        }
        mDefName = getViewPsiClassName(mRawData);
        return mRawData;
    }

    @Override
    public void run() {
        PsiElementFactory elementFactory = JavaPsiFacade
                .getElementFactory(mProject);
    }

    private boolean isItRedundant(){
        return false;
    }
    @Override
    public void setTagParameter(Project project,PsiDirectory psidirectory) {
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
    private PsiClass chooseOneMore(List<VirtualFile> list,String title){
        VirtualFile tagFile = null;
        if (list.size() > 1){
            tagFile = mView.getUserSelectClass(list,title);
        }else if (list.size() == 1){
            tagFile = list.get(0);
        }
        if (null!=tagFile) {
            return PsiTreeUtil.findChildOfAnyType(
                    PsiManager.getInstance(mProject).findFile(tagFile), PsiClass.class);
        }
        return null;
    }
}
