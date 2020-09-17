package com.tekzee.amiggoss.ui.chooseweek.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.chooseweek.interfaces.ChooseWeekInterface
import com.tekzee.amiggoss.ui.chooseweek.model.DayData
import kotlinx.android.synthetic.main.single_list_language.view.*

class ChooseWeekAdapter(
    private val items: ArrayList<DayData>,
    var listener: ChooseWeekInterface
): RecyclerView.Adapter<ChooseWeekAdapter.ChooseWeekViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseWeekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_choose_week,parent,false)
        return ChooseWeekViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChooseWeekViewHolder, position: Int) {
        holder.bindingdata.txt_name.text = items[position].value
        holder.bindingdata.txt_name.setOnClickListener{
            listener.onClickWeek(items[position])
        }
    }

    inner class ChooseWeekViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


