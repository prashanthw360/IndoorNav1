package com.example.indoornav1;

public class Coupons {

    private String ad, ctitle, cdesc;

    public Coupons(String ctitle, String cdesc) {
        this.ctitle = ctitle;
        this.cdesc = cdesc;
    }


    public String getCtitle() {
        return ctitle;
    }

    public String getCdesc() {
        return cdesc;
    }



    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }
}
