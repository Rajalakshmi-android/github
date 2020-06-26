package com.iamretailer.krishnasupermarket.POJO;

import java.util.ArrayList;

public class BrandsPO extends ArrayList<BrandsPO> {
    int bg_img,logo_img;
    String bg_img_url,s_id,store_name,sel_name,sel_address,sel_country,sell_id,product_total;
    String logo_img_url;
    boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    ArrayList<BrandsPO> arrayList;
    ArrayList<ProductsPO> productslist;

    public ArrayList<ProductsPO> getProductslist() {
        return productslist;
    }

    public void setProductslist(ArrayList<ProductsPO> productslist) {
        this.productslist = productslist;
    }

    public ArrayList<BrandsPO> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<BrandsPO> arrayList) {
        this.arrayList = arrayList;
    }

    public String getLogo_img_url() {
        return logo_img_url;
    }

    public void setLogo_img_url(String logo_img_url) {
        this.logo_img_url = logo_img_url;
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

    public String getSel_name() {
        return sel_name;
    }

    public void setSel_name(String sel_name) {
        this.sel_name = sel_name;
    }

    public String getSel_address() {
        return sel_address;
    }

    public void setSel_address(String sel_address) {
        this.sel_address = sel_address;
    }

    public String getSel_country() {
        return sel_country;
    }

    public void setSel_country(String sel_country) {
        this.sel_country = sel_country;
    }

    public String getSell_id() {
        return sell_id;
    }

    public void setSell_id(String sell_id) {
        this.sell_id = sell_id;
    }

    public String getProduct_total() {
        return product_total;
    }

    public void setProduct_total(String product_total) {
        this.product_total = product_total;
    }

    public int getBg_img() {
        return bg_img;
    }

    public void setBg_img(int bg_img) {
        this.bg_img = bg_img;
    }

    public int getLogo_img() {
        return logo_img;
    }

    public void setLogo_img(int logo_img) {
        this.logo_img = logo_img;
    }

    String fb,mail,twitter,desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
