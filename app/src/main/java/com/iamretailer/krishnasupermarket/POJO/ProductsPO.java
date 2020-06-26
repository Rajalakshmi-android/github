package com.iamretailer.krishnasupermarket.POJO;

import java.util.ArrayList;
import java.io.Serializable;

public class ProductsPO implements Serializable {
    String offer,weight,manufact;
    String product_name;
    double prod_offer_rate;
    double total;
    double prod_original_rate;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getManufact() {
        return manufact;
    }

    public void setManufact(String manufact) {
        this.manufact = manufact;
    }

    String cart_war;
    int cartvalue;
    String cart_brand_name;
    String producturl;
    int productid;
    boolean out_of_stock;
    String off_price,mrp;
    ArrayList<SingleOptionPO> singleOptionPOS;

    public ArrayList<SingleOptionPO> getSingleOptionPOS() {
        return singleOptionPOS;
    }

    public void setSingleOptionPOS(ArrayList<SingleOptionPO> singleOptionPOS) {
        this.singleOptionPOS = singleOptionPOS;
    }

    public boolean isWish_list() {
        return wish_list;
    }

    public void setWish_list(boolean wish_list) {
        this.wish_list = wish_list;
    }

    boolean wish_list;
    boolean cart_list;

    public boolean isCart_list() {
        return cart_list;
    }

    public void setCart_list(boolean cart_list) {
        this.cart_list = cart_list;
    }

    public String getOff_price() {
        return off_price;
    }

    public void setOff_price(String off_price) {
        this.off_price = off_price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public boolean isOut_of_stock() {
        return out_of_stock;
    }

    public void setOut_of_stock(boolean out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public double getP_rate() {
        return p_rate;
    }

    public void setP_rate(double p_rate) {
        this.p_rate = p_rate;
    }

    double p_rate;
    String key;
    int qty;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    double amount;
    double price;

ArrayList<OptionsPO> optionlist;

    public ArrayList<OptionsPO> getOptionlist() {
        return optionlist;
    }

    public void setOptionlist(ArrayList<OptionsPO> optionlist) {
        this.optionlist = optionlist;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }





    String product_id;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    String min;

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    boolean special;


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getCartvalue() {
        return cartvalue;
    }

    public void setCartvalue(int cartvalue) {
        this.cartvalue = cartvalue;
    }

    public String getCart_war() {
        return cart_war;
    }

    public void setCart_war(String cart_war) {
        this.cart_war = cart_war;
    }

    public String getCart_brand_name() {
        return cart_brand_name;
    }

    public void setCart_brand_name(String cart_brand_name) {
        this.cart_brand_name = cart_brand_name;
    }



    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }



    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getProd_offer_rate() {
        return prod_offer_rate;
    }

    public void setProd_offer_rate(double prod_offer_rate) {
        this.prod_offer_rate = prod_offer_rate;
    }

    public double getProd_original_rate() {
        return prod_original_rate;
    }

    public void setProd_original_rate(double prod_original_rate) {
        this.prod_original_rate = prod_original_rate;
    }


}
