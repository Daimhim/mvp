package org.daimhim.mepgenerate.action.newmvp;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.search.GlobalSearchScope;
import org.daimhim.mepgenerate.model.NewMvpParameter;

public class NewMvpActionPresenterImpl2 implements NewMvpActionContract.Presenter, Runnable {
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

    @Override
    public void run() {

    }

    @Override
    public String initMvpName() {
        return null;
    }

    @Override
    public void setTagParameter(Project project, PsiDirectory psidirectory, NewMvpParameter mvpParameter) {
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

    }

//    public static void main(String[] args) {
//        String s = "0.0001";
//        if(s.indexOf(".") > 0){
//            //正则表达
//            s = s.replaceAll("0+?$", "");
//            s = s.replaceAll("[.]$", "");
//        }
//        System.out.println(s);
//    }
}
