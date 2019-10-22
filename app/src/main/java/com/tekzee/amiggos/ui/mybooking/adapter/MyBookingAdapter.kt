package com.tekzee.amiggos.ui.mybooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.mybooking.BookingClicked
import com.tekzee.amiggos.ui.mybooking.model.MyBookingData
import kotlinx.android.synthetic.main.single_booking.view.*

class MyBookingAdapter(
    private val items: ArrayList<MyBookingData>,
    private val listener: BookingClicked
): RecyclerView.Adapter<MyBookingAdapter.BookingViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_booking,parent,false)
        return BookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {

        holder.bindingdata.txt_venue_name.text = items[position].clubName
        holder.bindingdata.txt_date.setText(items[position].dateTime)
        holder.bindingdata.txt_amount.setText("Paid : "+items[position].symbolLeft+" "+items[position].price)
        Glide.with(context!!).load(items[position].qrCode).into(holder.bindingdata.img_barcode)
        holder.bindingdata.mainlayout.setOnClickListener{
            listener.onBookingClicked(position,items[position])

        }

    }

    inner class BookingViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


