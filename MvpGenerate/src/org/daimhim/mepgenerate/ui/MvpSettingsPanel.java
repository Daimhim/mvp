package org.daimhim.mepgenerate.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class MvpSettingsPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lb_delete;
    private JPanel lb_add;
    private JSplitPane jsp_Conten;
    private JList list1;
    private JList list2;

    public MvpSettingsPanel() {
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
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
        list2.setFixedCellWidth(200);
        list2.setListData(title);
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
