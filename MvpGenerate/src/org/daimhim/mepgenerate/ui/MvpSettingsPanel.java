package org.daimhim.mepgenerate.ui;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactoryImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.*;

import com.intellij.psi.PsiClass;
import org.codehaus.groovy.util.ListHashMap;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.model.MvpGenerateModel;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import java.awt.event.*;

public class MvpSettingsPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton lb_delete;
    private JPanel lb_add;
    private JSplitPane jsp_Conten;
    private JList<String> list1;
    private JList list2;
    private JButton bt_add;
    private MvpGenerateModel mvpGenerateModel;
    Map<String,List<String>> listMap =new ListHashMap<>();
    Project mProject;

    public MvpSettingsPanel(Project project) {
        mProject = project;
        mvpGenerateModel = new MvpGenerateModel(mProject);
        setContentPane(contentPane);
        contentPane.setSize(1500,1500);
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
        lb_delete.setSize(20,20);
        bt_add.setSize(20,20);
        list1.setSize(100,500);
        list2.setSize(200,500);
        jsp_Conten.setSize(500,500);
        lb_add.setSize(1000,1000);
        list1.setFixedCellWidth(200);
        list2.setFixedCellWidth(500);
//        initData();
//        initView();
    }

    private void initView() {
        list1.setSelectedIndex(0);
        bt_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeClassChooser allProjectScopeChooser = TreeClassChooserFactoryImpl.getInstance(mProject).createAllProjectScopeChooser(list1.getSelectedValue());
                allProjectScopeChooser.showDialog();
                PsiClass selected = allProjectScopeChooser.getSelected();
                System.out.println(selected.getQualifiedName());
                listMap.get(list1.getSelectedValue()).add(selected.getQualifiedName());
                list2.setListData(new Vector<String>(listMap.get(list1.getSelectedValue())));
            }
        });
    }



    private void initData() {
        listMap.put(GlobalVariables.IVIEW,mvpGenerateModel.getLocalConfiguration(GlobalVariables.IVIEW));
        listMap.put(GlobalVariables.IPRESENTER,mvpGenerateModel.getLocalConfiguration(GlobalVariables.IPRESENTER));
        listMap.put(GlobalVariables.BPRESENTER,mvpGenerateModel.getLocalConfiguration(GlobalVariables.BPRESENTER));
        listMap.put(GlobalVariables.BVIEW,mvpGenerateModel.getLocalConfiguration(GlobalVariables.BVIEW));
        Vector<String> stringVector = new Vector<>(listMap.keySet());
        list1.setListData(stringVector);
        list1.addListSelectionListener(e -> {
            List<String> stringList = listMap.get(list1.getSelectedValue());
            list2.setListData(new Vector<String>(stringList));
        });

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
        MvpSettingsPanel dialog = new MvpSettingsPanel(null);
        dialog.setTitle("Mvp配置");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
