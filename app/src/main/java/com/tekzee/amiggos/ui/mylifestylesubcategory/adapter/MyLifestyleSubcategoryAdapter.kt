package com.tekzee.amiggos.ui.mylifestylesubcategory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.MylifestylesubcategorySingleListBinding
import com.tekzee.amiggos.ui.mylifestylesubcategory.MyLifestyleSubcategoryClickListener
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse


class MyLifestyleSubcategoryAdapter(
    private val listener: MyLifestyleSubcategoryClickListener,
    private val response: List<MyLifestyleSubcategoryResponse.Data.Lifestyle>
) :
    ListAdapter<MyLifestyleSubcategoryResponse.Data.Lifestyle, MyLifestyleSubcategoryAdapter.MyLifestyleSubcategoryViewHolder>(
        MyLifestyleSubcategoryDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyLifestyleSubcategoryViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: MylifestylesubcategorySingleListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.mylifestylesubcategory_single_list, parent, false
        )
        return MyLifestyleSubcategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyLifestyleSubcategoryViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem, context, response)
            holder.itemView.setOnClickListener {
                listener.onItemClick(listItem, position)
            }
        }
    }

    class MyLifestyleSubcategoryViewHolder(val listitembinding: MylifestylesubcategorySingleListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: MyLifestyleSubcategoryResponse.Data.Lifestyle,
            context: Context?,
            response: List<MyLifestyleSubcategoryResponse.Data.Lifestyle>
        ) {
//            Glide.with(context!!).load(listItem.catImage).placeholder(R.drawable.header_logo).into(listitembinding.imgUserFirstfragment)
            listitembinding.subCatName.text = listItem.name
            if (listItem.selected == 1) {
                listitembinding.imgLayout.setBackgroundDrawable(context!!.resources.getDrawable(R.drawable.white_ring))
                listitembinding.childCount.visibility = View.VISIBLE
            } else {
                listitembinding.childCount.visibility = View.GONE
                listitembinding.imgLayout.setBackgroundDrawable(context!!.resources.getDrawable(R.drawable.yellow_ring))
            }
            listitembinding.executePendingBindings()
        }
    }


}