package com.company.project.model;

import javax.persistence.*;

@Table(name = "PRE_JFJLCOMMFORM")
public class PreJfjlcommform {
    @Id
    @Column(name = "UNID")
    private String unid;

    @Column(name = "PROJID")
    private String projid;

    @Column(name = "FORM_NAME")
    private String formName;

    @Column(name = "FORM_UNID")
    private String formUnid;

    @Column(name = "FORM_SORT")
    private Short formSort;

    @Column(name = "USE_UNID")
    private String useUnid;

    @Column(name = "USE_TYPE")
    private String useType;

    @Column(name = "ITEM_VALUES")
    private String itemValues;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "BELONGSYSTEM")
    private String belongsystem;

    @Column(name = "CREATE_TIME")
    private String createTime;

    @Column(name = "SYNC_STATUS")
    private String syncStatus;

    @Column(name = "SYNC_RESULT")
    private String syncResult;

    @Column(name = "DATAVERSION")
    private Short dataversion;

    @Column(name = "YLZD1")
    private String ylzd1;

    @Column(name = "YLZD2")
    private String ylzd2;

    @Column(name = "EXTEND")
    private String extend;

    /**
     * @return UNID
     */
    public String getUnid() {
        return unid;
    }

    /**
     * @param unid
     */
    public void setUnid(String unid) {
        this.unid = unid;
    }

    /**
     * @return PROJID
     */
    public String getProjid() {
        return projid;
    }

    /**
     * @param projid
     */
    public void setProjid(String projid) {
        this.projid = projid;
    }

    /**
     * @return FORM_NAME
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * @return FORM_UNID
     */
    public String getFormUnid() {
        return formUnid;
    }

    /**
     * @param formUnid
     */
    public void setFormUnid(String formUnid) {
        this.formUnid = formUnid;
    }

    /**
     * @return FORM_SORT
     */
    public Short getFormSort() {
        return formSort;
    }

    /**
     * @param formSort
     */
    public void setFormSort(Short formSort) {
        this.formSort = formSort;
    }

    /**
     * @return USE_UNID
     */
    public String getUseUnid() {
        return useUnid;
    }

    /**
     * @param useUnid
     */
    public void setUseUnid(String useUnid) {
        this.useUnid = useUnid;
    }

    /**
     * @return USE_TYPE
     */
    public String getUseType() {
        return useType;
    }

    /**
     * @param useType
     */
    public void setUseType(String useType) {
        this.useType = useType;
    }

    /**
     * @return ITEM_VALUES
     */
    public String getItemValues() {
        return itemValues;
    }

    /**
     * @param itemValues
     */
    public void setItemValues(String itemValues) {
        this.itemValues = itemValues;
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return BELONGSYSTEM
     */
    public String getBelongsystem() {
        return belongsystem;
    }

    /**
     * @param belongsystem
     */
    public void setBelongsystem(String belongsystem) {
        this.belongsystem = belongsystem;
    }

    /**
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return SYNC_STATUS
     */
    public String getSyncStatus() {
        return syncStatus;
    }

    /**
     * @param syncStatus
     */
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    /**
     * @return SYNC_RESULT
     */
    public String getSyncResult() {
        return syncResult;
    }

    /**
     * @param syncResult
     */
    public void setSyncResult(String syncResult) {
        this.syncResult = syncResult;
    }

    /**
     * @return DATAVERSION
     */
    public Short getDataversion() {
        return dataversion;
    }

    /**
     * @param dataversion
     */
    public void setDataversion(Short dataversion) {
        this.dataversion = dataversion;
    }

    /**
     * @return YLZD1
     */
    public String getYlzd1() {
        return ylzd1;
    }

    /**
     * @param ylzd1
     */
    public void setYlzd1(String ylzd1) {
        this.ylzd1 = ylzd1;
    }

    /**
     * @return YLZD2
     */
    public String getYlzd2() {
        return ylzd2;
    }

    /**
     * @param ylzd2
     */
    public void setYlzd2(String ylzd2) {
        this.ylzd2 = ylzd2;
    }

    /**
     * @return EXTEND
     */
    public String getExtend() {
        return extend;
    }

    /**
     * @param extend
     */
    public void setExtend(String extend) {
        this.extend = extend;
    }
}