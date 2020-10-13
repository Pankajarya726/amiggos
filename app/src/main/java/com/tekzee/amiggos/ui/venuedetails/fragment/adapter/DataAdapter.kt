package com.tekzee.amiggos.ui.venuedetails.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
import kotlinx.android.synthetic.main.single_data.view.*


class DataAdapter(var venueData: List<ClubDetailResponse.Data.ClubData.VenueData>) : RecyclerView.Adapter<DataAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_data,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return venueData.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {
        holder.bindingdata.txt_name.text = venueData[position].value
        Glide.with(context!!).load(venueData[position].imageIcon).placeholder(R.drawable.noimage).into(holder.bindingdata.img_flag)
    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


