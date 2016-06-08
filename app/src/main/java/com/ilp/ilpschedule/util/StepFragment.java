package com.ilp.ilpschedule.util;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilp.ilpschedule.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step, container, false);
    }


}
