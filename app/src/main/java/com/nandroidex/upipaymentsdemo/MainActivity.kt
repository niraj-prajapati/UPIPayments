package com.nandroidex.upipaymentsdemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.nandroidex.upipayments.listener.PaymentStatusListener
import com.nandroidex.upipayments.models.TransactionDetails
import com.nandroidex.upipayments.utils.UPIPayment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PaymentStatusListener {

    private lateinit var upiPayment: UPIPayment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBuyNow.setOnClickListener {
            startUpiPayment()
        }
    }

    private fun startUpiPayment() {
        val millis: Long = System.currentTimeMillis()
        upiPayment = UPIPayment.Builder()
            .with(this@MainActivity)
            .setPayeeVpa(getString(R.string.vpa))
            .setPayeeName(getString(R.string.payee))
            .setTransactionId(millis.toString())
            .setTransactionRefId(millis.toString())
            .setDescription(getString(R.string.transaction_description))
            .setAmount(getString(R.string.amount))
            .build()

        upiPayment.setPaymentStatusListener(this)

        if (upiPayment.isDefaultAppExist()) {
            onAppNotFound()
            return
        }

        upiPayment.startPayment()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        val status = transactionDetails?.status
        val success = status.equals("success", true) || status.equals("submitted", true)
        val approvalRefNo = transactionDetails?.approvalRefNo
        val dialogType = if (success) DialogTypes.TYPE_SUCCESS else DialogTypes.TYPE_ERROR
        val title = if (success) "Good job!" else "Oops!"
        val description = if (success) "UPI ID : $approvalRefNo" else "Transaction Failed/Cancelled"
        val buttonColor = if (success) Color.parseColor("#00C885") else Color.parseColor("#FB2C56")
        val alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(this, dialogType)
                .setTitle(title)
                .setDescription(description)
                .setNoneText("Okay")
                .setNoneTextColor(Color.WHITE)
                .setNoneButtonColor(buttonColor)
                .setNoneListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                    }
                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
        upiPayment.detachListener()
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
