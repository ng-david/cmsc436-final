package com.example.msoohyun88.recyclinghelper.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Item implements Comparable<Item> {
    private String name;
    private String details;
    private String category;

    public Item(String name, String details, String category) {
        this.name = name;
        this.category = category;
        if (details.equals("")) {
            this.details = name + " should be disposed of in " + category + " bins.";
        } else {
            this.details = details;
        }
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory() { return category; }

    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Item s) {
        return name.compareTo(s.getName());
    }


}
