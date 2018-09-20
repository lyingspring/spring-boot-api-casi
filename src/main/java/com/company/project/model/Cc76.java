package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "CC76")
public class Cc76 {
    @Id
    @Column(name = "AAZ308")
    private Long aaz308;

    @Column(name = "AAC001")
    private Long aac001;

    @Column(name = "AAE135")
    private String aae135;

    @Column(name = "AAC009")
    private String aac009;

    @Column(name = "AAC011")
    private String aac011;

    @Column(name = "AAE006")
    private String aae006;

    @Column(name = "EAC101")
    private String eac101;

    @Column(name = "GZDD")
    private String gzdd;

    @Column(name = "BDC033")
    private String bdc033;

    @Column(name = "PROJID")
    private String projid;

    @Column(name = "AAE036")
    private Date aae036;

    @Column(name = "AAE013")
    private String aae013;

    @Column(name = "FLAG")
    private String flag;

    /**
     * @return AAZ308
     */
    public Long getAaz308() {
        return aaz308;
    }

    /**
     * @param aaz308
     */
    public void setAaz308(Long aaz308) {
        this.aaz308 = aaz308;
    }

    /**
     * @return AAC001
     */
    public Long getAac001() {
        return aac001;
    }

    /**
     * @param aac001
     */
    public void setAac001(Long aac001) {
        this.aac001 = aac001;
    }

    /**
     * @return AAE135
     */
    public String getAae135() {
        return aae135;
    }

    /**
     * @param aae135
     */
    public void setAae135(String aae135) {
        this.aae135 = aae135;
    }

    /**
     * @return AAC009
     */
    public String getAac009() {
        return aac009;
    }

    /**
     * @param aac009
     */
    public void setAac009(String aac009) {
        this.aac009 = aac009;
    }

    /**
     * @return AAC011
     */
    public String getAac011() {
        return aac011;
    }

    /**
     * @param aac011
     */
    public void setAac011(String aac011) {
        this.aac011 = aac011;
    }

    /**
     * @return AAE006
     */
    public String getAae006() {
        return aae006;
    }

    /**
     * @param aae006
     */
    public void setAae006(String aae006) {
        this.aae006 = aae006;
    }

    /**
     * @return EAC101
     */
    public String getEac101() {
        return eac101;
    }

    /**
     * @param eac101
     */
    public void setEac101(String eac101) {
        this.eac101 = eac101;
    }

    /**
     * @return GZDD
     */
    public String getGzdd() {
        return gzdd;
    }

    /**
     * @param gzdd
     */
    public void setGzdd(String gzdd) {
        this.gzdd = gzdd;
    }

    /**
     * @return BDC033
     */
    public String getBdc033() {
        return bdc033;
    }

    /**
     * @param bdc033
     */
    public void setBdc033(String bdc033) {
        this.bdc033 = bdc033;
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
     * @return AAE036
     */
    public Date getAae036() {
        return aae036;
    }

    /**
     * @param aae036
     */
    public void setAae036(Date aae036) {
        this.aae036 = aae036;
    }

    /**
     * @return AAE013
     */
    public String getAae013() {
        return aae013;
    }

    /**
     * @param aae013
     */
    public void setAae013(String aae013) {
        this.aae013 = aae013;
    }

    /**
     * @return FLAG
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @param flag
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }
}