package org.daimhim.mepgenerate.help;

import com.intellij.openapi.vfs.VirtualFile;

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
    public static ArrayList<VirtualFile> forVirtualFile(String terminator, String findFileName, VirtualFile upNode, boolean isUnder, ArrayList<VirtualFile> list, VirtualFile file) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (findFileName.equals(file.getName())) {
            list.add(file);
        }
        if (file.isDirectory()) {
            //文件夹
            VirtualFile[] files = file.getChildren();
            int i = 0;
            for (; i < files.length; i++) {
                VirtualFile file1 = files[i];
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
                forVirtualFile(terminator, findFileName, file, true, list, file.getParent());
            }
        }
        return list;
    }
}
