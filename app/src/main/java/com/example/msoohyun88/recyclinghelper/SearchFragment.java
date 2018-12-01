package com.example.msoohyun88.recyclinghelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    private ArrayList<Item> recycleList;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ListView listview = (ListView) view.findViewById(R.id.listview);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
//            list.add(values[i]);
//        }


        // YES
        recycleList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = database.getReference("items");

        final Fragment thisFrag = this;

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, ArrayList<Map<String, String>>> map = (Map) dataSnapshot.getValue();
                for (String k : map.keySet()) {
                    ArrayList<Map<String, String>> currList = map.get(k);
                    for (Map<String, String> itemMap : currList) {
                        Item item = new Item(itemMap.get("name"), itemMap.get("details"));
                        if (k.equals("compost")) {
                            recycleList.add(item);
                        }
                    }
                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(thisFrag).attach(thisFrag).commit();

                Log.w("HELLO", "Successfully updated local database object");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("HELLO", "Failed to read value.", error.toException());
            }
        });


        for (int i = 0; i < recycleList.size(); i++) {
            list.add(recycleList.get(i).getName());
        }
        // END YES

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        return view;
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


