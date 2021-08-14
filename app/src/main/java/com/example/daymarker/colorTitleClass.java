package com.example.daymarker;

public class colorTitleClass {
    String Title;
    int color;

    public colorTitleClass(String title, int color) {
        Title = title;
        this.color = color;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
