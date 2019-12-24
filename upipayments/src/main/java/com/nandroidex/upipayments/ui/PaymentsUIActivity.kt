package com.nandroidex.upipayments.ui

import com.nandroidex.upipayments.models.Payment
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nandroidex.upipayments.R
import com.nandroidex.upipayments.models.TransactionDetails
import com.nandroidex.upipayments.utils.Singleton
import java.util.*

class PaymentsUIActivity : AppCompatActivity() {

    private var singleton: Singleton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments_ui)

        singleton = Singleton.getInstance()

        val intent = intent
        val payment: Payment = intent.getSerializableExtra("payment") as Payment

        val payUri = Uri.Builder()

        payUri.scheme("upi").authority("pay")
        payUri.appendQueryParameter("pa", payment.vpa)
        payUri.appendQueryParameter("pn", payment.name)
        payUri.appendQueryParameter("tid", payment.txnId)

        if (payment.payeeMerchantCode != null) {
            payUri.appendQueryParameter("mc", payment.payeeMerchantCode)
        }

        payUri.appendQueryParameter("tr", payment.txnRefId)
        payUri.appendQueryParameter("tn", payment.description)
        payUri.appendQueryParameter("am", payment.amount)
        payUri.appendQueryParameter("cu", payment.currency)

        val uri = payUri.build()

        val paymentIntent = Intent(Intent.ACTION_VIEW)
        paymentIntent.data = uri

        if (payment.defaultPackage != null) {
            paymentIntent.setPackage(payment.defaultPackage)
        }

        if (paymentIntent.resolveActivity(packageManager) != null) {
            val intentList = packageManager
                .queryIntentActivities(paymentIntent, 0)
            showApps(intentList, paymentIntent)
        } else {
            Toast.makeText(
                this, "No UPI app found! Please Install to Proceed!",
                Toast.LENGTH_SHORT
            ).show()
            callbackOnAppNotFound()
        }
    }

    private fun showApps(
        appsList: List<ResolveInfo>,
        intent: Intent
    ) {
        val onCancelListener =
            View.OnClickListener {
                callbackTransactionCancelled()
                finish()
            }
        val appsBottomSheet = BottomsheetFragment(appsList, intent, onCancelListener)
        appsBottomSheet.show(supportFragmentManager, "Pay Using")
    }

    @SuppressLint("DefaultLocale")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_REQUEST) {
            if (data != null) {
                val response = data.getStringExtra("response")
                if (response == null) {
                    callbackTransactionCancelled()
                    Log.d(TAG, "Response is null")
                } else {
                    val transactionDetails: TransactionDetails = getTransactionDetails(response)
                    callbackTransactionComplete(transactionDetails)
                    try {
                        when {
                            transactionDetails.status?.toLowerCase().equals("success") -> {
                                callbackTransactionSuccess()
                            }
                            transactionDetails.status?.toLowerCase().equals("submitted") -> {
                                callbackTransactionSubmitted()
                            }
                            else -> {
                                callbackTransactionFailed()
                            }
                        }
                    } catch (e: Exception) {
                        callbackTransactionCancelled()
                        callbackTransactionFailed()
                    }
                }
            } else {
                Log.e(TAG, "Intent Data is null. User cancelled")
                callbackTransactionCancelled()
            }
            finish()
        }
    }

    private fun getQueryString(url: String): Map<String, String> {
        val params = url.split("&").toTypedArray()
        val map: MutableMap<String, String> =
            HashMap()
        for (param in params) {
            val name = param.split("=").toTypedArray()[0]
            val value = param.split("=").toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    private fun getTransactionDetails(response: String): TransactionDetails {
        val map = getQueryString(response)
        val transactionId = map["txnId"]
        val responseCode = map["responseCode"]
        val approvalRefNo = map["ApprovalRefNo"]
        val status = map["Status"]
        val transactionRefId = map["txnRef"]
        return TransactionDetails(
            transactionId,
            responseCode,
            approvalRefNo,
            status,
            transactionRefId
        )
    }

    private fun isListenerRegistered(): Boolean {
        return Singleton.getInstance()!!.isListenerRegistered
    }

    private fun callbackOnAppNotFound() {
        Log.e(TAG, "No UPI app found on device.")
        if (isListenerRegistered()) {
            singleton!!.getListener().onAppNotFound()
        }
        finish()
    }

    private fun callbackTransactionSuccess() {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionSuccess()
        }
    }

    private fun callbackTransactionSubmitted() {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionSubmitted()
        }
    }

    private fun callbackTransactionFailed() {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionFailed()
        }
    }

    private fun callbackTransactionCancelled() {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionCancelled()
        }
    }

    private fun callbackTransactionComplete(transactionDetails: TransactionDetails) {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionCompleted(transactionDetails)
        }
    }

    companion object {
        const val TAG = "PaymentsUIActivity"
        const val PAYMENT_REQUEST: Int = 100
    }
}
