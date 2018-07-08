package org.daimhim.mepgenerate.action.mvpgenerate;

import com.intellij.openapi.vfs.VirtualFile;
import org.daimhim.mepgenerate.mvp.IPresenter;
import org.daimhim.mepgenerate.mvp.IView;
import org.jetbrains.annotations.Nls;

import java.util.List;

public interface MvpGenerateContract {
    interface View extends IView {
        String showInputDialog(String message,String title);

        void showErrorDialog(String message,String title);

        void showStatusNotice(String message);

        VirtualFile getUserSelectClass(List<VirtualFile> list,String title);
    }

    interface Presenter extends IPresenter {

    }
}
