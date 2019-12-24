package com.nandroidex.upipayments.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.nandroidex.upipayments.R

class UPIAppsAdapter(
    private val mContext: Context?,
    private val mList: List<ResolveInfo>,
    private val mIntent: Intent
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.list_item_upi_apps, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = mList[position]
        val name = info.loadLabel(mContext?.packageManager).toString()
        val icon = info.loadIcon(mContext?.packageManager)
        holder.ivAppIcon.setImageDrawable(icon)
        holder.tvUPIAppName.text = name
        Log.e("TAG", "" + name)

        holder.itemView.setOnClickListener {
            val intent = mIntent
            intent.setPackage(info.activityInfo?.packageName)
            (mContext as Activity).startActivityForResult(
                intent,
                PaymentsUIActivity.PAYMENT_REQUEST
            )
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivAppIcon: AppCompatImageView = itemView.findViewById(R.id.ivAppIcon)
    val tvUPIAppName: AppCompatTextView = itemView.findViewById(R.id.tvUPIAppName)
}