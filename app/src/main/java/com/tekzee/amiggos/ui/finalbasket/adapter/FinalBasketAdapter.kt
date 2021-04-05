package com.tekzee.amiggos.ui.finalbasket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.databinding.SingleFinalbasketListBinding
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.finalbasket.FinalBasketClickListener
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.single_common_staff_list.view.*
import java.text.DecimalFormat


class FinalBasketAdapter(
    private val listener: FinalBasketClickListener,
    private val repository: ItemRepository?,
    private val prefs: SharedPreference
) :
    ListAdapter<Menu, FinalBasketAdapter.FinalBasketStaffViewHolder>(
        FinalBasketDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalBasketStaffViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleFinalbasketListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_finalbasket_list, parent, false
        )
        return FinalBasketStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinalBasketStaffViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem, context)
            holder.listitembinding.menuimage.setOnClickListener {
                listener.viewImage(listItem)
            }

            holder.listitembinding.imgDelete.setOnClickListener {
                listener.onItemClicked(position,listItem,"0")
            }

        }
    }

    class FinalBasketStaffViewHolder(val listitembinding: SingleFinalbasketListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
//        private val df2: DecimalFormat = DecimalFormat("#.##")
        fun bind(
            listItem: Menu?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.menuImage)
                .placeholder(R.drawable.noimage).into(itemView.menuimage)
            listitembinding.commonitem = listItem
            listitembinding.executePendingBindings()
            listitembinding.amout.text = "$"+listItem.price
        }
    }


}