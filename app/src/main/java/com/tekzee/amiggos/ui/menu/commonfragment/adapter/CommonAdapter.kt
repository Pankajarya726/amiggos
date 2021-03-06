package com.tekzee.amiggos.ui.menu.commonfragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.SingleCommonStaffListBinding
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.menu.commonfragment.CommonClickListener
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import kotlinx.android.synthetic.main.single_common_staff_list.view.*


class CommonAdapter(
    private val listener: CommonClickListener,
    private val repository: ItemRepository?,
    private val prefs: SharedPreference
) :
    ListAdapter<Menu, CommonAdapter.CommonStaffViewHolder>(
        CommonDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonStaffViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleCommonStaffListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_common_staff_list, parent, false
        )
        return CommonStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonStaffViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem, context)
            if(listItem.price.toFloat()>0.0){
                holder.listitembinding.txtAmout.text = Utility.formatCurrency(listItem.price.toFloat())
                holder.listitembinding.txtAmout.setTextColor(context!!.resources.getColor(R.color.black))
            }else{
                holder.listitembinding.txtAmout.text = "Free"
                holder.listitembinding.txtAmout.setTextColor(context!!.resources.getColor(R.color.green))
            }


            Coroutines.main {
                val dataResponse = repository!!.getItemDetail(listItem!!.id.toString())
                if (dataResponse != null) {
                    Log.e("quantity new-------->", dataResponse.quantity.toString())
                    holder.listitembinding.txtQty.text = dataResponse.quantity.toString()
                } else {
                    holder.listitembinding.txtQty.text = "0"
                }
            }
            holder.listitembinding.txtPlus.setOnClickListener {
//                if(prefs.getValueBoolean(ConstantLib.MYID,false)){
//                    val quantity =
//                        Integer.parseInt(holder.listitembinding.txtQty.text.toString()) + 1
//                    holder.listitembinding.txtQty.setText(quantity.toString())
//                    listItem.quantity = quantity
//                    listener.onItemClicked(position, listItem, quantity.toString())
//                    return@setOnClickListener
//                }


                if (listItem.ageRestriction.equals("1",true) ) {
                    if(listItem.is_idproof_uploaded.equals("0")){
                        listener.showAgeRestrictionPopup(holder.listitembinding.txtPlus)
                    }else if(listItem.is_idproof_verified.equals("0")){
                        listener.showNotVerifiedMessage(listItem.is_idproof_notverified_message)
                    }else{

                        if(Integer.parseInt(holder.listitembinding.txtQty.text.toString())==0 && listItem.ageRestriction.equals("1",true)){
                            listener.showAlertForAgeRestriction(listItem.warning_age_restriction)
                        }
                        val quantity =
                            Integer.parseInt(holder.listitembinding.txtQty.text.toString()) + 1
                        holder.listitembinding.txtQty.setText(quantity.toString())
                        listItem.quantity = quantity
                        listener.onItemClicked(position, listItem, quantity.toString())
                    }
                } else {
                    val quantity =
                        Integer.parseInt(holder.listitembinding.txtQty.text.toString()) + 1
                    holder.listitembinding.txtQty.setText(quantity.toString())
                    listItem.quantity = quantity
                    listener.onItemClicked(position, listItem, quantity.toString())
                }

            }

            holder.listitembinding.menuimage.setOnClickListener {
                listener.viewImage(listItem)
            }

            holder.listitembinding.txtMinus.setOnClickListener {
                if(prefs.getValueBoolean(ConstantLib.MYID,false)){
                    val quantity =
                        Integer.parseInt(holder.listitembinding.txtQty.text.toString()) - 1
                    if (quantity >= 0) {
                        holder.listitembinding.txtQty.text = quantity.toString()
                        listener.onItemClicked(position, listItem, quantity.toString())
                    }
                    return@setOnClickListener
                }
                if (listItem.ageRestriction.isNotEmpty() && prefs.getValueInt(ConstantLib.USER_AGE)<= Integer.parseInt(listItem.ageRestriction) ) {
                    listener.showAgeRestrictionPopup(holder.listitembinding.txtMinus)
                } else {
                    val quantity =
                        Integer.parseInt(holder.listitembinding.txtQty.text.toString()) - 1
                    if (quantity >= 0) {
                        holder.listitembinding.txtQty.text = quantity.toString()
                        listener.onItemClicked(position, listItem, quantity.toString())
                    }
                }

            }
        }
    }

    class CommonStaffViewHolder(val listitembinding: SingleCommonStaffListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: Menu?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.menuImage)
                .placeholder(R.drawable.noimage).into(itemView.menuimage)
            listitembinding.commonitem = listItem
            listitembinding.executePendingBindings()
        }
    }


}