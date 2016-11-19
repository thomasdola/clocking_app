package com.eurekacachet.clocling.ui.view;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsActivity;
import com.eurekacachet.clocling.R;

public class MainActivity extends BiometricsActivity {

    ImageView fingerPrintView;
    Button  grabFingerPrintButton;
    TextView opStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fingerPrintView = (ImageView) findViewById(R.id.fingerPrint);
        grabFingerPrintButton = (Button) findViewById(R.id.grabFinger);
        opStatusView = (TextView) findViewById(R.id.statusView);

        init();
    }

    private void init() {
        grabFingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerPrintView.setImageDrawable(null);
                grabFingerprint();
            }
        });
    }

    @Override
    public void onFingerprintGrabbed(ResultCode result, Bitmap bitmap, byte[] iso, String filepath, String status) {
        if(status != null) opStatusView.setText(status);
        if(bitmap != null) fingerPrintView.setImageBitmap(bitmap);
    }


}
