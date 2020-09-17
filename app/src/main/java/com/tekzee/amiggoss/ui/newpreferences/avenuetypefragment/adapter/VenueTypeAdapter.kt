package com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import kotlinx.android.synthetic.main.venue_type_single_list.view.*


class VenueTypeAdapter(
    val mDataList: ArrayList<AVenueTypeResponse.Data.VenueType>,
    val width: Int
) : RecyclerView.Adapter<VenueTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.venue_type_single_list, parent, false)
        val height = parent.measuredWidth / 3
        return ViewHolder(view,height)

    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.d(mDataList[position].venueType)
        holder.bind()
    }

    inner class ViewHolder(itemView: View, var height: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.txt_name.text = mDataList[adapterPosition].venueType
            itemView.mainlayout.minimumHeight = height
        }
    }


}