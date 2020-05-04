package com.tekzee.amiggos.ui.notification_new.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_notification.view.*


class ANotificationAdapter(
    var data: ArrayList<ANotificationResponse.Data.UserNotification>,
    var sharedPreference: SharedPreference?

)
    : RecyclerView.Adapter<ANotificationAdapter.ViewHolder>(){
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context = parent.context
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

            itemView.setOnClickListener {
//                val intent  = Intent(context,AHomeScreen::class.java)
//                intent.putExtra(ConstantLib.FROM,"FRIENDNOTIFICATION")
//                context!!.startActivity(intent)
            }
        }
    }


}