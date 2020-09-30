package com.tekzee.amiggos.ui.chooselanguage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.chooselanguage.interfaces.LanguageClicked
import com.tekzee.amiggos.ui.chooselanguage.model.Language
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_list_language.view.*

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
        holder.bindingdata.txt_name.text = items[position].name
        Glide.with(context!!).load(items[position].image).into(holder.bindingdata.profile_image)
        holder.bindingdata.language_mainlayout.setOnClickListener {
            listener.onLanguageClicked(position,items[position])
        }
    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


