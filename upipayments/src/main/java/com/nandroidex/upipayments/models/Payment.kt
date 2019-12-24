package com.nandroidex.upipayments.models

import java.io.Serializable

class Payment : Serializable {
    var vpa: String? = null
    var name: String? = null
    var payeeMerchantCode: String? = null
    var txnId: String? = null
    var txnRefId: String? = null
    var description: String? = null
    var amount: String? = null
    var defaultPackage: String? = null
    val currency = "INR"
}