package com.eurekacachet.clocling.ui.view.clocking.modals;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.data.model.Beneficiary;
import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;

import javax.inject.Inject;

public class ResultModal extends DialogFragment implements ResultMvpView {

    private static final String ARG_BENEFICIARY = "beneficiary";
    private static final String ARG_CLOCK = "clock";

    ImageView imageView;
    TextView nameView;
    TextView bidView;
    Button closeButton;
    TextView verifiedView;

    @Inject
    ResultPresenter presenter;
    private Beneficiary mBeneficiary;
    private Clock mClock;

    public static ResultModal newInstance(Beneficiary beneficiary){
        Bundle args = new Bundle();
        args.putParcelable(ARG_BENEFICIARY, beneficiary);
        ResultModal modal = new ResultModal();
        modal.setArguments(args);
        return modal;
    }

    public static ResultModal newInstance(Clock clock){
        Bundle args = new Bundle();
        args.putParcelable(ARG_CLOCK, clock);
        ResultModal modal = new ResultModal();
        modal.setArguments(args);
        return modal;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        if(getArguments() != null){
            mBeneficiary = getArguments().getParcelable(ARG_BENEFICIARY);
            mClock = getArguments().getParcelable(ARG_CLOCK);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ClockingActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.result_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.portraitView);
        nameView = (TextView) view.findViewById(R.id.nameView);
        bidView = (TextView) view.findViewById(R.id.bidView);
        verifiedView = (TextView) view.findViewById(R.id.statusView);
        closeButton = (Button) view.findViewById(R.id.closeModalButton);

        if(mClock != null){
            renderLittleInfo();
        }

        if(mBeneficiary != null){
            renderFullInfo();
        }

        initListeners();
    }

    private void renderFullInfo() {
        byte[] image = Base64.decode(mBeneficiary.picture, Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        nameView.setText(mBeneficiary.name);
        bidView.setText(mBeneficiary.bid);
    }

    private void renderLittleInfo() {
        imageView.setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.INVISIBLE);
        bidView.setText(mClock.bid);
    }

    private void initListeners() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
//                getActivity().finish();
            }
        });
    }
}
