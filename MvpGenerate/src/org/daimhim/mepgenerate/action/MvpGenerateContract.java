package org.daimhim.mepgenerate.action;

import org.daimhim.mepgenerate.mvp.IPresenter;
import org.daimhim.mepgenerate.mvp.IView;
import org.jetbrains.annotations.Nls;

public interface MvpGenerateContract {
    interface View extends IView {
        String showInputDialog(String message,String title);

        void showErrorDialog(String message,String title);

        void showStatusNotice(String message);
    }

    interface Presenter extends IPresenter {

    }
}
