package com.company.project.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CASI.JC20")
public class Jc20 {
    @Id
    @Column(name = "AAZ308")
    private Long aaz308;

    @Column(name = "AAZ002")
    private Long aaz002;

    @Column(name = "AAE002")
    private Integer aae002;

    @Column(name = "AAC001")
    private Long aac001;

    @Column(name = "AAZ159")
    private Long aaz159;

    @Column(name = "AAZ001")
    private Long aaz001;

    @Column(name = "EJC004")
    private String ejc004;

    @Column(name = "AJC093")
    private String ajc093;

    @Column(name = "AJC090")
    private Date ajc090;

    @Column(name = "EJC006")
    private Integer ejc006;

    @Column(name = "AJC092")
    private Integer ajc092;

    @Column(name = "EJC002")
    private BigDecimal ejc002;

    @Column(name = "EAC053")
    private String eac053;

    @Column(name = "AAE005")
    private String aae005;

    @Column(name = "AAE007")
    private String aae007;

    @Column(name = "AAE006")
    private String aae006;

    @Column(name = "EAB009")
    private String eab009;

    @Column(name = "EAB030")
    private String eab030;

    @Column(name = "AAE013")
    private String aae013;

    @Column(name = "AAE100")
    private String aae100;

    @Column(name = "PRSENO")
    private Long prseno;

    @Column(name = "AAE015")
    private Date aae015;

    @Column(name = "EJC001")
    private String ejc001;

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
     * @return AAZ002
     */
    public Long getAaz002() {
        return aaz002;
    }

    /**
     * @param aaz002
     */
    public void setAaz002(Long aaz002) {
        this.aaz002 = aaz002;
    }

    /**
     * @return AAE002
     */
    public Integer getAae002() {
        return aae002;
    }

    /**
     * @param aae002
     */
    public void setAae002(Integer aae002) {
        this.aae002 = aae002;
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
     * @return AAZ159
     */
    public Long getAaz159() {
        return aaz159;
    }

    /**
     * @param aaz159
     */
    public void setAaz159(Long aaz159) {
        this.aaz159 = aaz159;
    }

    /**
     * @return AAZ001
     */
    public Long getAaz001() {
        return aaz001;
    }

    /**
     * @param aaz001
     */
    public void setAaz001(Long aaz001) {
        this.aaz001 = aaz001;
    }

    /**
     * @return EJC004
     */
    public String getEjc004() {
        return ejc004;
    }

    /**
     * @param ejc004
     */
    public void setEjc004(String ejc004) {
        this.ejc004 = ejc004;
    }

    /**
     * @return AJC093
     */
    public String getAjc093() {
        return ajc093;
    }

    /**
     * @param ajc093
     */
    public void setAjc093(String ajc093) {
        this.ajc093 = ajc093;
    }

    /**
     * @return AJC090
     */
    public Date getAjc090() {
        return ajc090;
    }

    /**
     * @param ajc090
     */
    public void setAjc090(Date ajc090) {
        this.ajc090 = ajc090;
    }

    /**
     * @return EJC006
     */
    public Integer getEjc006() {
        return ejc006;
    }

    /**
     * @param ejc006
     */
    public void setEjc006(Integer ejc006) {
        this.ejc006 = ejc006;
    }

    /**
     * @return AJC092
     */
    public Integer getAjc092() {
        return ajc092;
    }

    /**
     * @param ajc092
     */
    public void setAjc092(Integer ajc092) {
        this.ajc092 = ajc092;
    }

    /**
     * @return EJC002
     */
    public BigDecimal getEjc002() {
        return ejc002;
    }

    /**
     * @param ejc002
     */
    public void setEjc002(BigDecimal ejc002) {
        this.ejc002 = ejc002;
    }

    /**
     * @return EAC053
     */
    public String getEac053() {
        return eac053;
    }

    /**
     * @param eac053
     */
    public void setEac053(String eac053) {
        this.eac053 = eac053;
    }

    /**
     * @return AAE005
     */
    public String getAae005() {
        return aae005;
    }

    /**
     * @param aae005
     */
    public void setAae005(String aae005) {
        this.aae005 = aae005;
    }

    /**
     * @return AAE007
     */
    public String getAae007() {
        return aae007;
    }

    /**
     * @param aae007
     */
    public void setAae007(String aae007) {
        this.aae007 = aae007;
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
     * @return EAB009
     */
    public String getEab009() {
        return eab009;
    }

    /**
     * @param eab009
     */
    public void setEab009(String eab009) {
        this.eab009 = eab009;
    }

    /**
     * @return EAB030
     */
    public String getEab030() {
        return eab030;
    }

    /**
     * @param eab030
     */
    public void setEab030(String eab030) {
        this.eab030 = eab030;
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
     * @return AAE015
     */
    public Date getAae015() {
        return aae015;
    }

    /**
     * @param aae015
     */
    public void setAae015(Date aae015) {
        this.aae015 = aae015;
    }

    /**
     * @return EJC001
     */
    public String getEjc001() {
        return ejc001;
    }

    /**
     * @param ejc001
     */
    public void setEjc001(String ejc001) {
        this.ejc001 = ejc001;
    }
}