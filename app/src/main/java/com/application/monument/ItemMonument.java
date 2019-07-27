package com.application.monument;

public class ItemMonument {
    private String img;
    private String name;
    private String description;

    //конструктор
    public ItemMonument(String img, String name, String description) {
        this.img = img;
        this.name = name;
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
