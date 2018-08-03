package org.daimhim.mepgenerate.ui;

import com.intellij.openapi.project.Project;
import org.daimhim.mepgenerate.GlobalVariables;
import org.daimhim.mepgenerate.model.MvpGenerateModel;
import org.daimhim.mepgenerate.model.NewMvpParameter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;

public class NewMvpPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField mvpNameInput;

    private JRadioButton activityRadioButton;
    private JRadioButton fragmentRadioButton;
    private JRadioButton dialogRadioButton;
    private JRadioButton otherRadioButton;

    private JTextField otherInput;
    private JLabel otherTag;

    private JCheckBox IViewCheckBox;
    private JComboBox IViewComboBox;

    private JCheckBox IPresenterCheckBox;
    private JComboBox IPresenterComboBox;

    private JCheckBox IModelCheckBox;
    private JComboBox IModelComboBox;

    private JCheckBox baseViewCheckBox;
    private JComboBox baseViewComboBox;

    private JCheckBox basePresenterCheckBox;
    private JComboBox basePresenterComboBox;

    private JCheckBox baseModelCheckBox;
    private JComboBox baseModelComboBox;


    private JLabel mvpNameTip;
    private JLabel otherTip;

    private MvpGenerateModel mvpGenerateModel;
    private NewMvpParameter mvpParameter;
    private ButtonGroup buttonGroup;

    public NewMvpPanel(Project project) {
        mvpGenerateModel = new MvpGenerateModel(project);
        initView();
        initData();
    }

    private void initDataTest() {
        Vector<String> vector = new Vector<>();
        for (int i = 0; i < 10; i++) {
            vector.add(String.valueOf(i));
        }
        IViewComboBox.setModel(new DefaultComboBoxModel<String>(vector));
        IPresenterComboBox.setModel(new DefaultComboBoxModel<String>(vector));
        IModelComboBox.setModel(new DefaultComboBoxModel<String>(vector));
        baseViewComboBox.setModel(new DefaultComboBoxModel<String>(vector));
        basePresenterComboBox.setModel(new DefaultComboBoxModel<String>(vector));
        baseModelComboBox.setModel(new DefaultComboBoxModel<String>(vector));

    }

    private void initData() {
        IViewComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.IVIEW)));
        IPresenterComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.IPRESENTER)));
        IModelComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.IMODEL)));
        baseViewComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.BVIEW)));
        basePresenterComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.BPRESENTER)));
        baseModelComboBox.setModel(new DefaultComboBoxModel<String>(mvpGenerateModel.getLocalConfiguration(GlobalVariables.BMODEL)));
    }

    private void initView() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("New Mvp");

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
        buttonGroup = new ButtonGroup();
        buttonGroup.add(activityRadioButton);
        buttonGroup.add(fragmentRadioButton);
        buttonGroup.add(dialogRadioButton);
        buttonGroup.add(otherRadioButton);
        otherRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (otherRadioButton.isSelected()) {
                    otherTag.setVisible(true);
                    otherInput.setVisible(true);
                } else {
                    otherTag.setVisible(false);
                    otherInput.setVisible(false);
                }
            }
        });
        IViewCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                IViewComboBox.setEnabled(IViewCheckBox.isSelected());
            }
        });
        IPresenterCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                IPresenterComboBox.setEnabled(IPresenterCheckBox.isSelected());
            }
        });
        IModelCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                IModelComboBox.setEnabled(IModelCheckBox.isSelected());
            }
        });
        baseViewCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                baseViewComboBox.setEnabled(baseViewCheckBox.isSelected());
            }
        });
        basePresenterCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                basePresenterComboBox.setEnabled(basePresenterCheckBox.isSelected());
            }
        });
        baseModelCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                baseModelComboBox.setEnabled(baseModelCheckBox.isSelected());
            }
        });

        mvpNameInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                mvpNameTip.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                mvpNameTip.setVisible(false);
            }
        });

        otherInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                otherTip.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                otherTip.setVisible(false);
            }
        });
    }

    public void showNewMvpPanel() {
        pack();
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        if (mvpNameInput.getText().isEmpty()) {
            mvpNameTip.setText("name not empty");
            mvpNameTip.setVisible(true);
            return;
        }
        if (otherRadioButton.isSelected() && otherInput.getText().isEmpty()) {
            otherTip.setText("other not empty");
            otherTip.setVisible(true);
            return;
        }
        mvpParameter = new NewMvpParameter();
        mvpParameter.setIView(IViewCheckBox.isSelected());
        mvpParameter.setiVIEW((String) IViewComboBox.getSelectedItem());
        mvpParameter.setIPresenter(IPresenterCheckBox.isSelected());
        mvpParameter.setiPRESENTER((String) IPresenterComboBox.getSelectedItem());
        mvpParameter.setIModel(IModelCheckBox.isSelected());
        mvpParameter.setiMODEL((String) IModelComboBox.getSelectedItem());


        mvpParameter.setBaseView(baseViewCheckBox.isSelected());
        mvpParameter.setbVIEW((String) baseViewComboBox.getSelectedItem());
        mvpParameter.setBasePresenter(basePresenterCheckBox.isSelected());
        mvpParameter.setbPRESENTER((String) basePresenterComboBox.getSelectedItem());
        mvpParameter.setBaseModel(baseModelCheckBox.isSelected());
        mvpParameter.setbMODEL((String) baseModelComboBox.getSelectedItem());
        String suffix = "";
        Enumeration<AbstractButton> elements = buttonGroup.getElements();
        AbstractButton abstractButton = null;
        while (elements.hasMoreElements()){
            abstractButton = elements.nextElement();
            if (abstractButton.isSelected()){
                suffix = abstractButton.getText().trim();
                break;
            }
        }
        if ("other".equals(suffix)) {
            suffix = otherInput.getText().trim();
        }
        mvpParameter.setClassSuffix(suffix);
        mvpParameter.setClassName(mvpNameInput.getText().trim());
        System.out.println(mvpParameter.toString());
        dispose();
    }

    public NewMvpParameter getMvpParameter() {
        return mvpParameter;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        NewMvpPanel dialog = new NewMvpPanel(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
