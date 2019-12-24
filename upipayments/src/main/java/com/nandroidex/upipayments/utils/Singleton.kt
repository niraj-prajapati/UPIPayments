package com.nandroidex.upipayments.utils

import com.nandroidex.upipayments.listener.PaymentStatusListener

class Singleton {
    private var listener: PaymentStatusListener? = null
    fun getListener(): PaymentStatusListener {
        return instance!!.listener!!
    }

    fun setListener(listener: PaymentStatusListener) {
        instance!!.listener = listener
    }

    val isListenerRegistered: Boolean
        get() = instance!!.listener != null

    fun detachListener() {
        instance!!.listener = null
    }

    companion object {
        private var instance: Singleton? = null
        fun getInstance(): Singleton? {
            if (instance == null) {
                instance =
                    Singleton()
            }
            return instance
        }
    }
}