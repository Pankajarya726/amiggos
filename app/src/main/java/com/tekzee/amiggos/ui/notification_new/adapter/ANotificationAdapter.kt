package com.tekzee.amiggos.ui.notification_new.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_notification.view.*


class ANotificationAdapter(
    var data: ArrayList<ANotificationResponse.Data.UserNotification>,
    var sharedPreference: SharedPreference?
)
    : RecyclerView.Adapter<ANotificationAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_notification, parent, false))
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {

            itemView.notification_title.text = data[adapterPosition].subject
            itemView.notification_description.text = data[adapterPosition].message

//            itemView.setOnClickListener {
//                mItemClickCallback?.let {
//                    mItemClickCallback.itemClickCallback(adapterPosition)
//                }
//            }
//
//            itemView.setOnLongClickListener(object: View.OnLongClickListener{
//                override fun onLongClick(p0: View?): Boolean {
//                    mItemClickCallback!!.onItemLongClickListener(adapterPosition)
//                    return true
//                }
//            })
        }
    }


}