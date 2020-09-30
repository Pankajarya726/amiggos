package com.tekzee.amiggos.ui.mylifestyle.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import ru.alexbykov.nopaginate.callback.OnRepeatListener
import ru.alexbykov.nopaginate.item.ErrorItem

class CustomErrorItem : ErrorItem {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_error, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        repeatListener: OnRepeatListener
    ) {
        val btnRepeat =
            holder.itemView.findViewById<View>(R.id.btnRepeat) as Button
        btnRepeat.setOnClickListener { repeatListener?.onClickRepeat() }
    }
}