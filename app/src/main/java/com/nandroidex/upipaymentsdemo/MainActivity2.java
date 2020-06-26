package com.nandroidex.upipaymentsdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.nandroidex.upipayments.listener.PaymentStatusListener;
import com.nandroidex.upipayments.models.TransactionDetails;
import com.nandroidex.upipayments.utils.UPIPayment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainActivity2 extends AppCompatActivity implements PaymentStatusListener {

    private AppCompatButton btnBuyNow;

    private UPIPayment upiPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuyNow = findViewById(R.id.btnBuyNow);

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity2.this.startUpiPayment();
            }
        });
    }

    private void startUpiPayment() {
        long millis = System.currentTimeMillis();
        upiPayment = new UPIPayment.Builder()
                .with(MainActivity2.this)
                .setPayeeVpa(getString(R.string.vpa))
                .setPayeeName(getString(R.string.payee))
                .setTransactionId(Long.toString(millis))
                .setTransactionRefId(Long.toString(millis))
                .setDescription(getString(R.string.transaction_description))
                .setAmount(getString(R.string.amount))
                .build();

        upiPayment.setPaymentStatusListener(this);

        if (upiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }

        upiPayment.startPayment();
    }

    @Override
    public void onTransactionCompleted(@Nullable TransactionDetails transactionDetails) {
        String status = null;
        String approvalRefNo = null;
        if (transactionDetails != null) {
            status = transactionDetails.getStatus();
            approvalRefNo = transactionDetails.getApprovalRefNo();
        }
        boolean success = false;
        if (status != null) {
            success = status.equalsIgnoreCase("success") || status.equalsIgnoreCase("submitted");
        }
        int dialogType = success ? DialogTypes.TYPE_SUCCESS : DialogTypes.TYPE_ERROR;
        String title = success ? "Good job!" : "Oops!";
        String description = success ? ("UPI ID :" + approvalRefNo) : "Transaction Failed/Cancelled";
        int buttonColor = success ? Color.parseColor("#00C885") : Color.parseColor("#FB2C56");
        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(this, dialogType)
                .setTitle(title)
                .setDescription(description)
                .setNoneText("Okay")
                .setNoneTextColor(Color.WHITE)
                .setNoneButtonColor(buttonColor)
                .setNoneListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                })
                .build();
        alertDialog.setCancelable(false);
        alertDialog.show();
        upiPayment.detachListener();
    }

    @Override
    public void onAppNotFound() {
        Toast.makeText(this, "App Not Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSubmitted() {
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
}