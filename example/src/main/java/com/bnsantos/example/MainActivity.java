package com.bnsantos.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bnsantos.phonenumbertextview.PhoneNumberEditText;

public class MainActivity extends AppCompatActivity {
    private PhoneNumberEditText phoneNumberEditText;
    private TextView formatNational;
    private TextView valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = (PhoneNumberEditText) findViewById(R.id.phoneNumberTextView);
        formatNational = (TextView) findViewById(R.id.formatNational);
        valid = (TextView) findViewById(R.id.valid);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatNational.setText(phoneNumberEditText.formatNational());
                valid.setText("Valid: " + phoneNumberEditText.isValid());
            }
        });
    }
}
