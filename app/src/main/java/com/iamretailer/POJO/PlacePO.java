package com.iamretailer.POJO;

import java.util.ArrayList;

public class PlacePO {
    int place_img;
    String product_name;
    String qty;
    String key;
    String product_id;
    String model;
    String total_amt;
    String price;
    String order_id;
    String payment;
    String tot_title;
    String tot_amt_txt;
    String date;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    String command;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<OptionsPO> getOptionlist() {
        return optionlist;
    }

    public void setOptionlist(ArrayList<OptionsPO> optionlist) {
        this.optionlist = optionlist;
    }

    ArrayList<OptionsPO> optionlist;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTot_title() {
        return tot_title;
    }

    public void setTot_title(String tot_title) {
        this.tot_title = tot_title;
    }

    public String getTot_amt_txt() {
        return tot_amt_txt;
    }

    public void setTot_amt_txt(String tot_amt_txt) {
        this.tot_amt_txt = tot_amt_txt;
    }

    public String getPrcie() {
        return price;
    }

    public void setPrcie(String prcie) {
        this.price = prcie;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAmout() {
        return total_amt;
    }

    public void setAmout(String amout) {
        this.total_amt = amout;
    }

    public int getPlace_img() {
        return place_img;
    }

    public void setPlace_img(int place_img) {
        this.place_img = place_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    int product_option_id, option_id;
    String type, value, required;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getProduct_option_id() {
        return product_option_id;
    }

    public void setProduct_option_id(int product_option_id) {
        this.product_option_id = product_option_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    ArrayList<SingleOptionPO> valuelist;
    ArrayList<PlacePO> valuelists;

    public ArrayList<PlacePO> getValuelists() {
        return valuelists;
    }

    public void setValuelists(ArrayList<PlacePO> valuelists) {
        this.valuelists = valuelists;
    }
}
