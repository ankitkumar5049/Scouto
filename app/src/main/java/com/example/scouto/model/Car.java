package com.example.scouto.model;

public class Car {
    private String makeId;
    private String makeName;

    public Car(String makeId, String makeName) {
        this.makeId = makeId;
        this.makeName = makeName;
    }

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }
}
