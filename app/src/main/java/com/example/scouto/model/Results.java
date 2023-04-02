package com.example.scouto.model;

public class Results {
    private String makeId;
    private String makeName;
    private String modelId;
    private String modelName;

    public Results(String makeId, String makeName, String modelId, String modelName) {
        this.makeId = makeId;
        this.makeName = makeName;
        this.modelId = modelId;
        this.modelName = modelName;
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

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
