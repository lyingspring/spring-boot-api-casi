package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "DATASHAINGTRANSLOG")
public class Datashaingtranslog {
    @Id
    @Column(name = "SNO")
    private Long sno;

    @Column(name = "TRANSID")
    private String transid;

    @Column(name = "SENDCODE")
    private String sendcode;

    @Column(name = "SENDNODECODE")
    private String sendnodecode;

    @Column(name = "RECEIVECODE")
    private String receivecode;

    @Column(name = "RECEIVENODECODE")
    private String receivenodecode;

    @Column(name = "ITRADETYPE")
    private String itradetype;

    @Column(name = "RESULTS")
    private String results;

    @Column(name = "STARTTIME")
    private Date starttime;

    @Column(name = "UPDATETIME")
    private Date updatetime;

    @Column(name = "AAE013")
    private String aae013;

    @Column(name = "REQUEST")
    private String request;

    @Column(name = "VARIN")
    private String varin;

    @Column(name = "VAROUT")
    private String varout;

    /**
     * @return SNO
     */
    public Long getSno() {
        return sno;
    }

    /**
     * @param sno
     */
    public void setSno(Long sno) {
        this.sno = sno;
    }

    /**
     * @return TRANSID
     */
    public String getTransid() {
        return transid;
    }

    /**
     * @param transid
     */
    public void setTransid(String transid) {
        this.transid = transid;
    }

    /**
     * @return SENDCODE
     */
    public String getSendcode() {
        return sendcode;
    }

    /**
     * @param sendcode
     */
    public void setSendcode(String sendcode) {
        this.sendcode = sendcode;
    }

    /**
     * @return SENDNODECODE
     */
    public String getSendnodecode() {
        return sendnodecode;
    }

    /**
     * @param sendnodecode
     */
    public void setSendnodecode(String sendnodecode) {
        this.sendnodecode = sendnodecode;
    }

    /**
     * @return RECEIVECODE
     */
    public String getReceivecode() {
        return receivecode;
    }

    /**
     * @param receivecode
     */
    public void setReceivecode(String receivecode) {
        this.receivecode = receivecode;
    }

    /**
     * @return RECEIVENODECODE
     */
    public String getReceivenodecode() {
        return receivenodecode;
    }

    /**
     * @param receivenodecode
     */
    public void setReceivenodecode(String receivenodecode) {
        this.receivenodecode = receivenodecode;
    }

    /**
     * @return ITRADETYPE
     */
    public String getItradetype() {
        return itradetype;
    }

    /**
     * @param itradetype
     */
    public void setItradetype(String itradetype) {
        this.itradetype = itradetype;
    }

    /**
     * @return RESULTS
     */
    public String getResults() {
        return results;
    }

    /**
     * @param results
     */
    public void setResults(String results) {
        this.results = results;
    }

    /**
     * @return STARTTIME
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * @param starttime
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * @return UPDATETIME
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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
     * @return REQUEST
     */
    public String getRequest() {
        return request;
    }

    /**
     * @param request
     */
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * @return VARIN
     */
    public String getVarin() {
        return varin;
    }

    /**
     * @param varin
     */
    public void setVarin(String varin) {
        this.varin = varin;
    }

    /**
     * @return VAROUT
     */
    public String getVarout() {
        return varout;
    }

    /**
     * @param varout
     */
    public void setVarout(String varout) {
        this.varout = varout;
    }
}