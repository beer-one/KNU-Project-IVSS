package com.example.youngjungoo.warningapp;


import java.net.URL;

public class ListViewItem {
    private URL url ;
    private String titleStr ;
    private String descStr ;

    public void setIcon(URL fileName) {
        url = fileName ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public URL getIcon() {
        return this.url ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}