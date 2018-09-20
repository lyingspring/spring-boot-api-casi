package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "PRE_APASINFO")
public class PreApasinfo {
    @Id
    @Column(name = "PROJID")
    private String projid;


    @Column(name = "DATAVERSION")
    private Short dataversion;

    @Column(name = "PROJPWD")
    private String projpwd;

    @Column(name = "SERVICECODE")
    private String servicecode;

    @Column(name = "SERVICEVERSION")
    private Short serviceversion;

    @Column(name = "SERVICENAME")
    private String servicename;

    @Column(name = "PROJECTNAME")
    private String projectname;

    @Column(name = "INFOTYPE")
    private String infotype;

    @Column(name = "BUS_TYPE")
    private String busType;

    @Column(name = "REL_BUS_ID")
    private String relBusId;

    @Column(name = "APPLYNAME")
    private String applyname;

    @Column(name = "APPLY_CARDTYPE")
    private String applyCardtype;

    @Column(name = "APPLY_CARDNUMBER")
    private String applyCardnumber;

    @Column(name = "CONTACTMAN")
    private String contactman;

    @Column(name = "CONTACTMAN_CARDTYPE")
    private String contactmanCardtype;

    @Column(name = "CONTACTMAN_CARDNUMBER")
    private String contactmanCardnumber;

    @Column(name = "TELPHONE")
    private String telphone;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LEGALMAN")
    private String legalman;

    @Column(name = "DEPTID")
    private String deptid;

    @Column(name = "DEPTNAME")
    private String deptname;

    @Column(name = "SS_ORGCODE")
    private String ssOrgcode;

    @Column(name = "RECEIVE_USEID")
    private String receiveUseid;

    @Column(name = "RECEIVE_NAME")
    private String receiveName;

    @Column(name = "APPLYFROM")
    private String applyfrom;

    @Column(name = "APPROVE_TYPE")
    private String approveType;

    @Column(name = "APPLY_PROPERTIY")
    private String applyPropertiy;

    @Column(name = "RECEIVETIME")
    private String receivetime;

    @Column(name = "BELONGTO")
    private String belongto;

    @Column(name = "AREACODE")
    private String areacode;

    @Column(name = "DATASTATE")
    private String datastate;

    @Column(name = "BELONGSYSTEM")
    private String belongsystem;

    @Column(name = "CREATE_TIME")
    private String createTime;

    @Column(name = "SYNC_STATUS")
    private String syncStatus;

    @Column(name = "SRC_IN_TIME")
    private Date srcInTime;

    @Column(name = "YWZT")
    private String ywzt;

    @Column(name = "CHC001")
    private String chc001;

    @Column(name = "EXTEND")
    private String extend;

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
     * @return PROJPWD
     */
    public String getProjpwd() {
        return projpwd;
    }

    /**
     * @param projpwd
     */
    public void setProjpwd(String projpwd) {
        this.projpwd = projpwd;
    }

    /**
     * @return SERVICECODE
     */
    public String getServicecode() {
        return servicecode;
    }

    /**
     * @param servicecode
     */
    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    /**
     * @return SERVICEVERSION
     */
    public Short getServiceversion() {
        return serviceversion;
    }

    /**
     * @param serviceversion
     */
    public void setServiceversion(Short serviceversion) {
        this.serviceversion = serviceversion;
    }

    /**
     * @return SERVICENAME
     */
    public String getServicename() {
        return servicename;
    }

    /**
     * @param servicename
     */
    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    /**
     * @return PROJECTNAME
     */
    public String getProjectname() {
        return projectname;
    }

    /**
     * @param projectname
     */
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    /**
     * @return INFOTYPE
     */
    public String getInfotype() {
        return infotype;
    }

    /**
     * @param infotype
     */
    public void setInfotype(String infotype) {
        this.infotype = infotype;
    }

    /**
     * @return BUS_TYPE
     */
    public String getBusType() {
        return busType;
    }

    /**
     * @param busType
     */
    public void setBusType(String busType) {
        this.busType = busType;
    }

    /**
     * @return REL_BUS_ID
     */
    public String getRelBusId() {
        return relBusId;
    }

    /**
     * @param relBusId
     */
    public void setRelBusId(String relBusId) {
        this.relBusId = relBusId;
    }

    /**
     * @return APPLYNAME
     */
    public String getApplyname() {
        return applyname;
    }

    /**
     * @param applyname
     */
    public void setApplyname(String applyname) {
        this.applyname = applyname;
    }

