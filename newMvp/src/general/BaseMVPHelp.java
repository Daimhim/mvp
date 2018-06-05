package general;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import static general.GlobalVariable.MVP_CLASS_SUFFIX;

/**
 * Created by Daimhim on 2017/8/10.
 */
public abstract class BaseMVPHelp<A extends BaseAnAction> implements Runnable, CreateHelp{
    protected String TAG = getClass().getSimpleName();
    protected final String pActivityName = "MVPBaseActivity";
    protected final String pContractName = "MVPBaseContract";
    protected final String pFragmentName = "MVPBaseFragment";
    protected final String pModuleName = "MVPBaseModule";
    protected final String pPresenterName = "MVPBasePresenter";
    protected String[] MVPNames = {pActivityName,
            pContractName,
            pFragmentName,
            pModuleName,
            pPresenterName};
    protected AnActionEvent pAnActionEvent;
    protected String pClassName;

    protected A action;

    protected PsiClass pMVPBaseActivity,
            pMVPBaseContract, pMVPBaseFragment, pMVPBaseModule, pMVPBasePresenter;

    public void setAction(A pAction) {
        this.action = pAction;
    }

    @Override
    public void onCreate(AnActionEvent anActionEvent) {
        pAnActionEvent = anActionEvent;
        init();
        onPrepare();
        WriteCommandAction.runWriteCommandAction(pAnActionEvent.getProject(), this);
    }

    private void init() {
        for (int i = 0; i < MVPNames.length; i++) {
            PsiClass[] psiClasses = PsiShortNamesCache.getInstance(pAnActionEvent.getProject())
                    .getClassesByName(MVPNames[i], GlobalSearchScope.allScope(pAnActionEvent.getProject()));
            for (int j = 0; j < psiClasses.length; j++) {
                if (psiClasses[j].getQualifiedName().endsWith("mvp." + MVPNames[i])) {
                    switch (psiClasses[j].getName()) {
                        case pActivityName:
                            pMVPBaseActivity = psiClasses[j];
                            break;
                        case pContractName:
                            pMVPBaseContract = psiClasses[j];
                            break;
                        case pFragmentName:
                            pMVPBaseFragment = psiClasses[j];
                            break;
                        case pModuleName:
                            pMVPBaseModule = psiClasses[j];
                            break;
                        case pPresenterName:
                            pMVPBasePresenter = psiClasses[j];
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }

    @Override
    public void onPrepare(){

    }

    @Override
    public void onBackstage(){
        onStop();
    }

    @Override
    public void onStop(){

    }

    /**
     * 获取当前类名 吧后缀去除调
     *
     * @param s
     * @return
     */
    public static String classNameCorrection(String s) {
        for (int i = 0; i < MVP_CLASS_SUFFIX.length; i++) {
            if (s.endsWith(MVP_CLASS_SUFFIX[i])) {
                return s.replace(MVP_CLASS_SUFFIX[i], "");
            }
        }
        return s;
    }

    public static boolean isNewMVPSimple(String path) {
        if (path.indexOf("/src/main/java/") < 0) return false;
        return true;
    }

    @Override
    public final void run() {
        onBackstage();
    }

    public void setpClassName(String pClassName) {
        this.pClassName = pClassName;
    }

}
