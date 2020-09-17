package com.tekzee.amiggoss.ui.chooselanguage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.chooselanguage.interfaces.LanguageClicked
import com.tekzee.amiggoss.ui.chooselanguage.model.Language
import com.tekzee.amiggoss.util.SharedPreference
import kotlinx.android.synthetic.main.single_list_language.view.*
import kotlinx.android.synthetic.main.single_list_language.view.s_switch

class LanguageAdapter(
    private val items: ArrayList<Language>,
    private val sharedPreferences: SharedPreference?,
    private val listener: LanguageClicked
): RecyclerView.Adapter<LanguageAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_list_language,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {

        holder.bindingdata.s_switch.isChecked = items[position].isChecked

        holder.bindingdata.s_switch.setOnCheckedChangeListener{
                compoundButton, b ->
            if(b){
                listener.onLanguageClicked(position,b,items[position])
            }

        }
        holder.bindingdata.txt_name.text = items[position].name

    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