    /**
     * @return APPLY_CARDTYPE
     */
    public String getApplyCardtype() {
        return applyCardtype;
    }

    /**
     * @param applyCardtype
     */
    public void setApplyCardtype(String applyCardtype) {
        this.applyCardtype = applyCardtype;
    }

    /**
     * @return APPLY_CARDNUMBER
     */
    public String getApplyCardnumber() {
        return applyCardnumber;
    }

    /**
     * @param applyCardnumber
     */
    public void setApplyCardnumber(String applyCardnumber) {
        this.applyCardnumber = applyCardnumber;
    }

    /**
     * @return CONTACTMAN
     */
    public String getContactman() {
        return contactman;
    }

    /**
     * @param contactman
     */
    public void setContactman(String contactman) {
        this.contactman = contactman;
    }

    /**
     * @return CONTACTMAN_CARDTYPE
     */
    public String getContactmanCardtype() {
        return contactmanCardtype;
    }

    /**
     * @param contactmanCardtype
     */
    public void setContactmanCardtype(String contactmanCardtype) {
        this.contactmanCardtype = contactmanCardtype;
    }

    /**
     * @return CONTACTMAN_CARDNUMBER
     */
    public String getContactmanCardnumber() {
        return contactmanCardnumber;
    }

    /**
     * @param contactmanCardnumber
     */
    public void setContactmanCardnumber(String contactmanCardnumber) {
        this.contactmanCardnumber = contactmanCardnumber;
    }

    /**
     * @return TELPHONE
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * @param telphone
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    /**
     * @return POSTCODE
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return ADDRESS
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return LEGALMAN
     */
    public String getLegalman() {
        return legalman;
    }

    /**
     * @param legalman
     */
    public void setLegalman(String legalman) {
        this.legalman = legalman;
    }

    /**
     * @return DEPTID
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * @param deptid
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    /**
     * @return DEPTNAME
     */
    public String getDeptname() {
        return deptname;
    }

    /**
     * @param deptname
     */
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    /**
     * @return SS_ORGCODE
     */
    public String getSsOrgcode() {
        return ssOrgcode;
    }

    /**
     * @param ssOrgcode
     */
    public void setSsOrgcode(String ssOrgcode) {
        this.ssOrgcode = ssOrgcode;
    }

    /**
     * @return RECEIVE_USEID
     */
    public String getReceiveUseid() {
        return receiveUseid;
    }

    /**
     * @param receiveUseid
     */
    public void setReceiveUseid(String receiveUseid) {
        this.receiveUseid = receiveUseid;
    }

    /**
     * @return RECEIVE_NAME
     */
    public String getReceiveName() {
        return receiveName;
    }

    /**
     * @param receiveName
     */
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    /**
     * @return APPLYFROM
     */
    public String getApplyfrom() {
        return applyfrom;
    }

    /**
     * @param applyfrom
     */
    public void setApplyfrom(String applyfrom) {
        this.applyfrom = applyfrom;
    }

    /**
     * @return APPROVE_TYPE
     */
    public String getApproveType() {
        return approveType;
    }

    /**
     * @param approveType
     */
    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    /**
     * @return APPLY_PROPERTIY
     */
    public String getApplyPropertiy() {
        return applyPropertiy;
    }

    /**
     * @param applyPropertiy
     */
    public void setApplyPropertiy(String applyPropertiy) {
        this.applyPropertiy = applyPropertiy;
    }

    /**
     * @return RECEIVETIME
     */
    public String getReceivetime() {
        return receivetime;
    }

    /**
     * @param receivetime
     */
    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
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
     * @return AREACODE
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * @param areacode
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    /**
     * @return DATASTATE
     */
    public String getDatastate() {
        return datastate;
    }

    /**
     * @param datastate
     */
    public void setDatastate(String datastate) {
        this.datastate = datastate;
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
     * @return SRC_IN_TIME
     */
    public Date getSrcInTime() {
        return srcInTime;
    }

    /**
     * @param srcInTime
     */
    public void setSrcInTime(Date srcInTime) {
        this.srcInTime = srcInTime;
    }

    /**
     * @return YWZT
     */
    public String getYwzt() {
        return ywzt;
    }

    /**
     * @param ywzt
     */
    public void setYwzt(String ywzt) {
        this.ywzt = ywzt;
    }

    /**
     * @return CHC001
     */
    public String getChc001() {
        return chc001;
    }

    /**
     * @param chc001
     */
    public void setChc001(String chc001) {
        this.chc001 = chc001;
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