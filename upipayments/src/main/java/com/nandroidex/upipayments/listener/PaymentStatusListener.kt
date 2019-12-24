package com.nandroidex.upipayments.listener

import com.nandroidex.upipayments.models.TransactionDetails

interface PaymentStatusListener {
    fun onTransactionCompleted(transactionDetails: TransactionDetails?)
    fun onTransactionSuccess()
    fun onTransactionSubmitted()
    fun onTransactionFailed()
    fun onTransactionCancelled()
    fun onAppNotFound()
}