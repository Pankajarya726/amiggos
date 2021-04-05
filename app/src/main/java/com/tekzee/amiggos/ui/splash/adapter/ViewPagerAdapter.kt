//package com.tekzee.amiggos.ui.splash.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.tekzee.amiggos.ui.splash.model.ViewPageData
//import android.content.Context
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.tekzee.amiggos.R
//
//
//class ViewPagerAdapter(
//    private var data: ArrayList<ViewPageData>,
//    private var applicationContext: Context
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return object : RecyclerView.ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.material_page, parent, false)) {
//
//        }
//    }
//
//    override fun getItemCount() = data.size
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            holder.itemView.findViewById<TextView>(R.id.title).setText(data[position].title)
//            holder.itemView.findViewById<TextView>(R.id.description).setText(data[position].description)
//    }
//}