package com.company.project.model;

import javax.persistence.*;

@Table(name = "PRE_FORM_FILE")
public class PreFormFile {
    @Id
    @Column(name = "UNID")
    private String unid;

    @Column(name = "PROJID")
    private String projid;

    @Column(name = "BELONGTO")
    private String belongto;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILEPATH")
    private String filepath;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "BELONGSYSTEM")
    private String belongsystem;

    @Column(name = "CREATE_TIME")
    private String createTime;

    @Column(name = "SYNC_STATUS")
    private String syncStatus;

    @Column(name = "DATAVERSION")
    private Short dataversion;

    @Column(name = "FILEPWD")
    private String filepwd;

    @Column(name = "TAKETYPE")
    private String taketype;

    @Column(name = "ISTAKE")
    private String istake;

    @Column(name = "ATTRNAME")
    private String attrname;

    @Column(name = "CONTENT")
    private byte[] content;

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
     * @return BELONGTO
     */
    public String getBelongto() {
        return belongto;
    }

    /**
     * @param belongto
     */
    public void setBelongto(String belongto) {
        this.belongto = belongto;
    }

    /**
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return FILEPATH
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @param filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
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
     * @return FILEPWD
     */
    public String getFilepwd() {
        return filepwd;
    }

    /**
     * @param filepwd
     */
    public void setFilepwd(String filepwd) {
        this.filepwd = filepwd;
    }

    /**
     * @return TAKETYPE
     */
    public String getTaketype() {
        return taketype;
    }

    /**
     * @param taketype
     */
    public void setTaketype(String taketype) {
        this.taketype = taketype;
    }

    /**
     * @return ISTAKE
     */
    public String getIstake() {
        return istake;
    }

    /**
     * @param istake
     */
    public void setIstake(String istake) {
        this.istake = istake;
    }

    /**
     * @return ATTRNAME
     */
    public String getAttrname() {
        return attrname;
    }

    /**
     * @param attrname
     */
    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    /**
     * @return CONTENT
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
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