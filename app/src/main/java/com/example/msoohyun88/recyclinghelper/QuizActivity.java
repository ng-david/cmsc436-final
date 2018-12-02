package com.example.msoohyun88.recyclinghelper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msoohyun88.recyclinghelper.database.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import static com.example.msoohyun88.recyclinghelper.ProgressFragment.RESULT_KEY;

public class QuizActivity extends AppCompatActivity {

    private final static int nQuizQuestions = 5;

    private Button mTrashButton;
    private Button mRecycleButton;
    private Button mCompostButton;

    private ImageView mImageView;
    private TextView mItemName;
    private int numberCorrect;
    private int questionNumber;
    private ArrayList<Item> itemList;
    private ArrayList<Item> chosenItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrashButton = (Button) findViewById(R.id.trash);
        mRecycleButton = (Button) findViewById(R.id.recyclable);
        mCompostButton = (Button) findViewById(R.id.compost);

        int primaryColor = getColor(R.color.colorPrimary);
        int greenColor = getColor(R.color.colorGreen);
        int grayColor = getColor(R.color.colorGray);
        int grey = getColor(R.color.colorWhite);

        mTrashButton.setBackgroundColor(grayColor);
        mTrashButton.setTextColor(grey);
        mRecycleButton.setBackgroundColor(primaryColor);
        mRecycleButton.setTextColor(grey);
        mCompostButton.setBackgroundColor(greenColor);
        mCompostButton.setTextColor(grey);

        mImageView = (ImageView) findViewById(R.id.material_image);
        mItemName = (TextView) findViewById(R.id.item_name);
        itemList = new ArrayList<Item>();

        numberCorrect = 0;
        questionNumber = 0;

        View.OnClickListener buttonClickListeners = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String answer = ((Button)v).getText().toString().toLowerCase();

                //checks the answer key to make sure the answer is correct
                if (chosenItemList.get(questionNumber).getCategory().equals(answer)) {
                    numberCorrect++;
                }

                questionNumber++;

                if(questionNumber == nQuizQuestions) {
                    //if all the questions were answered, finish the activity

                    //returning data to original main activity
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY, numberCorrect);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {

                    //prepares next question and clears the check.
                    mItemName.setText(chosenItemList.get(questionNumber).getName());
                }
            }
        };

        mTrashButton.setOnClickListener(buttonClickListeners);
        mRecycleButton.setOnClickListener(buttonClickListeners);
        mCompostButton.setOnClickListener(buttonClickListeners);

        setUpFirebaseListeners();
    }

    private void setUpFirebaseListeners() {
        // Get reference to the loading activityIndicator
        final RelativeLayout activityIndicator = findViewById(R.id.activityIndicator);

        // Communicate with FB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("items");
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Populate itemList with Items
                HashMap<String, ArrayList<Map<String, String>>> map = (HashMap) dataSnapshot.getValue();
                for (String k : map.keySet()) {
                    ArrayList<Map<String, String>> currList = map.get(k);
                    for (Map<String, String> itemMap : currList) {
                        String category = k.equals("trash") ? "trash" : k.equals("compost") ? "compost" : "recycle";
                        Item item = new Item(itemMap.get("name"), itemMap.get("details"), category);
                        itemList.add(item);
                    }
                }

                // If we have data, hide the activityIndicator
                activityIndicator.setVisibility(View.GONE);

                create_start_quiz();

                Log.w("QUIZ_ACTIVITY", "Successfully updated local list");
            }

            @Override
            public void onCancelled(DatabaseError error) {

                activityIndicator.setVisibility(View.GONE);
                Log.w("QUIZ_ACTIVITY", "Failed to read value.", error.toException());
            }
        });
    }

    private void create_start_quiz(){

        //goes through the
        //generate random fake data

        Random rand = new Random();
        int listSize = itemList.size();
        HashSet<Integer> indices = new HashSet<Integer>();

        while(indices.size() < nQuizQuestions) {
            indices.add(rand.nextInt(listSize));
        }

        for (int current: indices) {
            chosenItemList.add(itemList.get(current));
        }

        //set the item name set to the first item in the list
        mItemName.setText(chosenItemList.get(0).getName());

    }
}

