package com.tekzee.amiggos.ui.bookings_new.bookings.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.bookings_new.bookings.BookingClickedListener
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import kotlinx.android.synthetic.main.a_single_booking_fragment.view.*
import java.util.*


class BookingAdapter(
    var mDataList: ArrayList<ABookingResponse.Data.BookingData>,
    var listner: BookingClickedListener
)
    : RecyclerView.Adapter<BookingAdapter.ViewHolder>(){


    private var context: Context? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context = parent.context
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.a_single_booking_fragment, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(context!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(context: Context) {
            itemView.txt_booking_id.text = mDataList[adapterPosition].name
            itemView.txt_zone.text = mDataList[adapterPosition].booking_time
            itemView.txt_date.text = mDataList[adapterPosition].booking_date
            Glide.with(context).load(mDataList[adapterPosition].venue_home_image).into(itemView.profile_image)
            itemView.mainlayout.setOnClickListener {
                listner.onBookingClicked(mDataList[adapterPosition])
            }
        }
    }

 }