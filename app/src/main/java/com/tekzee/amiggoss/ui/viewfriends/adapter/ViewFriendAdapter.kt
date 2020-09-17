package com.tekzee.amiggoss.ui.viewfriends.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.viewfriends.ViewFriendInterface
import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewData
import com.tekzee.amiggoss.util.SharedPreference
import kotlinx.android.synthetic.main.single_list_language.view.*

class ViewFriendAdapter(
    private val items: ArrayList<StorieViewData>,
    private val sharedPreferences: SharedPreference?,
    private val listener: ViewFriendInterface
): RecyclerView.Adapter<ViewFriendAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_storieview,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {
        holder.bindingdata.txt_name.text = items[position].name
        holder.bindingdata.mainlayout.setOnClickListener{
            listener.onFriendClicked(position,items[position])

        }

    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


