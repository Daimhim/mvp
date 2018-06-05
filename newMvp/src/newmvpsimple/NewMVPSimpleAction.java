package newmvpsimple;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import general.BaseMVPHelp;
import resources.string;
import toolbox.Log;
import view.MVPSimpleView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static general.BaseMVPHelp.classNameCorrection;

/**
 * Created by Daimhim on 2017/6/19.
 */
public class NewMVPSimpleAction extends AnAction implements MVPSimpleView.OnClickListener, NewMVPSimpleHelp.SimpleView {
    protected String TAG = getClass().getSimpleName();
    NewMVPSimpleHelp newMVPSimpleHelp = null;

    @Override
    public void update(AnActionEvent event) {
        super.update(event);
        //获取Project根目录
        VirtualFile data = event.getData(VIRTUAL_FILE);
        Presentation presentation = event.getPresentation();
        if (BaseMVPHelp.isNewMVPSimple(data.getPath())) {
            presentation.setEnabledAndVisible(true);
        } else {
            presentation.setEnabledAndVisible(false);
        }

    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        newMVPSimpleHelp = new NewMVPSimpleHelp(this);
        Project project = anActionEvent.getProject();
        String className = classNameCorrection(Messages.showInputDialog(project, string.INPUT_CLASS_NAME, string.PLUGIN_NAME, Messages.getQuestionIcon()));
        VirtualFile data = anActionEvent.getData(VIRTUAL_FILE);
        PsiDirectory directory = PsiDirectoryFactory.getInstance(project).createDirectory(data);
        newMVPSimpleHelp.createClass(project, directory, newMVPSimpleHelp.getPackageName(data), className);
    }


    private void showMVPSimpleView() {
        MVPSimpleView dialog = new MVPSimpleView();
        dialog.setOnClickListener(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        dialog.setLocation((screenSize.width - dialog.getWidth()) / 2, (screenSize.height - dialog.getHeight()) / 2);
        dialog.setVisible(true);
    }

    @Override
    public void onClick(JTextField textField, ActionEvent e) {
        String trim = textField.getText().toString().trim();
        Log.i("NewMVPSimpleAction", "-------" + trim);
    }


    @Override
    public void showErrorDialog(String title, String content) {
        Messages.showErrorDialog(content,title);
    }
}
