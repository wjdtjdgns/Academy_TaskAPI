package com.nhnacademy.miniDooray.entity;

public enum Status {
    ACTIVATED("활성"),
    DORMANT("휴면"),
    CLOSED("종료");

    private final String koreanValue;

    Status(String koreanValue) {
        this.koreanValue = koreanValue;
    }

    public String getKoreanValue() {
        return koreanValue;
    }
}
