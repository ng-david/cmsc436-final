package com.example.msoohyun88.recyclinghelper.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ItemsDAO {

    private String TAG = "ItemsDAO";

    private ArrayList<Item> recycleList;
    private ArrayList<Item> trashList;
    private ArrayList<Item> compostList;

    public ItemsDAO() {

        recycleList = new ArrayList<>();
        trashList = new ArrayList<>();
        compostList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("items");

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, ArrayList<Map<String, String>>> map = (Map) dataSnapshot.getValue();
                for (String k : map.keySet()) {
                    ArrayList<Map<String, String>> currList = map.get(k);
                    for (Map<String, String> itemMap : currList) {
                        Item item = new Item(itemMap.get("name"), itemMap.get("details"), k);
                        if (k.equals("recycle")) {
                            recycleList.add(item);
                        } else if (k.equals("trash")) {
                            trashList.add(item);
                        } else if (k.equals("compost")) {
                            compostList.add(item);
                        } else {
                            Log.w(TAG, "Found a key that was not 'recycle', 'trash', or 'compost'.");
                        }
                    }
                }

                Log.w(TAG, "Successfully updated local database object");
//                Log.w(TAG, recycleList.toString());
//                Log.w(TAG, trashList.toString());
//                Log.w(TAG, compostList.toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public ArrayList<Item> getRecycleList() {
        return recycleList;
    }

    public ArrayList<Item> getTrashList() {
        return trashList;
    }

    public ArrayList<Item> getCompostList() {
        return compostList;
    }
}
