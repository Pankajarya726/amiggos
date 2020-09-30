package com.tekzee.amiggos.ui.mylifestyle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.MylifestyleSingleListBinding
import com.tekzee.amiggos.ui.mylifestyle.MyLifestyleClickListener
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse


class MyLifestyleAdapter(
    private val listener: MyLifestyleClickListener,
    private val response: List<MyLifestyleResponse.Data.Lifestyle>
) :
    ListAdapter<MyLifestyleResponse.Data.Lifestyle, MyLifestyleAdapter.MyLifestyleViewHolder>(
        MyLifestyleDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLifestyleViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: MylifestyleSingleListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.mylifestyle_single_list, parent, false
        )
        return MyLifestyleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyLifestyleViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context,response)
            holder.itemView.setOnClickListener {
                listener.onItemClick(listItem)
            }
        }
    }

    class MyLifestyleViewHolder(val listitembinding: MylifestyleSingleListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(listItem: MyLifestyleResponse.Data.Lifestyle, context: Context?, response: List<MyLifestyleResponse.Data.Lifestyle>) {
            Glide.with(context!!).load(listItem.catImage).placeholder(R.drawable.header_logo).into(listitembinding.imgUserFirstfragment)
            listitembinding.childCount.setText(listItem.children_count.toString())
            if(listItem.children_count > 0){
                listitembinding.childCount.visibility= View.VISIBLE
            }else
            {
                listitembinding.childCount.visibility= View.GONE
            }
            listitembinding.executePendingBindings()
        }
    }


}