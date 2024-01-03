package com.example.payment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout button = findViewById(R.id.button);


        PhonePe.init(this, PhonePeEnvironment.UAT,"PGTESTPAYUAT","");

        JSONObject data = new JSONObject();
        try {
            data.put("merchantTransactionId", String.valueOf(System.currentTimeMillis()));
            data.put("merchantId", "PGTESTPAYUAT");
            data.put("merchantUserId", String.valueOf(System.currentTimeMillis()));
            data.put("amount", 50000);
          //  data.put("mobileNumber", "8770631047");
            data.put("callbackUrl", "https://webhook.site/abd2f0f9-77a7-4df4-804b-2fff936bdfa8");

            JSONObject mPaymentInstrument = new JSONObject();
            mPaymentInstrument.put("type", "PAY_PAGE");

            data.put("paymentInstrument", mPaymentInstrument);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String base64Body = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Body = Base64.getEncoder().encodeToString(data.toString().getBytes(Charset.defaultCharset()));
        }

        String checksum = sha256(base64Body + "/pg/v1/pay" + "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399") +"###1";

        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                .setData(base64Body)
                .setChecksum(checksum)
                .setUrl("/pg/v1/pay")
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Button clicked, starting activity for result");
                try {
                    startActivityForResult(
                            PhonePe.getImplicitIntent(
                                    MainActivity.this, b2BPGRequest, "https://webhook.site/abd2f0f9-77a7-4df4-804b-2fff936bdfa8"
                            ), 1
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "Exception during PhonePe intent creation: " + e.getMessage());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "onActivityResult called with requestCode: " + requestCode);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }


    }




        private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(Charset.forName("UTF-8")));
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}