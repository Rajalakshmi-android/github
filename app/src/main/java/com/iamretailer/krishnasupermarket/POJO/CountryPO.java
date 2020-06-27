package com.iamretailer.krishnasupermarket.POJO;

public class CountryPO {

    String cont_id,count_name,count_iso_code_2,count_iso_code_3,zone_id;
    int count_id;

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public int getCount_id() {
        return count_id;
    }

    public void setCount_id(int count_id) {
        this.count_id = count_id;
    }

    public String getCont_id() {
        return cont_id;
    }

    public void setCont_id(String cont_id) {
        this.cont_id = cont_id;
    }

    public String getCount_name() {
        return count_name;
    }

    public void setCount_name(String count_name) {
        this.count_name = count_name;
    }

    public String getCount_iso_code_2() {
        return count_iso_code_2;
    }

    public void setCount_iso_code_2(String count_iso_code_2) {
        this.count_iso_code_2 = count_iso_code_2;
    }

    public String getCount_iso_code_3() {
        return count_iso_code_3;
    }

    public void setCount_iso_code_3(String count_iso_code_3) {
        this.count_iso_code_3 = count_iso_code_3;
    }
}
