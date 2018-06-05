package general;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.util.PsiTreeUtil;
import toolbox.TextUtils;

/**
 * Created by Administrator on 2017/8/11 0011.
 */
public class ClassOperation {

    /**
     * 根据路径获取包名
     * @param data
     * @return
     */
    public String getPackageName(VirtualFile data) {
        if (null == data) return null;
        String path = data.getPath();
        int i = path.indexOf("java/");
        if (i == -1) return null;
        return (path.substring(i + "java/".length(), path.length())).replace("/", ".");
    }

    /**
     * 根据类名返回 PsiClass
     * @param files  当前目录
     * @param className 类名
     * @return
     */
    public PsiClass findPsiClassFromPsiFile(PsiFile[] files, String className) {
        for (PsiFile itemFile : files) {
            if (TextUtils.equals(itemFile.getName(), className + ".java")) {
                return PsiTreeUtil.findChildOfAnyType(itemFile, PsiClass.class);
            }
        }
        return null;
    }

    /**
     * 该类是否实现了另一个接口
     * @param psiClass  当前类
     * @param classByName 接口类
     * @return
     */
    private boolean isImplements(PsiClass psiClass,PsiClass classByName){
        PsiJavaCodeReferenceElement[] referenceElements = psiClass.getImplementsList().getReferenceElements();
        for (int i = 0; i < referenceElements.length; i++) {
            if (TextUtils.equals(referenceElements[i].getReferenceName(),classByName.getName().replace(".java",""))){
                return false;
            }
        }
        return true;
    }

    /**
     * 检查当前包下是否有重复
     * @param files 文件
     * @param className  名称
     * @param suffix  后缀
     * @return 有的话返回
     */
    public PsiClass checkRepeat(PsiFile[] files, String className, String suffix) {
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName().replace(".java", "");
            if (name.endsWith(suffix) && name.equals(className + suffix)) {
                return PsiTreeUtil.findChildOfAnyType(files[i], PsiClass.class);
            }
        }
        return null;
    }
}
