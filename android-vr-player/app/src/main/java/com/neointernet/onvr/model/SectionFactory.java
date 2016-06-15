package com.neointernet.onvr.model;

import java.util.ArrayList;

/**
 * Created by HSH on 16. 6. 9..
 */
public class SectionFactory {

    private ArrayList<Section> sectionArrayList;

    public SectionFactory() {
        sectionArrayList = new ArrayList<>();
        sectionArrayList.add(new Section("엔터테인먼트", 1));
        sectionArrayList.add(new Section("이벤트", 4));
        sectionArrayList.add(new Section("익스트림", 7));
        sectionArrayList.add(new Section("뮤직비디오", 10));
        sectionArrayList.add(new Section("게임", 13));
        sectionArrayList.add(new Section("장소", 16));
    }

    public ArrayList<Section> getSectionArrayList() {
        return sectionArrayList;
    }
}
