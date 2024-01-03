package com.example.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    String MerchantId = "PGTESTPAYUAT";
    private static final String API_ENDPOINT = "/pg/v1/pay";
    private static final String SALT = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399";
    String merchantTransactionId = "txnId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        LinearLayout button = findViewById(R.id.button);
        PhonePe.init(this, PhonePeEnvironment.UAT,MerchantId,"");

        JSONObject data = new JSONObject();
        try {
            data.put("MerchantTransactionId", merchantTransactionId);
            data.put("MerchantId", MerchantId);
            data.put("amount", 200);
           // data.put("mobileNumber", "8770631047");
            data.put("callbackUrl", "https://webhook.site/abd2f0f9-77a7-4df4-804b-2fff936bdfa8 ");

            JSONObject paymentInstrument = new JSONObject();
            paymentInstrument.put("type", "UPI_INTENT");
            paymentInstrument.put("targetApp", "com.phonepe.app");

            data.put("paymentInstrument", paymentInstrument);

            JSONObject deviceContext = new JSONObject();
            deviceContext.put("deviceOS", "ANDROID");

            data.put("deviceContext", deviceContext);

            String payloadBase64 = Base64.encodeToString(
                    data.toString().getBytes(Charset.defaultCharset()),
                    Base64.NO_WRAP);

            Log.e("PhonepeActivity", "onCreate: " + payloadBase64 + "\nmtid : " + merchantTransactionId);

            String checksum = sha256(payloadBase64 + API_ENDPOINT + SALT) + "###1";

            B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                    .setData(payloadBase64)
                    .setChecksum(checksum)
                    .setUrl(API_ENDPOINT)
                    .build();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent phonePeIntent = PhonePe.getImplicitIntent(
                                PaymentActivity.this, b2BPGRequest,
                                "https://webhook.site/abd2f0f9-77a7-4df4-804b-2fff936bdfa8");

                        if (phonePeIntent != null) {
                            startActivityForResult(phonePeIntent, 1);
                        }
                    } catch (Exception e) {
                        Log.e("PhonepeActivity", "Exception during PhonePe intent creation: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PhonepeActivity", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == 1) {
            Toast.makeText(this, "check callback url", Toast.LENGTH_SHORT).show();

        }
    }

    private String sha256(String input) {
        byte[] bytes = input.getBytes(Charset.forName("UTF-8"));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}