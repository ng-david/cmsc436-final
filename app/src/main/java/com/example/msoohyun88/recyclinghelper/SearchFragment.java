package com.example.msoohyun88.recyclinghelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.msoohyun88.recyclinghelper.database.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    private ArrayList<Item> itemList;
    private HashMap<String, String> categoriesMap;
    public ArrayList<Item> filteredList;
    public ListView listview;
    public EditText searchField;
    private ItemDetailsFragment mItemDetailsFragment;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // TODO include all item type
    // TODO make it case insensitive
    // TODO make it optimized? not on every character change
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Get list view
        listview = view.findViewById(R.id.listview);

        // Hold a list and map of the DB items
        itemList = new ArrayList<>();
        categoriesMap = new HashMap<>();

        // Hold a list of items to render
        filteredList = new ArrayList<>();

        // Listener for DB read/changes
        setUpFirebaseListeners(view);

        // Text Field Change
        setUpSearchInput(view);

        View footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        footerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.w(TAG, "WOW FOOTER CLICK");
            }
        });
        listview.addFooterView(footerView);

        return view;
    }

    // Rerender list view depending on search input
    private void setUpSearchInput(View view) {
        searchField = view.findViewById(R.id.editText);
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
    }

    // Update local lists when firebase returns data or changes to data
    private void setUpFirebaseListeners(View view) {
        // Get reference to the loading activityIndicator
        final RelativeLayout activityIndicator =  view.findViewById(R.id.activityIndicator);

        // Communicate with FB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("items");
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Populate itemList with Items
                Map<String, ArrayList<Map<String, String>>> map = (Map) dataSnapshot.getValue();
                for (String k : map.keySet()) {
                    ArrayList<Map<String, String>> currList = map.get(k);
                    for (Map<String, String> itemMap : currList) {

                        String category = k.equals("trash") ? "trash" : k.equals("compost") ? "compost" : "recycle";

                        Item item = new Item(itemMap.get("name"), itemMap.get("details"), category);

                        categoriesMap.put(item.getName(), item.getCategory());
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
    }

    private void rerenderListView() {
        filteredList = new ArrayList<>();
        if(searchField.getText().toString().length() != 0) {
            // Filter items where names don't match
            for (Item item : itemList) {
                if (item.getName().toUpperCase().indexOf(searchField.getText().toString().toUpperCase()) != -1) {
                    Log.w(TAG, "Successful find while filtering for " + searchField.getText().toString());
                    filteredList.add(item);
                }
            }
        } else {
            // Grab all items
            for (Item item : itemList) {
                filteredList.add(item);
            }
        }

        Collections.sort(filteredList);

        // Update the list with latest filteredList
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.list_item, R.id.myItemName, filteredList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                TextView nameTextView = view.findViewById(R.id.myItemName);
                String name = nameTextView.getText().toString();
                String category = categoriesMap.get(name);
                ImageView imageView = view.findViewById(R.id.listCategoryIcon);

                if (category.equals("recycle")) {
                    imageView.setImageResource(R.drawable.recycle);

                } else if (category.equals("trash")) {
                    imageView.setImageResource(R.drawable.trash);
                } else {
                    imageView.setImageResource(R.drawable.compost);

                }

                return view;
            };
        };

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           Item item = filteredList.get(i);

           mItemDetailsFragment = new ItemDetailsFragment();
           Bundle args = new Bundle();
           args.putString("name",item.getName());
           args.putString("details",item.getDetails());
           args.putString("category",item.getCategory());
           mItemDetailsFragment.setArguments(args);
           loadFragment(mItemDetailsFragment);
           }
        });

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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


