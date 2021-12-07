package com.example.weatherapi;

import java.io.Serializable;

public class model implements Serializable {

    private String startTime;
    private String endTime;
    private String parameterName;
    private String parameterUnit;

    public model(String startTime, String endTime, String parameterName, String parameterUnit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.parameterName = parameterName;
        this.parameterUnit = parameterUnit;
    }

    public model() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }

    @Override
    public String toString() {
        return "model{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", parameterName='" + parameterName + '\'' +
                ", parameterUnit='" + parameterUnit + '\'' +
                '}';
    }
}
