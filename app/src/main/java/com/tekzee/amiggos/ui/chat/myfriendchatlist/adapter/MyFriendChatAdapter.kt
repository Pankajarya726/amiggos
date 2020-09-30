package com.tekzee.amiggos.ui.chat.myfriendchatlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.chat.myfriendchatlist.MyFriendChatListListener
import com.tekzee.amiggos.ui.chat.myfriendchatlist.model.MyFriendChatModel
import kotlinx.android.synthetic.main.single_myfriend_list.view.*
import java.text.SimpleDateFormat
import java.util.*


class MyFriendChatAdapter(
    private val items: List<MyFriendChatModel>,
    private val listener: MyFriendChatListListener
) : RecyclerView.Adapter<MyFriendChatAdapter.MyFriendViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFriendViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_myfriend_list, parent, false)
        return MyFriendViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyFriendViewHolder, position: Int) {


        holder.bindingdata.g_txt_name.text = items[position].name
        holder.bindingdata.txt_unread.text = items[position].unreadmessage
        if(items[position].unreadmessage.equals("0")){
            holder.bindingdata.txt_unread.visibility = View.GONE
        }else{
            holder.bindingdata.txt_unread.visibility = View.VISIBLE
        }
        holder.bindingdata.txt_timestamp.text = getDateTimeFromEpocLongOfSeconds(items[position].time!!.toLong()).toString()
        holder.bindingdata.txt_message.text = items[position].lastmessage
        Glide.with(context!!).load(items[position].image).placeholder(R.drawable.user)
            .into(holder.bindingdata.g_user_image)


        holder.bindingdata.mainlayout.setOnClickListener{
            listener.onMyFriendChatListClicked(items[position])
        }



    }

    inner class MyFriendViewHolder(val bindingdata: View) : RecyclerView.ViewHolder(bindingdata)



    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd-MMM",
                Locale.ENGLISH)
            val netDate = Date(epoc)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}


