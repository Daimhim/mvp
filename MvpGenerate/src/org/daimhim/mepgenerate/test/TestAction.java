package org.daimhim.mepgenerate.test;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.impl.ModuleManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TestAction extends BaseGenerateAction {
    public TestAction() {
        super(null);
    }

    public TestAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiClass psiClass = getTargetClass(editor, mFile);
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        System.out.println("psiClass.getName:" + psiClass.getName());
        System.out.println("project.getBasePath:" + project.getBasePath());
        System.out.println("virtualFile.getPresentableName:" + virtualFile.getPresentableName());
        System.out.println("virtualFile.getCanonicalPath:" + virtualFile.getCanonicalPath());
        System.out.println("virtualFile.getExtension:" + virtualFile.getExtension());
        System.out.println("virtualFile.getParent:" + virtualFile.getParent());
        System.out.println("virtualFile.getParent.getName():" + virtualFile.getParent().getName());
        System.out.println("virtualFile.getPresentableUrl:" + virtualFile.getPresentableUrl());
        String path = virtualFile.getPath();
        System.out.println("virtualFile.getPath:" + path);
        String substring = path.substring(0, path.indexOf("src/main/java"));
        System.out.println("substring:" + substring);
        System.out.println("project.getProjectFile:" + project.getProjectFile());
        System.out.println("project.getWorkspaceFile:" + project.getWorkspaceFile());
        System.out.println("project.getBaseDir:" + project.getBaseDir());
        System.out.println("project.getProjectFilePath:" + project.getProjectFilePath());
        System.out.println("psiClass.getQualifiedName:" + psiClass.getQualifiedName());
        System.out.println("project.getWorkspaceFile().getPath():" + project.getWorkspaceFile());
//        PsiPackage mvp = JavaPsiFacade.getInstance(project).findPackage("mvp");
//        System.out.println("mvp:"+mvp.containsClassNamed("BaseView"));
//        System.out.println("mvp:"+mvp.getText());
//        DocumentImpl document = new DocumentImpl(substring);
//        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
//        System.out.println("file.getName():"+file.getName());

        System.out.println("-----moduleForFile---------");
        Module moduleForFile = ModuleUtil.findModuleForFile(virtualFile, project);
        System.out.println(moduleForFile.getName());
        System.out.println("-----allDependentModules---------");
        List<Module> allDependentModules = ModuleUtil.getAllDependentModules(moduleForFile);
        for (int i = 0; i < allDependentModules.size(); i++) {
            System.out.println(allDependentModules.get(i).getName());
        }
        System.out.println("---------getNodes--------");
        Graph<Module> moduleGraph = ModuleManager.getInstance(project).moduleGraph();
        Iterator<Module> iterator = moduleGraph.getNodes().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            System.out.println(iterator.next().getName());
        }
        System.out.println("---------getIn--------");
        Iterator<Module> in = moduleGraph.getIn(moduleForFile);
        while (in.hasNext()) {
            System.out.println(in.next().getName());
        }
        System.out.println("------modifiableModel-----------");
        ModifiableModuleModel modifiableModel = ModuleManager.getInstance(project).getModifiableModel();
        Module[] modules1 = modifiableModel.getModules();
        for (int i = 0; i < modules1.length; i++) {
            System.out.println(modules1[i].getName());
        }
//        ModuleManager.getInstance(project).;
        System.out.println("-----------------");
        Module[] modules = ModuleManagerImpl.getInstanceImpl(project).getModules();
        for (int i = 0; i < modules.length; i++) {
            System.out.println(modules[i].getName());
        }
        PsiField[] allFields = psiClass.getAllFields();
        for (int i = 0; i < allFields.length; i++) {
            System.out.println("allFields:"+allFields[i].getName() + "   getType:"+allFields[i].getType().toString());
        }
//        PropertiesComponent.getInstance(project)
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(new FileChooserDescriptor(true, true, true, true, true, true)
                , project, virtualFile);
        for (int i = 0; i < virtualFiles.length; i++) {
            System.out.println(virtualFiles[i].getPath());
            System.out.println(virtualFiles[i].getUrl());
            System.out.println(LocalFileSystem.getInstance().findFileByIoFile(new File(virtualFiles[i].getPath())).getName());
        }
    }


    public static void main(String[] args) {
//        doInBackground("",
//                "H:\\msdia80.dll",
//                "out",
//                "POST");

    }

    /**
     * @param strings [0] url [1] filepatch [2] in or out [3] requestType
     *                [4] ReadTimeout [5] ConnectTimeout
     * @return
     */
    protected static Integer doInBackground(String... strings) {
        HttpURLConnection connection = null;
        InputStream in = null;
        OutputStream out = null;
        InputStream callIn = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(strings.length < 4 ? "GET" : strings[3]);
            connection.setReadTimeout(strings.length < 5 ? 5000 : Integer.valueOf(strings[4]));
            connection.setConnectTimeout(strings.length < 6 ? 10000 : Integer.valueOf(strings[5]));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //数据编码格式，这里utf-8
            connection.setRequestProperty("Charset", "utf-8");
            // 配置请求Content-Type
            connection.setRequestProperty("Content-Type", "form-data");
            connection.connect();
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            if ("in".equals(strings[2])) {
                in = connection.getInputStream();
                out = new FileOutputStream(new File(strings[1]));
            } else {
                out = connection.getOutputStream();
                in = new FileInputStream(new File(strings[1]));
//                out.write("file=".getBytes());
            }
            byte[] buf = new byte[1024];
            int ch;
            while ((ch = in.read(buf)) != -1) {
                out.write(buf, 0, ch);
            }
            out.flush();
            out.close();
            callIn = connection.getInputStream();
            buf = new byte[callIn.available()];
            callIn.read(buf);
            System.out.println(new String(buf));
//            }
            return connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        }
        return HttpURLConnection.HTTP_NO_CONTENT;
    }
}
