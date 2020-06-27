package com.iamretailer.krishnasupermarket.POJO;

public class SingleOptionPO {
    String imga_url;
    String name;
    String price_format;

    int product_option_value_id;
    int option_value_id;
    int quantity;
    double price;
    double price_tax;
    private boolean imgSel;
    String type;
    String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String rev_author,rev_text,rev_date;
    int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRev_author() {
        return rev_author;
    }

    public void setRev_author(String rev_author) {
        this.rev_author = rev_author;
    }

    public String getRev_text() {
        return rev_text;
    }

    public void setRev_text(String rev_text) {
        this.rev_text = rev_text;
    }


    public String getRev_date() {
        return rev_date;
    }

    public void setRev_date(String rev_date) {
        this.rev_date = rev_date;
    }

    public boolean isImgSel() {
        return imgSel;
    }

    public void setImgSel(boolean imgSel) {
        this.imgSel = imgSel;
    }

    public double getPrice_tax() {
        return price_tax;
    }

    public void setPrice_tax(double price_tax) {
        this.price_tax = price_tax;
    }



    public String getImga_url() {
        return imga_url;
    }

    public void setImga_url(String imga_url) {
        this.imga_url = imga_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProduct_option_value_id() {
        return product_option_value_id;
    }

    public void setProduct_option_value_id(int product_option_value_id) {
        this.product_option_value_id = product_option_value_id;
    }

    public int getOption_value_id() {
        return option_value_id;
    }

    public void setOption_value_id(int option_value_id) {
        this.option_value_id = option_value_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getPrice_format() {
        return price_format;
    }

    public void setPrice_format(String price_format) {
        this.price_format = price_format;
    }




}
