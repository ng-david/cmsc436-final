package com.example.msoohyun88.recyclinghelper.database;

public class Item {
    private String name;
    private String details;
    private String category;

    public Item(String name, String details, String category) {
        this.name = name;
        this.details = details;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory() { return category; }

    public String toString() {
        return "{ name: " + name + ", details: " + details + "}";
    }
}
