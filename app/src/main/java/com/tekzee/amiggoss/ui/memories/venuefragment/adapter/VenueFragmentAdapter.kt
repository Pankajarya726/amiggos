package com.tekzee.amiggoss.ui.memories.venuefragment.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.memories.venuefragment.model.VenueTaggedResponse
import kotlinx.android.synthetic.main.single_first_fragment.view.*
import java.util.ArrayList


class VenueFragmentAdapter(
    var mDataList: ArrayList<VenueTaggedResponse.Data.TaggedVenue>
)
    : RecyclerView.Adapter<VenueFragmentAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_first_fragment, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Glide.with(itemView.context).load(mDataList[adapterPosition].image).placeholder(R.drawable.blackbg).into(itemView.img_user_firstfragment)
        }
    }

 }