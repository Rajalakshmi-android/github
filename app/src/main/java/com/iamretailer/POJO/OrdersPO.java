package com.iamretailer.POJO;

public class OrdersPO {
    String order_id, order_name, order_status, order_date, order_products, order_total, order_cur, order_cur_val, order_total_raw, order_time,geocode,latitude,langtitude;
    String cur_id, sym_left, sym_right, decimal, value, quantity;

    public String getPayment() {
        return payment;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLangtitude() {
        return langtitude;
    }

    public void setLangtitude(String langtitude) {
        this.langtitude = langtitude;
    }

    public String getGuestval() {
        return guestval;
    }

    public void setGuestval(String guestval) {
        this.guestval = guestval;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    String payment,return_id,guestval;

    public String getReturn_id() {
        return return_id;
    }

    public void setReturn_id(String return_id) {
        this.return_id = return_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_products() {
        return order_products;
    }

    public void setOrder_products(String order_products) {
        this.order_products = order_products;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_cur() {
        return order_cur;
    }

    public void setOrder_cur(String order_cur) {
        this.order_cur = order_cur;
    }

    public String getOrder_cur_val() {
        return order_cur_val;
    }

    public void setOrder_cur_val(String order_cur_val) {
        this.order_cur_val = order_cur_val;
    }

    public String getOrder_total_raw() {
        return order_total_raw;
    }

    public void setOrder_total_raw(String order_total_raw) {
        this.order_total_raw = order_total_raw;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getCur_id() {
        return cur_id;
    }

    public void setCur_id(String cur_id) {
        this.cur_id = cur_id;
    }

    public String getSym_left() {
        return sym_left;
    }

    public void setSym_left(String sym_left) {
        this.sym_left = sym_left;
    }

    public String getSym_right() {
        return sym_right;
    }

    public void setSym_right(String sym_right) {
        this.sym_right = sym_right;
    }

    public String getDecimal() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
