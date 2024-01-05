package com.example.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaytmPaymentGateway extends AppCompatActivity {

    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    EditText amount,note,name,upiId;
    Button pay;
    TextView msg;
    public static String payerName,upi_Id, msgNote, status, sendAmount;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment_gateway);

        name = findViewById(R.id.name);
        upiId = findViewById(R.id.upi_id);
        amount = findViewById(R.id.amount);
        note = findViewById(R.id.txt_note);
        msg = findViewById(R.id.msg);
        pay = findViewById(R.id.btnPay);



      //  status = msg.getText().toString();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payerName = name.getText().toString();
                upi_Id = upiId.getText().toString();
                sendAmount = amount.getText().toString();
                msgNote = note.getText().toString();

                if (payerName!= null && upi_Id!= null && sendAmount!= null&& msgNote!= null){
                   uri = getPaytmUri(payerName,upi_Id,msgNote,sendAmount);
                   payWithPaytm(PAYTM_PACKAGE_NAME);
                }else {
                    Toast.makeText(PaytmPaymentGateway.this, "Please fill all the Details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private static Uri getPaytmUri(String name,String upiId,String note, String amount){
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("CU","INR")
                .build();
    }

    private void payWithPaytm(String packageName){
        if(isAppInstalled(this,packageName)){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(uri);
            i.setPackage(packageName);
            startActivityForResult(i,1);
        }else {
            Toast.makeText(this, "Payment is not Paid and please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public  static boolean isAppInstalled(Context context,String packageName){
        try{
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        return false;
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (data != null){
            status = data.getStringExtra("status").toLowerCase();
        }
        if (resultCode == RESULT_OK && "success".equals(status)) {
        Toast.makeText(this, "Transaction Successful" +data.getStringExtra("ApprovalRefNo"), Toast.LENGTH_SHORT).show();
            msg.setText("Transaction Successful of INR "+sendAmount);
            msg.setText(Color.GREEN);
        }else {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            msg.setText("Transaction Failed of INR "+sendAmount);
            msg.setText(Color.RED);
        }
    }
}