package com.iamretailer.POJO;

public class TypePO {
    String types;
    String color;
    String code;
    String title;
    int cat_id;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String amount;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProduct_img_url() {
        return product_img_url;
    }

    public void setProduct_img_url(String product_img_url) {
        this.product_img_url = product_img_url;
    }

    String product_img_url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private boolean imgSel;


    public boolean isImgSel() {
        return imgSel;
    }

    public void setImgSel(boolean imgSel) {
        this.imgSel = imgSel;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
