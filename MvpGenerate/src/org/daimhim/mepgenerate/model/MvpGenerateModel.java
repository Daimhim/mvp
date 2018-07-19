package org.daimhim.mepgenerate.model;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import org.daimhim.mepgenerate.GlobalVariables;

import java.io.File;
import java.util.*;

public class MvpGenerateModel {
    private Project mProject;

    public MvpGenerateModel(Project mProject) {
        this.mProject = mProject;
    }

    public List<String> getView(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add(GlobalVariables.BVIEW + GlobalVariables.JAVA);
        strings.add(GlobalVariables.IVIEW + GlobalVariables.JAVA);
        return strings;
    }

    public List<String> getPresenter(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add(GlobalVariables.IPRESENTER + GlobalVariables.JAVA);
        strings.add(GlobalVariables.BPRESENTER + GlobalVariables.JAVA);
        return strings;
    }
    public List<String> getBaseView(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("BaseActivity" + GlobalVariables.JAVA);
        strings.add("BaseFragment" + GlobalVariables.JAVA);
        strings.add("BaseDialog" + GlobalVariables.JAVA);
        strings.add("MVPBaseActivity" + GlobalVariables.JAVA);
        strings.add("MVPBaseFragmentActivity" + GlobalVariables.JAVA);
        return strings;
    }
    public String getBaseViewSuffix(String key){
        Map<String,String> map = new HashMap<>();
        map.put("BaseActivity","Activity");
        map.put("MVPBaseActivity","Activity");
        map.put("MVPBaseFragmentActivity","Activity");
        map.put("BaseFragment","Fragment");
        map.put("BaseDialog","Dialog");
        return map.get(key);
    }

    public List<String> getBasePresenter(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("BasePresenterImpl" + GlobalVariables.JAVA);
        return strings;
    }

    public List<String> getLocalConfiguration(String key){
        String[] values = PropertiesComponent.getInstance(mProject).getValues(key);
        if (values == null){
            return new ArrayList<String>();
        }
        return Arrays.asList(values);
    }


    public String[] getViewNameSuffix(){
        return new String[]{"Activity",
                    "View",
                    "Dialog",
                    "Fragment",
                    "PopupWindow",
                    "Action"};
    }


}
