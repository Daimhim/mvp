package newafpm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import general.BaseMVPHelp;
import toolbox.QLog;
import toolbox.TextUtils;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;
import static general.GlobalVariable.*;

/**
 * Created by Daimhim on 2017/8/31.
 */
public class NewAFPMHelp extends BaseMVPHelp<NewAFPMAction> {
    Project mProject;
    PsiDirectory mPsiDirectory;
    protected PsiClass mMVPActivity,
            mMVPContract,
            mContractView,
            mContractModule,
            mContractPresenter,
            mMVPFragment,
            mMVPModule,
            mMVPPresenter;
    PsiElementFactory mElementFactory;

    @Override
    public void onPrepare() {
        super.onPrepare();
        mProject = pAnActionEvent.getProject();
        VirtualFile data = pAnActionEvent.getData(VIRTUAL_FILE);
        mPsiDirectory = PsiDirectoryFactory.getInstance(mProject).createDirectory(data);
        mElementFactory = JavaPsiFacade.getElementFactory(mPsiDirectory.getProject());
    }

    @Override
    public void onBackstage() {
        super.onBackstage();
        //初始化 Contract
        PsiFile file = mPsiDirectory.findFile(pClassName + MVP_CONTRACT + MVP_JAVA);
        initContract(file);
        //初始化Acitivity
        file = mPsiDirectory.findFile(pClassName + MVP_ACTIVITY + MVP_JAVA);
        initActivity(file);
        //初始化Presenter
        file = mPsiDirectory.findFile(pClassName + MVP_PRESENTERIMPL + MVP_JAVA);
        initPresenter(file);
        //初始化Fragment
        file = mPsiDirectory.findFile(pClassName + MVP_FRAGMENT + MVP_JAVA);
        initFragment(file);
        //初始化Module
        file = mPsiDirectory.findFile(pClassName + MVP_MODULEIMPL + MVP_JAVA);
        initModule(file);
    }

    private void initModule(PsiFile file) {
        if (null != file) {
            mMVPModule = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
        } else {
            mMVPModule = JavaDirectoryService.getInstance().createClass(mPsiDirectory, pClassName + MVP_MODULEIMPL);
            mMVPModule.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
        }
        //检测继承
        if (!mMVPModule.isInheritor(pMVPBaseFragment, false)) {
            mMVPModule.getExtendsList().add(mElementFactory.createClassReferenceElement(pMVPBaseModule));
        }
        //检测实现
        if (!mMVPModule.isInheritor(mContractModule, false)) {
            mMVPModule.getImplementsList().add(mElementFactory.createClassReferenceElement(mContractModule));
        }
        //添加抽象方法
        abstractImplementation(mElementFactory, pMVPBaseModule, mMVPModule);
        abstractImplementation(mElementFactory, pMVPBaseModule, mContractModule);
    }

