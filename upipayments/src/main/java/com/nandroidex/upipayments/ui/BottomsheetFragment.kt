package com.nandroidex.upipayments.ui


import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nandroidex.upipayments.R
import kotlinx.android.synthetic.main.fragment_bottomsheet.view.*


class BottomsheetFragment(
    private val mList: List<ResolveInfo>,
    private val mIntent: Intent,
    private val mListener: View.OnClickListener
) : BottomSheetDialogFragment() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapterUPI: UPIAppsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        isCancelable = false

        mRecyclerView = view.findViewById(R.id.recycler_view)
        view.ibClose.setOnClickListener(mListener)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val manager = GridLayoutManager(activity, 4)
        mRecyclerView!!.layoutManager = manager
        mAdapterUPI = UPIAppsAdapter(activity, mList, mIntent)
        mRecyclerView!!.adapter = mAdapterUPI
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}
