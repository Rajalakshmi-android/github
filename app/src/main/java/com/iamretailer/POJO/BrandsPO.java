package com.iamretailer.POJO;

import java.util.ArrayList;

public class BrandsPO extends ArrayList<BrandsPO> {
    String bg_img_url, s_id, store_name;
    boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    ArrayList<BrandsPO> arrayList;
    ArrayList<BrandsPO> arrayList1;

    public ArrayList<BrandsPO> getArrayList1() {
        return arrayList1;
    }

    public void setArrayList1(ArrayList<BrandsPO> arrayList1) {
        this.arrayList1 = arrayList1;
    }

    public ArrayList<BrandsPO> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<BrandsPO> arrayList) {
        this.arrayList = arrayList;
    }

    public String getBg_img_url() {
        return bg_img_url;
    }

    public void setBg_img_url(String bg_img_url) {
        this.bg_img_url = bg_img_url;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }


    String fb;

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }


}
