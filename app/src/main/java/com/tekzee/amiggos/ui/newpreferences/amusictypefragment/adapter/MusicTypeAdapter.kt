package com.tekzee.amiggos.ui.newpreferences.amusictypefragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import kotlinx.android.synthetic.main.venue_type_single_list.view.*


class MusicTypeAdapter(
    val mDataList: ArrayList<AMusicTypeResponse.Data.MusicType>
) : RecyclerView.Adapter<MusicTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.venue_type_single_list, parent, false)
        val height = parent.measuredWidth / 3
        return ViewHolder(view,height)

    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.d(mDataList[position].music)
        holder.bind()
    }

    inner class ViewHolder(itemView: View, var height: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.txt_name.text = mDataList[adapterPosition].music
            itemView.mainlayout.minimumHeight = height
        }
    }


}