    private void initFragment(PsiFile file) {
        if (null != file) {
            mMVPFragment = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
        } else {
            mMVPFragment = JavaDirectoryService.getInstance().createClass(mPsiDirectory, pClassName + MVP_FRAGMENT);
            mMVPFragment.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
        }
        //检测继承
        if (!mMVPFragment.isInheritor(pMVPBaseFragment, false)) {
            mMVPFragment.getExtendsList().add(mElementFactory.createClassReferenceElement(pMVPBaseFragment));
            for (PsiJavaCodeReferenceElement element : mMVPFragment.getExtendsList().getReferenceElements()) {
                if (TextUtils.equals(element.getQualifiedName(), pMVPBaseActivity.getQualifiedName())) {
                    //添加泛型
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractPresenter)));
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractModule)));
                    break;
                }
            }
        }
        //添加抽象方法
        abstractImplementation(mElementFactory, pMVPBaseFragment, mMVPFragment);
    }

    private void initPresenter(PsiFile file) {
        if (null != file) {
            mMVPPresenter = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
        } else {
            mMVPPresenter = JavaDirectoryService.getInstance().createClass(mPsiDirectory, pClassName + MVP_PRESENTERIMPL);
            mMVPPresenter.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
        }
        //检测继承
        if (!mMVPPresenter.isInheritor(pMVPBasePresenter, false)) {
            mMVPPresenter.getExtendsList().add(mElementFactory.createClassReferenceElement(pMVPBasePresenter));
//            mMVPPresenter.getExtendsListTypes()[0].
            for (PsiJavaCodeReferenceElement element :
                    mMVPPresenter.getExtendsList().getReferenceElements()) {
                if (TextUtils.equals(element.getQualifiedName(), pMVPBasePresenter.getQualifiedName())) {
                    //添加泛型
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractView)));
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractModule)));
                    break;
                }
            }
        }
        //检测实现
        if (!mMVPPresenter.isInheritor(mContractPresenter, false)) {
            mMVPPresenter.getImplementsList().add(mElementFactory.createClassReferenceElement(mContractPresenter));
        }

        //添加抽象方法
        abstractImplementation(mElementFactory, pMVPBasePresenter, mMVPPresenter);
        abstractImplementation(mElementFactory, mContractPresenter, mMVPPresenter);
    }

    private void initActivity(PsiFile file) {
        //检测是否有
        if (null != file) {
            mMVPActivity = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
        } else {
            mMVPActivity = JavaDirectoryService.getInstance().createClass(mPsiDirectory, pClassName + MVP_ACTIVITY);
            mMVPActivity.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
        }
        //检测继承
        if (!mMVPActivity.isInheritor(pMVPBaseActivity, false)) {
            mMVPActivity.getExtendsList().add(mElementFactory.createClassReferenceElement(pMVPBaseActivity));
            for (PsiJavaCodeReferenceElement element : mMVPActivity.getExtendsList().getReferenceElements()) {
                PsiClassType[] extendsListTypes1 = mMVPActivity.getExtendsListTypes();
                for (int i = 0; i < extendsListTypes1.length; i++) {
                    System.out.println("getClassName:"+extendsListTypes1[i].getClassName());
                    PsiType[] parameters = extendsListTypes1[i].getParameters();
                    for (int j = 0; j < parameters.length; j++) {
                        System.out.println("getCanonicalText:"+parameters[j].getCanonicalText());
                    }
                }
                if (TextUtils.equals(element.getQualifiedName(), pMVPBaseActivity.getQualifiedName())) {
                    //添加泛型
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractPresenter)));
                    element.getParameterList().add(mElementFactory.createTypeElement(mElementFactory.createType(mContractModule)));
                    break;
                }
            }
        }
        //添加抽象方法
        abstractImplementation(mElementFactory, pMVPBaseActivity, mMVPActivity);
    }

    private void initContract(PsiFile file) {
        if (null != file) {
            mMVPContract = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
        } else {
            mMVPContract = JavaDirectoryService.getInstance().createInterface(mPsiDirectory, pClassName + MVP_CONTRACT);
            mMVPContract.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
        }
        //初始化View  合同
        mContractView = mMVPContract.findInnerClassByName(MVP_VIEW, false);
        if (mContractView == null) {
            mContractView = mElementFactory.createInterface(MVP_VIEW);
            mContractView.getExtendsList().add(mElementFactory
                    .createClassReferenceElement(pMVPBaseContract.findInnerClassByName(MVP_VIEW, false)));   //添加父类的内部类继承
            mMVPContract.add(mContractView);
        }
        //初始化 Presenter 合同
        mContractPresenter = mMVPContract.findInnerClassByName(MVP_PRESENTER, false);
        if (mContractPresenter == null) {
            mContractPresenter = mElementFactory.createInterface(MVP_PRESENTER);
            mContractPresenter.getExtendsList().add(mElementFactory
                    .createClassReferenceElement(pMVPBaseContract.findInnerClassByName(MVP_PRESENTER, false)));
            mMVPContract.add(mContractPresenter);
        }
        //初始化 Module 合同
        mContractModule = mMVPContract.findInnerClassByName(MVP_MODULE, false);
        if (mContractModule == null) {
            mContractModule = mElementFactory.createInterface(MVP_MODULE);
            mContractModule.getExtendsList().add(mElementFactory
                    .createClassReferenceElement(pMVPBaseContract.findInnerClassByName(MVP_MODULE, false)));
            mMVPContract.add(mContractModule);
        }
        mContractView = mMVPContract.findInnerClassByName(MVP_VIEW, false);
        mContractPresenter = mMVPContract.findInnerClassByName(MVP_PRESENTER, false);
        mContractModule = mMVPContract.findInnerClassByName(MVP_MODULE, false);
    }

    //添加抽象方法
    private void abstractImplementation(PsiElementFactory elementFactory, PsiClass absClass, PsiClass impClass) {
        //添加抽象方法
        PsiMethod method = null;
        //构造方法
        for (PsiMethod constructorMethod : absClass.getConstructors()) {
            //已有 不需要创建
            if (null == findMethod(constructorMethod, impClass)) {    //创建一个新的
                method = elementFactory.createConstructor(constructorMethod.getName(),impClass);
                method.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);  //添加修饰
                PsiParameter[] parameters = constructorMethod.getParameterList().getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    method.getParameterList().add(elementFactory.createParameter(parameters[i].getName(),parameters[i].getType(),impClass));
                    QLog.v(TAG,"getName:"+parameters[i].getName());
                    QLog.v(TAG,parameters[i].getTypeElement().getText()+"   parameters:"+parameters[i].getName());
                }
                impClass.add(method);
                findMethod(constructorMethod,impClass);
            }
            method = findMethod(constructorMethod, impClass);
            //复制注释
            if (null != constructorMethod.getDocComment() && null == method.getDocComment()) {
                method.addRangeBefore(constructorMethod.getDocComment(), constructorMethod.getDocComment(), method.getFirstChild());
            }
        }
        //抽象方法
        PsiType returnType = null;
        for (PsiMethod absMethod : absClass.getMethods()){
            if (absMethod.getModifierList().hasExplicitModifier(PsiModifier.ABSTRACT)) {
                //已有 不需要创建
                if (null == findMethod(absMethod, impClass)) {    //创建一个新的
                    method = elementFactory.createMethod(absMethod.getName(), absMethod.getReturnType(), impClass);
                    method.getModifierList().addAnnotation("Override");
                    //复制方法参数
                    for (PsiParameter parameters : absMethod.getParameterList().getParameters()) {
                        method.getParameterList().add(parameters);
                    }
                    //添加默认返回值
                    returnType = method.getReturnType();
                    if (!returnType.equalsToText("void")){
                        if (returnType.equalsToText("int") || returnType.equalsToText("Integer")
                                || returnType.equalsToText("Double") || returnType.equalsToText("double")
                                || returnType.equalsToText("float") || returnType.equalsToText("Float")
                                || returnType.equalsToText("Long") || returnType.equalsToText("Long")){

                            method.getBody().add(elementFactory.createStatementFromText("return 0;", method));
                        }else {
                            method.getBody().add(elementFactory.createStatementFromText("return null;",method));
                        }
                    }
                    impClass.add(method);
                }
                method = findMethod(method, impClass);
                //复制注释
                if (null != absMethod.getDocComment() && null == method.getDocComment()) {
                    method.addRangeBefore(absMethod.getDocComment(), absMethod.getDocComment(), method.getFirstChild());
                }
            }
        }

    }

    private PsiMethod findMethod(PsiMethod absMethod, PsiClass impClass) {
        for (PsiMethod method :
                impClass.findMethodsByName(absMethod.getName(), false)) {
            if (absMethod.getParameterList().getParametersCount() == method.getParameterList().getParametersCount()
                    && absMethod.getReturnType().equalsToText(method.getReturnType().getCanonicalText())) {
                return method;
            }
        }
        return null;
    }

    private String filterParameter(){
        return null;
    }
}
