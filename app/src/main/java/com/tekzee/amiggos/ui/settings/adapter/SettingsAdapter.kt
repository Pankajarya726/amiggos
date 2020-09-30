package com.tekzee.amiggos.ui.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.settings.interfaces.SettingsInterface
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import kotlinx.android.synthetic.main.single_settings.view.*

class SettingsAdapter(
    private val items: ArrayList<SettingsResponse.Data.Setting>,
    private val listener: SettingsInterface
): RecyclerView.Adapter<SettingsAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_settings,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {
        holder.bindingdata.s_txt_name.text = items[position].title
        if(items[position].type == "toggle"){
            holder.bindingdata.s_switch.visibility = View.VISIBLE
        }else{
            holder.bindingdata.s_switch.visibility = View.GONE
        }

        holder.bindingdata.s_switch.isChecked = items[position].isSet == 1

        holder.bindingdata.s_switch.setOnCheckedChangeListener{
            compoundButton, b ->
                listener.onItemClicked(items[position],b)
        }

        holder.bindingdata.s_txt_name.setOnClickListener{
            if(items[position].type=="url"){
                listener.onTextClicked(items[position])
            }
        }


    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


