package com.company.project.model;

import javax.persistence.*;

@Table(name = "AB51")
public class Ab51 {
    @Id
    @Column(name = "AAC001")
    private Long aac001;

    @Column(name = "AAZ067")
    private Long aaz067;

    @Column(name = "AAC069")
    private String aac069;

    @Column(name = "AAE030")
    private Integer aae030;

    @Column(name = "PRSENO")
    private Long prseno;

    @Column(name = "AAC060")
    private String aac060;

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
     * @return AAZ067
     */
    public Long getAaz067() {
        return aaz067;
    }

    /**
     * @param aaz067
     */
    public void setAaz067(Long aaz067) {
        this.aaz067 = aaz067;
    }

    /**
     * @return AAC069
     */
    public String getAac069() {
        return aac069;
    }

    /**
     * @param aac069
     */
    public void setAac069(String aac069) {
        this.aac069 = aac069;
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
     * @return PRSENO
     */
    public Long getPrseno() {
        return prseno;
    }

    /**
     * @param prseno
     */
    public void setPrseno(Long prseno) {
        this.prseno = prseno;
    }

    /**
     * @return AAC060
     */
    public String getAac060() {
        return aac060;
    }

    /**
     * @param aac060
     */
    public void setAac060(String aac060) {
        this.aac060 = aac060;
    }
}