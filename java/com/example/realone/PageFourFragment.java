package com.example.realone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFourFragment extends Fragment {

    public static PageFourFragment newInstance() {
        Bundle args = new Bundle();

        PageFourFragment fragment = new PageFourFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public PageFourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_four, container, false);
    }

}
