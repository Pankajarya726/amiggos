package com.tekzee.amiggoss.ui.notification_new.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.constant.ConstantLib
import com.tekzee.amiggoss.enums.FriendsAction
import com.tekzee.amiggoss.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggoss.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggoss.util.SharedPreference
import kotlinx.android.synthetic.main.single_notification.view.*


class ANotificationAdapterFriend(
    var data: ArrayList<ANotificationResponse.Data.UserNotification>,
    var sharedPreference: SharedPreference?
)
    : RecyclerView.Adapter<ANotificationAdapterFriend.ViewHolder>(){
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

                val intent = Intent(context!!, AHomeScreen::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = FriendsAction.CLICK.action
                    putExtra(ConstantLib.SUB_TAB, 2)
                }
                context!!.startActivity(intent)
            }
        }
    }


}