package com.neointernet.onvr.model;

/**
 * Created by HSH on 16. 6. 8..
 */
public class Section {

    private String title;
    private String korTitle;
    private int position;

    public Section(String title, int position, String korTitle) {
        this.title = title;
        this.position = position;
        this.korTitle = korTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getKorTitle() {
        return korTitle;
    }

    public int getPosition() {
        return position;
    }
}
