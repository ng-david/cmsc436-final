package com.example.msoohyun88.recyclinghelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msoohyun88.recyclinghelper.database.Item;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemDetailsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Item item;
    private ImageButton back;

    //private ItemDetailsFragment.OnFragmentInteractionListener mListener;


    public ItemDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ItemDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemDetailsFragment newInstance() {
        ItemDetailsFragment fragment = new ItemDetailsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_item_details,
                container, false);
        back = (ImageButton) view.findViewById(R.id.back);
        TextView nameText= (TextView) view.findViewById(R.id.name);
        TextView categoryText= (TextView) view.findViewById(R.id.category);
        TextView detailsText = (TextView) view.findViewById(R.id.details);
        ImageView categoryIcon = view.findViewById(R.id.categoryIcon);

        // Set item name text
        String name = getArguments().getString("name");
        nameText.setText(name);

        // Set category text
        String category = getArguments().getString("category");
        categoryText.setText(category.toUpperCase());
        if (category.equals("recycle")) {
            categoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        } else if (category.equals("compost")) {
            categoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            categoryIcon.setImageResource(R.drawable.compost);
        } else {
            categoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGray));
            categoryIcon.setImageResource(R.drawable.trash);
        }

        // Set details
        String details = getArguments().getString("details");
        detailsText.setText(details);

        // Back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    back.setImageResource(R.drawable.ic_chevron_left_black_24dp_pressed);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    back.setImageResource(R.drawable.ic_chevron_left_black_24dp);
                return false;
            }
        });

        return view;

    }

    public void onButtonPressed() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
        //    mListener = (OnFragmentInteractionListener) context;
        //} else {
        //    throw new RuntimeException(context.toString()
        //            + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
}
