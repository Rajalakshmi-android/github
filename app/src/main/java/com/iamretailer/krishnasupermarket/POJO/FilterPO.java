package com.iamretailer.krishnasupermarket.POJO;

import java.util.ArrayList;

public class FilterPO {

    String filter_name;
    boolean selected;
    ArrayList<FilterPO> filterPOS;

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
