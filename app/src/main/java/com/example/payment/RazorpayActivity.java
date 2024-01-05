package com.example.payment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    LinearLayout lyMoney1,lyMoney2,lyMoney3;
    EditText etMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        LinearLayout button = findViewById(R.id.button1);
        etMoney = findViewById(R.id.etMoney);
        lyMoney1 = findViewById(R.id.lyMoney1);
        lyMoney2 = findViewById(R.id.lyMoney2);
        lyMoney3 = findViewById(R.id.lyMoney3);

        lyMoney1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataFromLyMoney1 = "1000";
                etMoney.setText(dataFromLyMoney1);
            }
        });
        lyMoney2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataFromLyMoney1 = "2000";
                etMoney.setText(dataFromLyMoney1);
            }
        });
        lyMoney3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataFromLyMoney1 = "3000";
                etMoney.setText(dataFromLyMoney1);
            }
        });




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