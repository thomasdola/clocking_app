package com.eurekacachet.clocling.ui.view.login.modal;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.ui.view.main.MainActivity;
import com.eurekacachet.clocling.utils.services.SocketService;

import java.util.HashMap;

import javax.inject.Inject;

import ru.kolotnev.formattedittext.MaskedEditText;

public class LoginFragment extends DialogFragment implements LoginFragmentMvpView{

    private ProgressDialog mProgressDialog;
    public static String ARG_DEVICE_ID = "device_id";
    MaskedEditText mPinField;
    Button mCancelButton;
    Button mLoginButton;

    @Inject
    LoginFragmentPresenter presenter;
    String mDeviceId;

    public LoginFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mDeviceId = getArguments().getString(ARG_DEVICE_ID);
        }
    }

    public static LoginFragment newInstance(String deviceId){
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPinField = (MaskedEditText) view.findViewById(R.id.pinTextField);
        mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        initListeners();
    }

    private void initListeners() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = mPinField.getText(true).toString();
                if(pin.trim().length() == 4){
                    TelephonyManager telephonyManager = (TelephonyManager)
                            getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    HashMap<String, String> cred = new HashMap<>();
                    cred.put("pin", pin);
                    cred.put("device_id", telephonyManager.getDeviceId());
                    presenter.signIn(cred);
                }
            }
        });
    }

    @Override
    public void toggleLoading(Boolean visible) {
        if(visible){
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading_text));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }else{
            if(mProgressDialog != null){
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                mProgressDialog = null;
            }
        }
    }

    @Override
    public void launchMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getDialog().dismiss();
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSignInFailed(String reason) {
        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSocketService() {
        getActivity()
                .startService(new Intent(getActivity(), SocketService.class));
    }
}
