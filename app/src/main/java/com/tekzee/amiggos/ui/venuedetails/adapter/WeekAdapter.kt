package com.tekzee.amiggos.ui.venuedetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import kotlinx.android.synthetic.main.single_settings.view.*
import kotlinx.android.synthetic.main.week_single_list.view.*

class WeekAdapter(
    private val items: List<VenueDetails.Data.ClubData.WorkingDay>,
): RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.week_single_list,parent,false)
        return WeekViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {

            holder.bindingdata.txt_week_name.text = items[position].name
            holder.bindingdata.txt_timinings.text = items[position].timing

    }

    inner class WeekViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


