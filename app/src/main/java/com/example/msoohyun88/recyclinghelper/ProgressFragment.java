package com.example.msoohyun88.recyclinghelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;
import static com.example.msoohyun88.recyclinghelper.QuizActivity.nQuizQuestions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button mQuizButton;
    private TextView mScore;
    private ImageView mForestGraphic;
    public static final int REQUEST = 111;
    public static final String RESULT_KEY = "NUMBER_CORRECT";
    private double average = 0;
    private int numberOfQuizzes = 0;
    private Bitmap graphic;

    private long storedTime = -10000;

    private static final String AVERAGE_KEY = "AVERAGE_KEY";
    private static final String NUMBER_KEY = "NUMBER_KEY";

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        double averageText = average/5.0*100.0;
        mScore.setText(String.format("%.0f", averageText)+"%");

        if (graphic != null){
            mForestGraphic.setImageBitmap(graphic);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View returnView = inflater.inflate(R.layout.fragment_progress, container, false);
        mQuizButton = returnView.findViewById(R.id.quiz_button);
        mScore = returnView.findViewById(R.id.score_text);
        mForestGraphic = returnView.findViewById(R.id.forest_graphic);

        mQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivityForResult(intent, REQUEST);
            }
        });

        // Inflate the layout for this fragment
        return returnView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();

        //set button enabled to false for the day
        long timeNow = System.currentTimeMillis();

        if (timeNow - storedTime > DateUtils.DAY_IN_MILLIS) {
            mQuizButton.setEnabled(true);
            mQuizButton.setVisibility(View.VISIBLE);
        } else {
            mQuizButton.setEnabled(false);
            mQuizButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //TODO: get the result (number of correct) here
        if (REQUEST == requestCode) {
            if (resultCode == RESULT_OK) {
                int returnedResult = data.getExtras().getInt(RESULT_KEY);
                //only previous 3 quizzes count towards your score
                numberOfQuizzes++;
                average = (average*Math.min(3, numberOfQuizzes-1)+returnedResult)/Math.min(4, numberOfQuizzes);
                double averageText = average/5.0*100.0;
                mScore.setText(String.format("%.0f", averageText)+"%");

                if (average >= 4) {
                    graphic = ((BitmapDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.barren4, null)).getBitmap();
                } else if (average >= 3) {
                    graphic = ((BitmapDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.barren3, null)).getBitmap();
                } else if (average >= 2) {
                    graphic = ((BitmapDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.barren2, null)).getBitmap();
                } else if (average >= 1) {
                    graphic = ((BitmapDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.barren, null)).getBitmap();
                } else {
                    mForestGraphic.setVisibility(View.GONE);
                }

                if (graphic != null) {
                    mForestGraphic.setImageBitmap(graphic);
                }

                mQuizButton.setEnabled(false);
                mQuizButton.setVisibility(View.GONE);
                storedTime = System.currentTimeMillis();
            }
        }
    }

    private BitmapDrawable scaleBitmap(Bitmap bm){

        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = getView().getWidth();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int newHeight = size.y;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        return new BitmapDrawable(getContext().getResources(), resizedBitmap);
    }
}
