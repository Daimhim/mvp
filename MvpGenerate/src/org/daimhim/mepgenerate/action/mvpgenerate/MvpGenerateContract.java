package org.daimhim.mepgenerate.action.mvpgenerate;

import org.daimhim.mepgenerate.mvp.IPresenter;
import org.daimhim.mepgenerate.mvp.IView;
import org.jetbrains.annotations.Nls;

import java.util.List;

public interface MvpGenerateContract {
    interface View extends IView {
        String showInputDialog(String message,String title);

        void showErrorDialog(String message,String title);

        void showStatusNotice(String message);

        String getUserSelectClass(List<String> list);
    }

    interface Presenter extends IPresenter {

    }
}
