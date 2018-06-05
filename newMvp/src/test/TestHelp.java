package test;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import general.BaseMVPHelp;
import toolbox.QLog;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static general.GlobalVariable.MVP_PRESENTERIMPL;

/**
 * Created by Daimhim on 2017/8/31.
 */
public class TestHelp extends BaseMVPHelp<TestAction>{
    Project mProject;
    PsiDirectory mPsiDirectory;
    PsiElementFactory mElementFactory;
    @Override
    public void onPrepare() {
        super.onPrepare();
        QLog.v(TAG,"pMVPBaseActivity.getName():"+pMVPBaseActivity.getName());
        QLog.v(TAG,"pMVPBaseContract.getName():"+pMVPBaseContract.getName());
        QLog.v(TAG,"pMVPBaseFragment.getName():"+pMVPBaseFragment.getName());
        QLog.v(TAG,"pMVPBasePresenter.getName():"+pMVPBasePresenter.getName());
        mProject = pAnActionEvent.getProject();
        VirtualFile data = pAnActionEvent.getData(VIRTUAL_FILE);
        mPsiDirectory = PsiDirectoryFactory.getInstance(mProject).createDirectory(data);

    }

    @Override
    public void onBackstage() {
        super.onBackstage();
        mElementFactory = JavaPsiFacade.getElementFactory(mPsiDirectory.getProject());
        PsiFile[] files = mPsiDirectory.getFiles();
        for (int i = 0; i < files.length; i++) {
            QLog.v(TAG,"files[i].getName():"+files[i].getName());
        }
        setpClassName(TAG);
        PsiClass mMVPPresenter = JavaDirectoryService.getInstance().createClass(mPsiDirectory, pClassName + MVP_PRESENTERIMPL);
        mMVPPresenter.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰

        PsiMethod method = mElementFactory.createMethod("aksjhdk",mElementFactory.createType(pMVPBaseFragment), mMVPPresenter);
        mMVPPresenter.add(method);
//        method.getReturnTypeElement().add(absMethod.getReturnTypeElement());

    }
}
