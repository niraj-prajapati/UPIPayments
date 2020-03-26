package com.nandroidex.upipayments.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.nandroidex.upipayments.listener.PaymentStatusListener
import com.nandroidex.upipayments.models.Payment
import com.nandroidex.upipayments.ui.PaymentsUIActivity
import com.nandroidex.upipayments.utils.Singleton.Companion.getInstance

class UPIPayment private constructor(
    private val mActivity: Activity,
    private val mPayment: Payment
) {
    fun startPayment() {
        val payIntent = Intent(mActivity, PaymentsUIActivity::class.java)
        payIntent.putExtra("payment", mPayment)
        mActivity.startActivity(payIntent)
    }

    fun setPaymentStatusListener(mListener: PaymentStatusListener) {
        val singleton = getInstance()
        singleton!!.setListener(mListener)
    }

    fun detachListener() {
        getInstance()?.detachListener()
    }

    private fun isPackageInstalled(
        packageName: String,
        packageManager: PackageManager
    ): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun isDefaultAppExist(): Boolean {
        return if (mPayment.defaultPackage != null) {
            !isPackageInstalled(mPayment.defaultPackage!!, mActivity.packageManager)
        } else {
            Log.w(
                "UnSpecified", "Default app is not Specified. Specify it using " +
                        "'setDefaultApp()' method of Builder class"
            )
            false
        }
    }

    class Builder {
        private var activity: Activity? = null
        private var payment: Payment? = null
        fun with(activity: Activity): Builder {
            this.activity = activity
            payment = Payment()
            return this
        }

        fun setPayeeVpa(vpa: String): Builder {
            check(vpa.contains("@")) { "Payee VPA address should be valid (For e.g. example@vpa)" }
            payment!!.vpa = vpa
            return this
        }

        fun setPayeeName(name: String): Builder {
            check(name.trim { it <= ' ' }.isNotEmpty()) { "Payee Name Should be Valid!" }
            payment!!.name = name
            return this
        }

        fun setPayeeMerchantCode(merchantCode: String): Builder {
            check(merchantCode.trim { it <= ' ' }.isNotEmpty()) { "Merchant Code Should be Valid!" }
            payment!!.payeeMerchantCode = merchantCode
            return this
        }

        fun setTransactionId(id: String): Builder {
            check(id.trim { it <= ' ' }.isNotEmpty()) { "Transaction ID Should be Valid!" }
            payment!!.txnId = id
            return this
        }

        fun setTransactionRefId(refId: String): Builder {
            check(refId.trim { it <= ' ' }.isNotEmpty()) { "RefId Should be Valid!" }
            payment!!.txnRefId = refId
            return this
        }

        fun setDescription(description: String): Builder {
            check(description.trim { it <= ' ' }.isNotEmpty()) { "Description Should be Valid!" }
            payment!!.description = description
            return this
        }

        fun setAmount(amount: String): Builder {
            check(amount.contains(".")) { "Amount should be in decimal format XX.XX (For e.g. 100.00)" }
            payment!!.amount = amount
            return this
        }

        fun build(): UPIPayment {
            checkNotNull(activity) { "Activity must be specified using with() call before build()" }
            checkNotNull(payment) { "com.nandroidex.upipayments.models.Payment Details must be initialized before build()" }
            checkNotNull(payment!!.vpa) { "Must call setPayeeVpa() before build()." }
            checkNotNull(payment!!.txnId) { "Must call setTransactionId() before build" }
            checkNotNull(payment!!.txnRefId) { "Must call setTransactionRefId() before build" }
            checkNotNull(payment!!.name) { "Must call setPayeeName() before build()." }
            checkNotNull(payment!!.amount) { "Must call setAmount() before build()." }
            checkNotNull(payment!!.description) { "Must call setDescription() before build()." }
            return UPIPayment(activity!!, payment!!)
        }
    }
}