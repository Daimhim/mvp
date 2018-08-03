package org.daimhim.mepgenerate.action.newmvp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import org.daimhim.mepgenerate.action.mvpgenerate.MvpGenerateContract;
import org.daimhim.mepgenerate.model.NewMvpParameter;
import org.daimhim.mepgenerate.mvp.IPresenter;
import org.daimhim.mepgenerate.mvp.IView;

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

public interface NewMvpActionContract {
    public interface View extends IView {
        String showInputDialog(String message,String title);

        void showErrorDialog(String message,String title);

        void showStatusNotice(String message);

        VirtualFile getUserSelectClass(List<VirtualFile> list, String title);
    }

    public interface Presenter extends IPresenter {
        String initMvpName();
        void setTagParameter(Project project,PsiDirectory psidirectory,NewMvpParameter mvpParameter);
        void startView(NewMvpActionContract.View view);
    }
}
