package com.example.msoohyun88.recyclinghelper;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNewItem extends AppCompatActivity {

    private String TAG = "AddNewItem";

    private EditText nameInput;
    private Spinner categorySpinner;
    private MultiAutoCompleteTextView detailsInput;
    private Button submitButton;

    // Keep track of the current count of compost, recycle, and trash items
    private long cIndex;
    private long rIndex;
    private long tIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        nameInput = findViewById(R.id.nameInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        detailsInput = findViewById(R.id.detailsInput);
        submitButton = findViewById(R.id.button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText() != null ? nameInput.getText().toString() : "";
                String cat = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "";
                String details = detailsInput.getText() != null ? detailsInput.getText().toString() : "";

                if (name.equals("") || cat.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Your must input a name and select a category.",Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),nameInput.getText().toString() + " has been added.",Toast.LENGTH_LONG);
                    toast.show();

                    updateFirebase(name, cat.toLowerCase(), details);

                    Log.w(TAG, "Added new item: " + name + ", " + cat + ", " + details);

                    finish();

                }

            }
        });

        // Keep counts in sync
        syncFirebaseCounts();

    }

    private void updateFirebase(final String name, final String category, final String details) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference categoryRef = database.getReference("items/" + category);

        String newIndex = ((category.equals("compost")) ? cIndex : (category.equals("recycle")) ? rIndex : tIndex) + "";
        DatabaseReference newItemRef = categoryRef.child(newIndex);

        newItemRef.child("name").setValue(name);
        newItemRef.child("details").setValue(details);
    }

    private void syncFirebaseCounts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference cRef = database.getReference("items/compost");
        final DatabaseReference tRef = database.getReference("items/trash");
        final DatabaseReference rRef = database.getReference("items/recycle");


        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cIndex = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tIndex = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        rRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                rIndex = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
