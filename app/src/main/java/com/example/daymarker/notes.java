package com.example.daymarker;

public class notes {
    String NOTE;

    public notes(String NOTE, String DATE) {
        this.NOTE = NOTE;
        this.DATE = DATE;
    }

    String DATE;

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
