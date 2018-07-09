package org.daimhim.mepgenerate;

import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ResourceImportTest {

    public static void main(String[] args) {
        ResourceImportTest importTest = new ResourceImportTest();
        //        printDirectory(new File("G:\\meyki-zhshm-design\\UI\\天天有用\\01首页\\03切图\\首页").getPath());
        importTest.copyFile(true);
    }

    boolean isExit = true;
    //项目目录
    File[] mFromFile;
    //资源目录
    File[] mToFile;
    /**
     * F copyName, S realName
     */
    Map<String, String> mFiles;
    ExecutorService mExecutorService = null;

    String fromFilePath = "H:\\formalProject\\meyki-zhshm\\code\\branches\\sclient-v1.16.0\\skt\\src\\main\\res";
    String toFilePath = "G:\\meyki-zhshm-design\\UI\\天天有用V1.0\\01天天有用 主目录\\01首页\\03切图\\签到后领券弹层\\android";

    void init() {
        mFromFile = new File(fromFilePath).listFiles();
        mToFile = new File(toFilePath).listFiles();
        mFiles = new HashMap<>();
        mExecutorService = Executors.newFixedThreadPool(5);
//        mFiles.put("digital_coupon_incentive_digital_coupon_bg.png", "激励数字券.png");
//        mFiles.put("digital_coupon_available_balance_bg.png", "数字券可用余额.png");

        mFiles.put("sign_in_dialog_bg.png", "签到后领券弹层背景.png");
        mFiles.put("go_to_recommend.png", "去推荐.png");
//        mFiles.put("digital_coupon_share_sinaweibo.png", "微博-分享.png");
//        mFiles.put("digital_coupon_share_wechat.png", "微信-分享.png");
    }

    private void copyFile(boolean mode) {
        init();
        for (int i = 0; i < mFromFile.length; i++) {
            for (int j = 0; j < mToFile.length; j++) {
                if (mFromFile[i].getName().equals(mToFile[j].getName()) && mFromFile[i].isDirectory() && mToFile[j].isDirectory()) {
                    mExecutorService.execute(new ResourceImport(mFromFile[i], mToFile[j], mFiles, mode));
                    continue;
                }
            }
        }
        try {
            Thread.sleep(3000);
            int activeCount = 1;
            while (activeCount > 0) {
                activeCount = ((ThreadPoolExecutor) mExecutorService).getActiveCount();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void printDirectory(String filePath) {
        File file = new File(filePath);
        for (File itemFile :
                file.listFiles()) {
            System.out.println(itemFile.getName());
        }
    }

    class ResourceImport implements Runnable {
        File mFromFile;
        File mToFile;
        Map<String, String> mFiles;
        //源文件
        File temFile = null;
        //目标文件
        File targetFile = null;
        //覆写模式
        boolean mOverwrite;

        public ResourceImport(File fromFile, File toFile, Map<String, String> files) {
            mFromFile = fromFile;
            mToFile = toFile;
            mFiles = files;
        }

        public ResourceImport(File fromFile, File toFile, Map<String, String> files, boolean overwrite) {
            mFromFile = fromFile;
            mToFile = toFile;
            mFiles = files;
            mOverwrite = overwrite;
        }

        @Override
        public void run() {
            Set<Map.Entry<String, String>> entries = mFiles.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                temFile = findFile(mToFile.listFiles(), next.getValue());
                targetFile = findFile(mFromFile.listFiles(), next.getKey());
                if (targetFile == null) {
                    targetFile = findFile(mFromFile.listFiles(), next.getValue());
                }
//                if (targetFile != null && !targetFile.getName().equals(next.getKey())) {
//                    Log("在目标文件找到了" + targetFile.getPath() + ",不用复制，直接改名为" + mFromFile.getPath() + "\\" + next.getKey());
//                    targetFile.renameTo(new File(mFromFile.getPath() + "\\" + next.getKey()));
//                    continue;
//                }
                if (temFile != null && targetFile != null && targetFile.getName().equals(next.getKey())) {
                    if (mOverwrite) {
                        Log("在目标文件找到了" + targetFile.getPath() + ",开始覆写");
                        overwriteFileUsingFileChannels(temFile, new File(mFromFile.getPath() + "\\" + next.getKey()));
                    } else {
                        Log("在目标文件找到了" + targetFile.getPath() + ",不复制");
                    }
                    continue;
                }
                if (null != temFile) {
                    Log("找到源文件" + temFile.getPath() + ",开始复制到" + mFromFile.getPath() + "\\" + next.getKey());
                    copyFileUsingFileChannels(temFile, new File(mFromFile.getPath() + "\\" + next.getKey()));
                    Log("文件复制完成：" + temFile.getPath() + "-------->>>>" + mFromFile.getPath() + "\\" + next.getKey());
                }
            }
        }
    }

    void overwriteFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    File findFile(File[] files, String fileName) {
        for (int i = 0; i < files.length; i++) {
            if (fileName.equals(files[i].getName())) {
                return files[i];
            }
        }
        return null;
    }

    void Log(String content) {
        System.out.println(content);
    }
}
