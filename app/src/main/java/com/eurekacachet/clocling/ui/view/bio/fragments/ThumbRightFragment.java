package com.eurekacachet.clocling.ui.view.bio.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThumbRightFragment extends Fragment {

    Button nextButton;
    Button cancelButton;

    public ThumbRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thumb_right, container, false);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        initListeners();
        return view;
    }

    private void initListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BioActivity) getActivity()).getWizard().navigateNext();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
    }

}
