package com.example.msoohyun88.recyclinghelper.database;

public class Item {
    private String name;
    private String details;

    public Item(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String toString() {
        return "{ name: " + name + ", details: " + details + "}";
    }
}
