package com.tekzee.amiggos.ui.choosepackage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.choosepackage.interfaces.ChoosePackageInterface
import com.tekzee.amiggos.ui.choosepackage.model.PackageData
import kotlinx.android.synthetic.main.single_choose_package.view.*

class ChoosePackageAdapter(
    private val items: ArrayList<PackageData>,
    private val languageData: LanguageData,
    var listener: ChoosePackageInterface
): RecyclerView.Adapter<ChoosePackageAdapter.ChoosePackageViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoosePackageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_choose_package,parent,false)
        return ChoosePackageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChoosePackageViewHolder, position: Int) {

        Glide.with(context!!).load(items[position].packageImage)
            .placeholder(R.drawable.noimage).into(holder.bindingdata.user_image)
        holder.bindingdata.txt_seat.text = items[position].noSeat +"\n"+ languageData.kllblSeatsTitle
        holder.bindingdata.txt_package_name.text = items[position].packageName
        holder.bindingdata.price.text = items[position].symbolLeft+items[position].price.toString()
        holder.bindingdata.time.text = items[position].endTime

//        holder.bindingdata.txt_name.text = items[position].value
        holder.bindingdata.mainlayout.setOnClickListener{
            listener.onClickWeek(items[position])
        }
    }

    inner class ChoosePackageViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


