package org.daimhim.mepgenerate.model;

import com.intellij.psi.PsiClass;
import org.daimhim.mepgenerate.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvpGenerateModel {
    private Map<String,List<PsiClass>> baseClasss;

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

    public String[] getViewNameSuffix(){
        return new String[]{"Activity",
                    "View",
                    "Dialog",
                    "Fragment",
                    "PopupWindow",
                    "Action"};
    }


}
