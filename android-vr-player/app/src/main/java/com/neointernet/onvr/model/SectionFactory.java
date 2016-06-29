package com.neointernet.onvr.model;

import java.util.ArrayList;

/**
 * Created by HSH on 16. 6. 9..
 */
public class SectionFactory {

    private ArrayList<Section> sectionArrayList;

    public SectionFactory() {
        sectionArrayList = new ArrayList<>();
        sectionArrayList.add(new Section("entertainment", 1, "엔터테인먼트"));
        sectionArrayList.add(new Section("event", 4, "이벤트"));
        sectionArrayList.add(new Section("extreme", 7, "익스트림"));
        sectionArrayList.add(new Section("musicvideo", 10, "뮤직비디오"));
        sectionArrayList.add(new Section("game", 13, "게임"));
        sectionArrayList.add(new Section("location", 16, "장소"));
    }

    public ArrayList<Section> getSectionArrayList() {
        return sectionArrayList;
    }
}
