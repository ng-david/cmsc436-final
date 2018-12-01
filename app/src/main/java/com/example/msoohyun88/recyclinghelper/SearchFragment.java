package com.example.msoohyun88.recyclinghelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.msoohyun88.recyclinghelper.database.Item;
import com.example.msoohyun88.recyclinghelper.database.ItemsDAO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private final String TAG = "SearchFragment";

    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    ArrayList<Item> itemList;
    private ArrayList<String> filteredList;
    private ListView listview;
    private EditText searchField;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProgressFragment.
     */
    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // TODO include all item type
    // TODO make it case insensitive
    // TODO make it optimized? not on every character change
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Get list view
        listview = (ListView) view.findViewById(R.id.listview);

        // Get reference to the loading activityIndicator
        final RelativeLayout activityIndicator =  view.findViewById(R.id.activityIndicator);

        // Hold a list of the DB items
        itemList = new ArrayList<>();

        // Hold a list of items to render
        filteredList = new ArrayList<>();

        // Communicate with FB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("items");

        // Listener for DB read/changes
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Populate itemList with Items
                Map<String, ArrayList<Map<String, String>>> map = (Map) dataSnapshot.getValue();
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

                rerenderListView();

                Log.w(TAG, "Successfully updated local list");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Text Field Change
        searchField = (EditText) view.findViewById(R.id.editText);
        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rerenderListView();
            }
        });

        return view;
    }

    private void rerenderListView() {
        filteredList = new ArrayList<>();
        if(searchField.getText().toString().length() != 0) {
            // Filter items where names don't match
            for (Item item : itemList) {
                if (item.getName().toUpperCase().indexOf(searchField.getText().toString().toUpperCase()) != -1) {
                    Log.w(TAG, "Successful find while filtering for " + searchField.getText().toString());
                    filteredList.add(item.getName());
                }
            }
        } else {
            // Grab all items
            for (Item item : itemList) {
                filteredList.add(item.getName());
            }
        }

        // Update the list with latest filteredList
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, filteredList);
        listview.setAdapter(adapter);
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

}


