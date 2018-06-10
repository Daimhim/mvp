package org.daimhim.mepgenerate.help;

import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileUrlChangeAdapter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.util.PsiClassUtil;

import java.util.ArrayList;

public class VirtualFileHelp {

    /**
     * B树任意节点开始查找
     *
     * @param terminator  终止目录名
     * @param findFileName  查找的文件
     * @param upNode  下层数据
     * @param isUnder 向下 还是向上
     * @param list    查找结果 保存
     * @param file    起点文件
     * @return 返回查找结果
     */
    private static ArrayList<VirtualFile> forVirtualFile(String terminator, String[] findFileName, VirtualFile upNode, boolean isUnder, ArrayList<VirtualFile> list, VirtualFile file) {
        for (int i = 0; i < findFileName.length; i++) {
            if (findFileName[i].equals(file.getName())) {
                list.add(file);
            }
        }
        if (file.isDirectory()) {
            //文件夹
            VirtualFile[] files = file.getChildren();
            int i = 0;
            VirtualFile file1 = null;
            for (; i < files.length; i++) {
                file1 = files[i];
                if (upNode == null || !file1.getName().equals(upNode.getName())) {
                    forVirtualFile(terminator, findFileName, null, false, list, file1);
                }
            }
            if (isUnder && !terminator.equals(file.getName())) {
                forVirtualFile(terminator, findFileName, files[i - 1].getParent(), true, list, files[i - 1].getParent().getParent());
            }
        } else {
            //文件
            if (isUnder && !terminator.equals(file.getName())) {
                forVirtualFile(terminator, findFileName, file.getParent(), true, list, file.getParent().getParent());
            }
        }
        return list;
    }

    public static ArrayList<VirtualFile> findTagVirtualFile(VirtualFile file,String... findFileName) {
        return forVirtualFile("src",
                findFileName,null,true,new ArrayList<>(),file);
    }

    //根据路径获取包名
    public static String getPackageName(VirtualFile data) {
        if (null == data) return null;
        String path = data.getPath();
        return (path.substring(path.indexOf("java/") + "java/".length(), path.length()))
                .replace("/", ".")
                .replace("."+data.getName(),"");
    }

    /**
     * 获取工作目录路径
     *
     * @param data
     * @return
     */
    public static String getWorkList(VirtualFile data) {
        return data.getPath().substring(0, data.getPath().indexOf("src/main/java"));
    }
}
