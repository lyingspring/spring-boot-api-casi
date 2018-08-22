package com.company.project.model;

import java.util.List;

public class BasicinfoDTO {
    private PaybankDTO paybankdto;
    private List<Aa05DTO> listaa05;
    private String aae013;
    private String aae014;

    public PaybankDTO getPaybankdto() {
        return paybankdto;
    }

    public void setPaybankdto(PaybankDTO paybankdto) {
        this.paybankdto = paybankdto;
    }

    public List<Aa05DTO> getListaa05() {
        return listaa05;
    }

    public void setListaa05(List<Aa05DTO> listaa05) {
        this.listaa05 = listaa05;
    }

    public String getAae013() {
        return aae013;
    }

    public void setAae013(String aae013) {
        this.aae013 = aae013;
    }

    public String getAae014() {
        return aae014;
    }

    public void setAae014(String aae014) {
        this.aae014 = aae014;
    }


}
