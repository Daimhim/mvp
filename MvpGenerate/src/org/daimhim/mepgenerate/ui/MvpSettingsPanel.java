package org.daimhim.mepgenerate.ui;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactoryImpl;
import com.intellij.openapi.project.Project;

import java.awt.*;
import java.util.*;

import com.intellij.psi.PsiClass;
import com.intellij.util.ArrayUtil;
import org.codehaus.groovy.util.ListHashMap;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.model.MvpGenerateModel;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class MvpSettingsPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton lb_delete;
    private JPanel lb_add;
    private JSplitPane jsp_Conten;
    private JList<String> list1;
    private JList<String> list2;
    private JButton bt_add;
    private MvpGenerateModel mvpGenerateModel;
    Map<String, Vector<String>> listMap = new ListHashMap<>();
    AbstractListModelHelp listModelHelp;
    Project mProject;

    public MvpSettingsPanel(Project project) {
        mProject = project;
        mvpGenerateModel = new MvpGenerateModel(mProject);
        setContentPane(contentPane);
        initData();
        initView();
    }

    private void initView() {
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Mvp Configuration");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Iterator<Map.Entry<String, Vector<String>>> iterator = listMap.entrySet().iterator();
                Map.Entry<String, Vector<String>> next = null;
                while (iterator.hasNext()) {
                    next = iterator.next();
                    mvpGenerateModel.setLocalConfiguration(next.getKey(), ArrayUtil.toStringArray(next.getValue()));
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add your code here if necessary
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // add your code here if necessary
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
                                               public void actionPerformed(ActionEvent e) {
                                                   // add your code here if necessary
                                                   dispose();
                                               }
                                           },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        lb_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!list2.isSelectionEmpty()) {
                    listMap.get(list1.getSelectedValue()).remove(list2.getSelectedValue());
                    list2.updateUI();
                }
            }
        });
        bt_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeClassChooser allProjectScopeChooser = TreeClassChooserFactoryImpl.getInstance(mProject)
                        .createAllProjectScopeChooser(list1.getSelectedValue());
                allProjectScopeChooser.showDialog();
                PsiClass selected = allProjectScopeChooser.getSelected();
                if (null != selected && !listMap.get(list1.getSelectedValue()).contains(selected.getQualifiedName())) {
                    listMap.get(list1.getSelectedValue()).add(selected.getQualifiedName());
                    list2.updateUI();
                }
            }
        });
        list1.addListSelectionListener(e -> {
            listModelHelp.setKey(list1.getSelectedValue());
            list2.updateUI();
        });
        list1.setSelectedIndex(0);
    }


    private void initData() {
        listMap.put(GlobalVariables.IVIEW, mvpGenerateModel.getLocalConfiguration(GlobalVariables.IVIEW));
        listMap.put(GlobalVariables.IPRESENTER, mvpGenerateModel.getLocalConfiguration(GlobalVariables.IPRESENTER));
        listMap.put(GlobalVariables.BPRESENTER, mvpGenerateModel.getLocalConfiguration(GlobalVariables.BPRESENTER));
        listMap.put(GlobalVariables.BVIEW, mvpGenerateModel.getLocalConfiguration(GlobalVariables.BVIEW));
        Vector<String> stringVector = new Vector<>(listMap.keySet());
        list1.setListData(stringVector);
        listModelHelp = new AbstractListModelHelp(listMap);
        list2.setModel(listModelHelp);
    }

    public void showMvpSettingsPanel() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        MvpSettingsPanel dialog = new MvpSettingsPanel(null);
        dialog.setTitle("Mvp配置");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    class AbstractListModelHelp extends AbstractListModel<String> {
        private Map<String, Vector<String>> vectorMap;
        String key;

        public AbstractListModelHelp(Map<String, Vector<String>> vectorMap) {
            this.vectorMap = vectorMap;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public int getSize() {
            return key == null || "".equals(key) ? 0 : vectorMap.get(key).size();
        }

        @Override
        public String getElementAt(int index) {
            return vectorMap.get(key).get(index);
        }
    }
}
