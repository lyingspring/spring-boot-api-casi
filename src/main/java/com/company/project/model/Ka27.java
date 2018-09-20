package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "KA27")
public class Ka27 {
    @Id
    @Column(name = "AKA027")
    private Long aka027;

    @Column(name = "AAC001")
    private Long aac001;

    @Column(name = "AAC003")
    private String aac003;

    @Column(name = "AAE135")
    private String aae135;

    @Column(name = "AAC001_S")
    private Long aac001S;

    @Column(name = "AAC003_S")
    private String aac003S;

    @Column(name = "AAE135_S")
    private String aae135S;

    @Column(name = "AKA012")
    private String aka012;

    @Column(name = "AAE030")
    private Integer aae030;

    @Column(name = "AAE031")
    private Integer aae031;

    @Column(name = "AAE036")
    private Date aae036;

    @Column(name = "AAE100")
    private String aae100;

    @Column(name = "AAE013")
    private String aae013;

    /**
     * @return AKA027
     */
    public Long getAka027() {
        return aka027;
    }

    /**
     * @param aka027
     */
    public void setAka027(Long aka027) {
        this.aka027 = aka027;
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
     * @return AAC003
     */
    public String getAac003() {
        return aac003;
    }

    /**
     * @param aac003
     */
    public void setAac003(String aac003) {
        this.aac003 = aac003;
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
     * @return AAC001_S
     */
    public Long getAac001S() {
        return aac001S;
    }

    /**
     * @param aac001S
     */
    public void setAac001S(Long aac001S) {
        this.aac001S = aac001S;
    }

    /**
     * @return AAC003_S
     */
    public String getAac003S() {
        return aac003S;
    }

    /**
     * @param aac003S
     */
    public void setAac003S(String aac003S) {
        this.aac003S = aac003S;
    }

    /**
     * @return AAE135_S
     */
    public String getAae135S() {
        return aae135S;
    }

    /**
     * @param aae135S
     */
    public void setAae135S(String aae135S) {
        this.aae135S = aae135S;
    }

    /**
     * @return AKA012
     */
    public String getAka012() {
        return aka012;
    }

    /**
     * @param aka012
     */
    public void setAka012(String aka012) {
        this.aka012 = aka012;
    }

    /**
     * @return AAE030
     */
    public Integer getAae030() {
        return aae030;
    }

    /**
     * @param aae030
     */
    public void setAae030(Integer aae030) {
        this.aae030 = aae030;
    }

    /**
     * @return AAE031
     */
    public Integer getAae031() {
        return aae031;
    }

    /**
     * @param aae031
     */
    public void setAae031(Integer aae031) {
        this.aae031 = aae031;
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
     * @return AAE100
     */
    public String getAae100() {
        return aae100;
    }

    /**
     * @param aae100
     */
    public void setAae100(String aae100) {
        this.aae100 = aae100;
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
}