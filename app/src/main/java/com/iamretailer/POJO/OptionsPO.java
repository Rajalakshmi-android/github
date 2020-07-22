package com.iamretailer.POJO;

import java.util.ArrayList;
import java.io.Serializable;

public class OptionsPO implements Serializable {

    int selected_id;
    int qty;
    ArrayList<OptionsPO> pos;
    double offer_rate;
    String title;
    int id;
    String prefix,weight,manufact;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public double getPrices() {
        return prices;
    }

    public void setPrices(double prices) {
        this.prices = prices;
    }

    double prices;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    ArrayList<SingleOptionPO> singleOptionPOS;

    public ArrayList<SingleOptionPO> getSingleOptionPOS() {
        return singleOptionPOS;
    }

    public void setSingleOptionPOS(ArrayList<SingleOptionPO> singleOptionPOS) {
        this.singleOptionPOS = singleOptionPOS;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    boolean wish_list;
    boolean select;
    boolean cart_list;

    public boolean isCart_list() {
        return cart_list;
    }

    public void setCart_list(boolean cart_list) {
        this.cart_list = cart_list;
    }

    public boolean isWish_list() {
        return wish_list;
    }

    public void setWish_list(boolean wish_list) {
        this.wish_list = wish_list;
    }

    public double getOffer_rate() {
        return offer_rate;
    }

    public void setOffer_rate(double offer_rate) {
        this.offer_rate = offer_rate;
    }

    public ArrayList<OptionsPO> getPos() {
        return pos;
    }

    public void setPos(ArrayList<OptionsPO> pos) {
        this.pos = pos;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(int selected_id) {
        this.selected_id = selected_id;
    }

    public ArrayList<SingleOptionPO> getValuelist() {
        return valuelist;
    }

    public void setValuelist(ArrayList<SingleOptionPO> valuelist) {
        this.valuelist = valuelist;
    }

    ArrayList<SingleOptionPO> valuelist;
   ArrayList<OptionsPO> valuelists;

    public ArrayList<OptionsPO> getValuelists() {
        return valuelists;
    }

    public void setValuelists(ArrayList<OptionsPO> valuelists) {
        this.valuelists = valuelists;
    }

    String name,type,value,required;
    int product_option_id,option_id;

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
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


    String p_id;
    String price;
    String p_name;
    String productrate;
    String productoffer;
    String description;
    String image;
    String model,url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    int cat_id,parent_id;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    String tax;
    String product_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getProductrate() {
        return productrate;
    }

    public void setProductrate(String productrate) {
        this.productrate = productrate;
    }

    public String getProductoffer() {
        return productoffer;
    }

    public void setProductoffer(String productoffer) {
        this.productoffer = productoffer;
    }

    public double getRates() {
        return rates;
    }

    public void setRates(double rates) {
        this.rates = rates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    double rate,rates;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
