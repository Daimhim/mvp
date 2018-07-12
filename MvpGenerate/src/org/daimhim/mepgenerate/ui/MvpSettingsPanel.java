package org.daimhim.mepgenerate.ui;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.codehaus.groovy.util.ListHashMap;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.model.MvpGenerateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class MvpSettingsPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lb_delete;
    private JPanel lb_add;
    private JSplitPane jsp_Conten;
    private JList<String> list1;
    private JList list2;
    private MvpGenerateModel mvpGenerateModel;
    Map<String,List<VirtualFile>> listMap =new ListHashMap<>();
    public MvpSettingsPanel() {
        mvpGenerateModel = new MvpGenerateModel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        String[] title = new String[5];
        for (int i = 0; i < title.length; i++) {
            title[i] = String.valueOf(i);
        }
        list1.setFixedCellWidth(100);

        list1.setListData(title);
        list2.setFixedCellWidth(200);
        list2.setListData(title);
        initData();
    }

    private void initData() {
        List<VirtualFile> IVIEW = mvpGenerateModel.getLocalConfiguration(GlobalVariables.IVIEW);
        List<VirtualFile> IPRESENTER = mvpGenerateModel.getLocalConfiguration(GlobalVariables.IPRESENTER);
        List<VirtualFile> BPRESENTER = mvpGenerateModel.getLocalConfiguration(GlobalVariables.BPRESENTER);
        List<VirtualFile> BVIEW = mvpGenerateModel.getLocalConfiguration(GlobalVariables.BVIEW);

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    public void showMvpSettingsPanel(){

    }
    public static void main(String[] args) {
        MvpSettingsPanel dialog = new MvpSettingsPanel();
        dialog.setTitle("Mvp配置");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
