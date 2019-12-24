package com.nandroidex.upipaymentsdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nandroidex.upipayments.listener.PaymentStatusListener
import com.nandroidex.upipayments.models.TransactionDetails
import com.nandroidex.upipayments.utils.UPIPayment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PaymentStatusListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val millis: Long = System.currentTimeMillis()

        etTxnId.setText("$millis")
        etTxnRefId.setText("$millis")

        btnPayUsing.setOnClickListener {
            startUpiPayment()
        }
    }

    private fun startUpiPayment() {
        val upiPayment: UPIPayment = UPIPayment.Builder()
            .with(this@MainActivity)
            .setPayeeVpa(etVPA.text.toString())
            .setPayeeName(etName.text.toString())
            .setPayeeMerchantCode(etPayeeMerchantCode.text.toString())
            .setTransactionId(etTxnId.text.toString())
            .setTransactionRefId(etTxnRefId.text.toString())
            .setDescription(etDescription.text.toString())
            .setAmount(etAmount.text.toString())
            .build()

        upiPayment.setPaymentStatusListener(this)

        if (upiPayment.isDefaultAppExist()) {
            onAppNotFound()
            return
        }

        upiPayment.startPayment()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        Log.d("TransactionDetails", transactionDetails.toString())
    }

    override fun onTransactionSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionSubmitted() {
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCancelled() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
    }

    override fun onAppNotFound() {
        Toast.makeText(this, "App Not Found", Toast.LENGTH_SHORT).show()
    }
}
