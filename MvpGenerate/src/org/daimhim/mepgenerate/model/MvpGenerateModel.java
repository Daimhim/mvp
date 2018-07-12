package org.daimhim.mepgenerate.model;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import org.daimhim.mepgenerate.GlobalVariables;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvpGenerateModel {
    private Map<String,List<PsiClass>> baseClasss;
    private Project mProject;
    public MvpGenerateModel() {
        init();
    }



    private void init() {
        baseClasss = new HashMap<>();
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

    public List<VirtualFile> getLocalConfiguration(String key){
        String[] values = PropertiesComponent.getInstance(mProject).getValues(key);
        ArrayList<VirtualFile> list = new ArrayList<>();
        VirtualFile virtualFile = null;
        for (int i = 0; i < values.length; i++) {
            virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(values[i]));
            if (virtualFile != null) {
                list.add(virtualFile);
            }
        }
        return list;
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
