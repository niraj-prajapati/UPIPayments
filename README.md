# UPIPayments

![](https://miro.medium.com/max/1000/1*BOrZtZYRwGEe8NlavcSzRA.png)

<h2>Introduction</h2>

I have developed this Android library to easily implement UPI Payment Integration in Android app.

- UPI apps are required to be installed already before using this library because, internally this API calls UPI apps for payment processing.
- Before using it, make sure that your device is having atleast one UPI app installed. Otherwise it will unable to process the payments.
- This API is in beta, there are lot of improvements are still needed.

![](https://i.ibb.co/V2JLC85/UPIPayments.jpg)

<h2>How to get?</h2>

<b>Step 1.</b>

Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

<b>Step 2.</b>

Add the dependency

```
dependencies {
    implementation 'com.github.NAndroidEx:UPIPayments:1.1.3'
}
```

<h2>How to use it?</h2>

<b>Implement PaymentStatusListener and its methods</b>

```
class MainActivity : AppCompatActivity(), PaymentStatusListener {

    ....
    
    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        Log.d("TransactionDetails", transactionDetails.toString())
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
```

<b>Initializing UPIPayment :</b>

```
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
```

<b>Calls and Descriptions :</b>

<table>
  <tbody><tr>
    <th>Method</th>
    <th><span>Mandatory</span></th>
    <th>Description</th>
  </tr>
  <tr>
    <td>with()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>This call takes Activity as a parameter where Payment is to be implemented<br></td>
  </tr>
  <tr>
    <td>setPayeeVpa()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>It takes VPA address of payee for e.g. <span>shreyas@upi</span></td>
  </tr>
  <tr>
    <td>setTransactionId()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>This field is used in Merchant Payments generated by PSPs.</td>
  </tr>
  <tr>
    <td>setTransactionRefId()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>Transaction reference ID. This could be order number, subscription number, Bill ID, booking ID, insurance renewal reference, etc. Needed for merchant transactions and dynamic URL generation. This is mandatory because absencse of this field generated error in apps like PhonePe</td>
  </tr>
  <tr>
    <td>setDescription()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>To provide a valid small note or description about payment. for e.g. <br><span>For Food</span><br></td>
  </tr>
  <tr>
    <td>setAmount()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>It takes the amount in String decimal format (xx.xx) to be paid. <br>For e.g. 90.88 will pay <span>Rs. 90.88.</span></td>
  </tr>
  <tr>
    <td>setPayeeMerchantCode()</td>
    <td></td>
    <td>Payee Merchant code if present it should be passed.</td>
  </tr>
  <tr>
    <td>build()</td>
    <td><g-emoji class="g-emoji" alias="heavy_check_mark" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/2714.png">✔️</g-emoji></td>
    <td>It will build and returns the <span>EasyUpiPayment</span> instance.</td>
  </tr>
</tbody></table>

<b>Removing Listener</b>

To remove listeners, you can invoke `detachListener()` after the transaction is completed or you haven’t to do with payment callbacks.

```
upiPayment.detachListener()
```

<b>Getting Transaction Details</b>

To get details about transactions, we have callback method `onTransactionCompleted()` with parameter of `TransactionDetails`. TransactionDetails instance includes details about previously completed transaction.
To get details, below method of `TransactionDetails` are useful :

<table>
  <tbody><tr>
    <th>Method</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>getTransactionId()</td>
    <td>Returns Transaction ID</td>
  </tr>
  <tr>
    <td>getResponseCode()</td>
    <td>Returns UPI Response Code</td>
  </tr>
  <tr>
    <td>getApprovalRefNo()</td>
    <td>Returns UPI Approval Reference Number (beneficiary)</td>
  </tr>
  <tr>
    <td>getStatus()</td>
    <td>Returns Status of transaction.<br>(Submitted/Success/Failure)<br></td>
  </tr>
  <tr>
    <td>getTransactionRefId()</td>
    <td>Returns Transaction reference ID passed in input</td>
  </tr>
</tbody></table>

<b>We have successfully implemented UPI integration in our Android app. Thank You!</b>

<h3>Your contributions are welcome.</h3>
