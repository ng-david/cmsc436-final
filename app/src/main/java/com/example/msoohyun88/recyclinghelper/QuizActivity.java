package com.example.msoohyun88.recyclinghelper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msoohyun88.recyclinghelper.database.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    private final static int nQuizQuestions = 5;
    private RadioGroup mRadioGroup;
    private ImageView mImageView;
    private Button mNextButton;
    private TextView mItemName;
    private int numberCorrect;
    private int questionNumber;
    private ArrayList<Item> itemList;
    private HashMap<String, String> mAnswerKey = new HashMap<String, String>(); //Item name to item category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mImageView = (ImageView) findViewById(R.id.material_image);
        mNextButton = (Button) findViewById(R.id.next_button);
        mItemName = (TextView) findViewById(R.id.item_name);

        numberCorrect = 0;
        questionNumber = 0;

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String answer = "";

                switch (mRadioGroup.getCheckedRadioButtonId()) {
                    case 0:
                        answer = "trash";
                        break;
                    case 1:
                        answer = "recyclable";
                        break;
                    case 2:
                        answer = "compost";
                }

                if (answer == "") {
                    Toast.makeText(getApplicationContext(), "Please choose an answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                mItemName.setText(itemList.get(questionNumber).getName());
                String name = mItemName.getText().toString();

                //checks the answer key to make sure the answer is correct
                if (mAnswerKey.get(name).equals(answer)) {
                    numberCorrect++;
                }

                questionNumber++;

                if(questionNumber == nQuizQuestions) {
                    //if all the questions were answered, finish the activity

                    //returning data to original main activity
                    Intent intent = new Intent();
                    intent.putExtra("NUM_CORRECT", numberCorrect);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {

                    //prepares next question and clears the check.
                    mItemName.setText(itemList.get(questionNumber).getName());
                    mRadioGroup.clearCheck();
                }

            }
        });

        create_start_quiz();

        finish();
    }

    private void get_data() {
        //gets the data from firebase here. Choose random trash
        itemList = new ArrayList<Item>();
    }

    private void create_start_quiz(){

        //goes through the
        //generate random fake data
        get_data();

        for (int i = 0; i < nQuizQuestions; i++) {

            //choose random data and populate item list with it
        }

        //set the item name set to the first item in the list
        mItemName.setText(itemList.get(0).getName());
    }
}

