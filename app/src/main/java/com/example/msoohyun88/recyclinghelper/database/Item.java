package com.example.msoohyun88.recyclinghelper.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

public class Item implements Comparable<Item> {
    private String name;
    private String details;
    private String category;

    public Item(String name, String details, String category) {
        this.name = name;
        this.category = category;
        if (details == null || details.equals("")) {
            this.details = name + " should be disposed of in " + category + " bins.";
        } else {
            this.details = details;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) &&
                Objects.equals(category, item.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, category);
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
