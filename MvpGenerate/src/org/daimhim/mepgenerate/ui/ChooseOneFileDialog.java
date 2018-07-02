package org.daimhim.mepgenerate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChooseOneFileDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList mLists;
    private JButton buttonNone;
    private String mStatus;

    public ChooseOneFileDialog(List<String> list) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        mLists.setListData(list.toArray());
        mLists.setSelectedIndex(0);
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
        buttonNone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNone();
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
    }

    @Override
    public void setVisible(boolean b) {
        setAlwaysOnTop(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        super.setVisible(b);
    }

    public String showDialog(){
        setFocusable(true);
        pack();
        setVisible(true);
        if ("1".equals(mStatus)){
            return (String) mLists.getSelectedValue();
        }else if ("2".equals(mStatus)){
            return "";
        }else {
            return null;
        }
    }

    private void onNone(){
        mStatus = "2";
        dispose();
    }
    private void onOK() {
        // add your code here
        mStatus = "1";
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        mStatus = "3";
        dispose();
    }

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add(String.valueOf(i));
        }
        ChooseOneFileDialog dialog = new ChooseOneFileDialog(stringList);
        dialog.setTitle("选择你爸爸");
        String s = dialog.showDialog();
        System.out.println(s);
        System.exit(0);
    }
}
