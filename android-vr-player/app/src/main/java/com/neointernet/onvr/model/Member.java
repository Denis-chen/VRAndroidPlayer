package com.neointernet.onvr.model;

/**
 * Created by HSH on 16. 5. 24..
 */
public class Member {
    private String memId;
    private String memNickname;
    private String memProfileImage;

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getMemNickname() {
        return memNickname;
    }

    public void setMemNickname(String memNickname) {
        this.memNickname = memNickname;
    }

    public String getMemProfileImage() {
        return memProfileImage;
    }

    public void setMemProfileImage(String memProfileImage) {
        this.memProfileImage = memProfileImage;
    }
}

