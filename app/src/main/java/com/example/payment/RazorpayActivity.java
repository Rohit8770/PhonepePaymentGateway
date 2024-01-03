package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });


    }

    public void startPayment(){
        Checkout checkout=new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = this;
        try {
            JSONObject option =new JSONObject();
            option.put("name",R.string.app_name);
            option.put("description","payment for anything");
            option.put("send_sms_hash",true);
            option.put("allow_rotation",false);

            option.put("currency","INR");
            option.put("amount","100");

            JSONObject preFill =new JSONObject();
            option.put("email","");
            option.put("contact","");
            option.put("prefill",preFill);

            checkout.open(activity, option);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment" +e.getMessage() , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();

    }
}