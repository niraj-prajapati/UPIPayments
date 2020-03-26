package com.nandroidex.upipaymentsdemo

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bluehomestudio.luckywheel.WheelItem
import com.nandroidex.upipayments.listener.PaymentStatusListener
import com.nandroidex.upipayments.models.TransactionDetails
import com.nandroidex.upipayments.utils.UPIPayment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PaymentStatusListener {

    private lateinit var upiPayment: UPIPayment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpWheel()

        btnClaim.setOnClickListener {
            startUpiPayment()
        }
    }

    private fun setUpWheel() {
        val wheelItems: MutableList<WheelItem> = ArrayList()

        wheelItems.add(
            WheelItem(
                Color.LTGRAY,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "text 1"
            )
        )

        wheelItems.add(
            WheelItem(
                Color.BLUE,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "text 2"
            )
        )

        wheelItems.add(
            WheelItem(
                Color.BLACK,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "text 3"
            )
        )

        wheelItems.add(
            WheelItem(
                Color.GRAY,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name)
                , "text 4"
            )
        )

        wheelItems.add(
            WheelItem(
                Color.RED,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "text 5"
            )
        )

        wheelItems.add(
            WheelItem(
                Color.BLACK,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                "text 6"
            )
        )

        lwv.addWheelItems(wheelItems)
        lwv.setTarget((0..5).random())

        lwv.setLuckyWheelReachTheTarget {
            lwv.setTarget((0..5).random())
            lwv.isEnabled = false
            btnClaim.isEnabled = true
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
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        upiPayment.detachListener()
        if (status.equals("success", true) || status.equals("submitted", true)) {
            lwv.isEnabled = true
            btnClaim.isEnabled = false
        }
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
