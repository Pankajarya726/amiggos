package com.tekzee.amiggos.ui.mypreferences.viewholders

import android.view.View
import android.widget.Checkable
import android.widget.CheckedTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.mypreferences.model.Value
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder

class ChildSingleCheckViewHolder(itemView: View) : CheckableChildViewHolder(itemView) {

    val txt_single_check = itemView.findViewById(R.id.txt_single_check) as CheckedTextView

    override fun getCheckable(): Checkable {
        return txt_single_check
    }

    fun bindData(childDataItem: Value) {
        txt_single_check.text = childDataItem.name
    }

}