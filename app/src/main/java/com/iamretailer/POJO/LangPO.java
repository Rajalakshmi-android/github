package com.iamretailer.POJO;

public class LangPO {

    String lang_id, lang_name, lang_code;
    boolean select_lang;

    public boolean isSelect_lang() {
        return select_lang;
    }

    public void setSelect_lang(boolean select_lang) {
        this.select_lang = select_lang;
    }

    public String getLang_id() {
        return lang_id;
    }

    public void setLang_id(String lang_id) {
        this.lang_id = lang_id;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }
}
