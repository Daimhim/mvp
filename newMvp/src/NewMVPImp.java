import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import toolbox.Log;

/**
 * Created by Daimhim on 2017/7/7.
 */
public class NewMVPImp implements ModuleComponent {
    protected String TAG = getClass().getSimpleName();
    public NewMVPImp(Module module) {
    }
    //初始化组件
    @Override
    public void initComponent() {
    }
    //处理组件
    @Override
    public void disposeComponent() {
    }
    //获取组件名称
    @Override
    @NotNull
    public String getComponentName() {
        return "NewMVPImp";
    }

    //模块已添加
    @Override
    public void moduleAdded() {
    }
}
