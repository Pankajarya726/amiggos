package com.tekzee.amiggos.ui.turningup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.turningup.interfaces.TurningUpClicked
import com.tekzee.amiggos.ui.turningup.model.TurningUpData
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_list_language.view.txt_name
import kotlinx.android.synthetic.main.single_turning_up.view.*

class TurningAdapter(
    private val items: ArrayList<TurningUpData>,
    private val sharedPreferences: SharedPreference?,
    private val listener: TurningUpClicked
): RecyclerView.Adapter<TurningAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_turning_up,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {

        holder.bindingdata.txt_name.text = items[position].name
        holder.bindingdata.txt_venue_name.text = items[position].clubName
        holder.bindingdata.txt_date_time.text = items[position].partyTime.split(" ")[0]+"\n"+items[position].partyTime.split(" ")[1]+" "+items[position].partyTime.split(" ")[2]
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.noimage).into(holder.bindingdata.profile_image)
        holder.bindingdata.mainlayout.setOnClickListener{
            listener.onTuringUpClicked(position,items[position])
        }


    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


