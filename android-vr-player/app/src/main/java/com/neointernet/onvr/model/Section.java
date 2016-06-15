package com.neointernet.onvr.model;

/**
 * Created by HSH on 16. 6. 8..
 */
public class Section {

    private String title;
    private int position;

    public Section(String title, int position) {
        this.title = title;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }
}
