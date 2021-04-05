//package com.tekzee.amiggos.ui.memories.ourmemories.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.memories.ourmemories.FeaturedBrandsClickListener
//import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
//import kotlinx.android.synthetic.main.single_first_fragment.view.*
//
//
//class FeaturedBrandAdapter(
//    var mDataList: ArrayList<MemorieResponse.Data.Memories>,
//    var listenerFragment: FeaturedBrandsClickListener
//) : RecyclerView.Adapter<FeaturedBrandAdapter.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.single_featuredbrand,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int = mDataList.size
//
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind()
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind() {
//
//            Glide.with(itemView.context).load(mDataList[adapterPosition].profile)
//                .placeholder(R.drawable.blackbg).into(itemView.img_user_firstfragment)
//
//            itemView.img_user_firstfragment.setOnClickListener {
////                listenerFragment.OnFeaturedBrandsClicked(mDataList[adapterPosition])
//            }
//        }
//
//    }
//
//
//}