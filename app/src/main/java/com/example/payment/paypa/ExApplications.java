package com.example.payment.paypa;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class ExApplications extends Application {

    public String Client_id = "Aakwbag7yDJ7ZcL6KzQZhEQ42H07muUz9V5Wg1205KYGd655GPX-nd5hDiE696CJZB7P0NiEeGj8TZf5";
    public String Return_Url =  "com.example.payment://paypalpay";

    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                        this,
                        Client_id,
                        Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                Return_Url
                ));
    }
}
