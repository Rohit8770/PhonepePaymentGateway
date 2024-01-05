package com.example.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.payment.paypa.PaypalActivity;

public class MargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marge);

        Button btnPaytm=findViewById(R.id.btnPaytm);
        Button btnPhonepe=findViewById(R.id.btnPhonepe);
        Button btnRazorpay=findViewById(R.id.btnRazorpay);

        btnPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MargeActivity.this, PaytmPaymentGateway.class));
            }
        });
        btnPhonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MargeActivity.this, MainActivity.class));
            }
        });
        btnRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MargeActivity.this, RazorpayActivity.class));
            }
        });
    }
}