package com.eurekacachet.clocling.ui.view.login.modal;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;

import javax.inject.Inject;

public class LoginFragment extends DialogFragment implements LoginFragmentMvpView{

    private ProgressDialog mProgressDialog;

    @Inject
    LoginFragmentPresenter presenter;

    public LoginFragment(){}

    public static LoginFragment newIntent(){
        return new LoginFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.login_dialog, null))
                .setPositiveButton(R.string.login_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginFragment.this.getDialog().dismiss();
                    }
                });
        return builder.create();
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
        getDialog().dismiss();
    }

    @Override
    public void onSignInFailed(String reason) {
        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
    }
}
