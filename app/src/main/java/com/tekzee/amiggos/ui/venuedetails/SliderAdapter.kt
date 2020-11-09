//package com.tekzee.amiggos.ui.venuedetails
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.smarteist.autoimageslider.SliderViewAdapter
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
//
//
//class SliderAdapter(
//    var data: List<ClubDetailResponse.Data.ClubData.ImageVideoData>,
//    var listener: ImageClickListener
//): SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
//    private var context: Context? = null
//
//
//    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
//        context = parent!!.context
//        val inflate = LayoutInflater.from(parent!!.getContext()).inflate(R.layout.image_slider_layout_item, null);
//        return SliderViewHolder(inflate)
//    }
//
//    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
//        if(data[position].isImage ==1){
//            Glide.with(context!!)
//                .load(data[position].url)
//                .into(viewHolder!!.image)
//        }else{
//            Glide.with(context!!)
//                .load(R.drawable.user)
//                .into(viewHolder!!.image)
//        }
//
//        viewHolder.image.setOnClickListener{
//            listener.onItemClick(data[position])
//        }
//
//    }
//
//    override fun getCount(): Int {
//        return data.size
//    }
//
//
//
//    class SliderViewHolder(itemview: View): SliderViewAdapter.ViewHolder(itemview) {
//
//        var image: ImageView = itemview.findViewById(R.id.img_slider) as ImageView
//
//    }
//}