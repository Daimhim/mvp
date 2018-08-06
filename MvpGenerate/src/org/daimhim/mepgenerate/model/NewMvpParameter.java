package org.daimhim.mepgenerate.model;

public class NewMvpParameter {
    private boolean isIView;
    private boolean isIPresenter;
    private boolean isIModel;

    private boolean isImplP;
    private boolean isImplV;

    private boolean isBaseView;
    private boolean isBasePresenter;
    private boolean isBaseModel;

    private String iVIEW;
    private String iPRESENTER;
    private String iMODEL;

    private String bVIEW;
    private String bPRESENTER;
    private String bMODEL;

    private String classSuffix;
    private String className;

    @Override
    public String toString() {
        return "NewMvpParameter{" +
                "isIView=" + isIView +
                ", isIPresenter=" + isIPresenter +
                ", isIModel=" + isIModel +
                ", isImplP=" + isImplP +
                ", isImplV=" + isImplV +
                ", isBaseView=" + isBaseView +
                ", isBasePresenter=" + isBasePresenter +
                ", isBaseModel=" + isBaseModel +
                ", iVIEW='" + iVIEW + '\'' +
                ", iPRESENTER='" + iPRESENTER + '\'' +
                ", iMODEL='" + iMODEL + '\'' +
                ", bVIEW='" + bVIEW + '\'' +
                ", bPRESENTER='" + bPRESENTER + '\'' +
                ", bMODEL='" + bMODEL + '\'' +
                ", classSuffix='" + classSuffix + '\'' +
                ", className='" + className + '\'' +
                '}';
    }

    public boolean isImplP() {
        return isImplP;
    }

    public void setImplP(boolean implP) {
        isImplP = implP;
    }

    public boolean isImplV() {
        return isImplV;
    }

    public void setImplV(boolean implV) {
        isImplV = implV;
    }

    public String getClassSuffix() {
        return classSuffix;
    }

    public void setClassSuffix(String classSuffix) {
        this.classSuffix = classSuffix;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isIView() {
        return isIView;
    }

    public void setIView(boolean IView) {
        isIView = IView;
    }

    public boolean isIPresenter() {
        return isIPresenter;
    }

    public void setIPresenter(boolean IPresenter) {
        isIPresenter = IPresenter;
    }

    public boolean isIModel() {
        return isIModel;
    }

    public void setIModel(boolean IModel) {
        isIModel = IModel;
    }

    public boolean isBaseView() {
        return isBaseView;
    }

    public void setBaseView(boolean baseView) {
        isBaseView = baseView;
    }

    public boolean isBasePresenter() {
        return isBasePresenter;
    }

    public void setBasePresenter(boolean basePresenter) {
        isBasePresenter = basePresenter;
    }

    public boolean isBaseModel() {
        return isBaseModel;
    }

    public void setBaseModel(boolean baseModel) {
        isBaseModel = baseModel;
    }

    public String getiVIEW() {
        return iVIEW;
    }

    public void setiVIEW(String iVIEW) {
        this.iVIEW = iVIEW;
    }

    public String getiPRESENTER() {
        return iPRESENTER;
    }

    public void setiPRESENTER(String iPRESENTER) {
        this.iPRESENTER = iPRESENTER;
    }

    public String getiMODEL() {
        return iMODEL;
    }

    public void setiMODEL(String iMODEL) {
        this.iMODEL = iMODEL;
    }

    public String getbVIEW() {
        return bVIEW;
    }

    public void setbVIEW(String bVIEW) {
        this.bVIEW = bVIEW;
    }

    public String getbPRESENTER() {
        return bPRESENTER;
    }

    public void setbPRESENTER(String bPRESENTER) {
        this.bPRESENTER = bPRESENTER;
    }

    public String getbMODEL() {
        return bMODEL;
    }

    public void setbMODEL(String bMODEL) {
        this.bMODEL = bMODEL;
    }
}
