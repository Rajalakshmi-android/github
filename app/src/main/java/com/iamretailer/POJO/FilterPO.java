package com.iamretailer.POJO;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterPO implements Serializable {

    String filter_name;
    boolean selected;
    ArrayList<FilterPO> filterPOS;

    String sub_id,main_id;
    float price_values;

    public float getPrice_values() {
        return price_values;
    }

    public void setPrice_values(float price_values) {
        this.price_values = price_values;
    }

    public float getPrice_values0() {
        return price_values0;
    }

    public void setPrice_values0(float price_values0) {
        this.price_values0 = price_values0;
    }

    float seek_min,seek_max;

    public float getSeek_min() {
        return seek_min;
    }

    public void setSeek_min(float seek_min) {
        this.seek_min = seek_min;
    }

    public float getSeek_max() {
        return seek_max;
    }

    public void setSeek_max(float seek_max) {
        this.seek_max = seek_max;
    }

    float price_values0;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public ArrayList<FilterPO> getFilterPOS() {
        return filterPOS;
    }

    public void setFilterPOS(ArrayList<FilterPO> filterPOS) {
        this.filterPOS = filterPOS;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
