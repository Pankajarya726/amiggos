package com.tekzee.amiggos.ui.helpcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.helpcenter.interfaces.HelpCenterInterface
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterData
import kotlinx.android.synthetic.main.single_helpcenter.view.*
import kotlinx.android.synthetic.main.single_settings.view.*

class HelpCenterAdapter(
    private val items: ArrayList<HelpCenterData>,
    private val listener: HelpCenterInterface
): RecyclerView.Adapter<HelpCenterAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_helpcenter,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {
        holder.bindingdata.h_txt_name.text = items[position].title

        holder.bindingdata.mainlayout.setOnClickListener{
           listener.onHelpCenterClicked(items[position])
        }


    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


