package com.tekzee.amiggos.ui.viewmenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.impulsiveweb.galleryview.GalleryView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_view_menu.view.*

class ViewMenuAdapter(
    private val items: List<BookingDetailsNewResponse.Data.Booking.Menu>,
    private val sharedPreferences: SharedPreference?,
    private val languageData: LanguageData?
): RecyclerView.Adapter<ViewMenuAdapter.PendingJobViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_view_menu,parent,false)
        return PendingJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PendingJobViewHolder, position: Int) {

        holder.bindingdata.txt_name.text = items[position].name
        holder.bindingdata.txt_description.text = items[position].description
        holder.bindingdata.txt_amout.text = languageData!!.quantity+items[position].qty
        holder.bindingdata.price.text = languageData!!.price+"$"+items[position].price
        Glide.with(context!!).load(items[position].menuImage).placeholder(R.drawable.user).placeholder(R.drawable.noimage).into(holder.bindingdata.menuimage)
        holder.bindingdata.menuimage.setOnClickListener {
            if(items[position].menuImage.isNotEmpty()){
                val paths: ArrayList<String> = ArrayList()
                paths.add(items[position].menuImage)
                GalleryView.show(context, paths)
            }
        }

    }

    inner class PendingJobViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


