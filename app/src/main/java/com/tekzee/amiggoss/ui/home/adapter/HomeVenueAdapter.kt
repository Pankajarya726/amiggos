package com.tekzee.amiggoss.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.chooseweek.ChooseWeekActivity
import com.tekzee.amiggoss.ui.home.model.NearestClub
import com.tekzee.amiggoss.ui.imagepanaroma.ImagePanaromaActivity
import com.tekzee.amiggoss.constant.ConstantLib


class HomeVenueAdapter(var venueListData: ArrayList<NearestClub>) : RecyclerView.Adapter<HomeVenueAdapter.ViewHolder>() {

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var isLoadingAdded = false
    private var context: Context? = null
   
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        if (viewType == holderRow) {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.single_venue_stories,
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.infinite_loading_progress_bar_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = venueListData.size

    override fun getItemViewType(position: Int): Int = when (venueListData[position].loadingStatus) {
        false -> holderRow
        else -> {
            holderLoading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {


            val layoutimage = itemView.findViewById(R.id.imageView6) as ImageView
            layoutimage.setOnClickListener{
                    val intent = Intent(context,ImagePanaromaActivity::class.java)
                    intent.putExtra(ConstantLib.PROFILE_IMAGE,venueListData[adapterPosition].panaroma_Image)
                    intent.putExtra(ConstantLib.CLUB_ID,venueListData[adapterPosition].club_id.toString())
                    intent.putExtra(ConstantLib.IS_REGULAR_IMAGE,venueListData[adapterPosition].is_regular_image)
                    intent.putExtra(ConstantLib.VIP_TABLE,venueListData[adapterPosition].vip_table)
                    context!!.startActivity(intent)
            }

            val bottleImage = itemView.findViewById(R.id.img_bottle) as ImageView
            if(venueListData[adapterPosition].vip_table == 1){

                bottleImage.visibility = View.VISIBLE
            }else{
                bottleImage.visibility = View.GONE
            }
            bottleImage.setOnClickListener{
                val intent = Intent(context, ChooseWeekActivity::class.java)
                intent.putExtra(ConstantLib.CLUB_ID,venueListData[adapterPosition].club_id.toString())
                context!!.startActivity(intent)
            }

            Glide.with(itemView.context).load(venueListData[adapterPosition].image).placeholder(R.drawable.blackbg).into(itemView.findViewById(R.id.imageView6))

            val txtAge = itemView.findViewById(R.id.txt_age) as TextView
            if (venueListData[adapterPosition].agelimit.isEmpty()) {
                txtAge.visibility = View.GONE
            } else {
                txtAge.visibility = View.VISIBLE
                txtAge.text = venueListData[adapterPosition].agelimit
            }

            val txtLanguage = itemView.findViewById(R.id.txt_language) as TextView
            val txtType = itemView.findViewById(R.id.txt_type) as TextView
            val txtCountry = itemView.findViewById(R.id.txt_country) as TextView

            val danceData = venueListData[adapterPosition].music_type.split(",");

            if (danceData.isEmpty()) {
                txtLanguage.visibility = View.GONE
                txtType.visibility = View.GONE
                txtCountry.visibility = View.GONE
            }

            if (danceData.isNotEmpty()) {
                txtType.text = danceData[0]
                txtType.visibility = View.VISIBLE
            }

            if (danceData.size>1) {
                txtLanguage.text = danceData[1]
                txtLanguage.visibility = View.VISIBLE
            }

            if (danceData.size>2) {
                txtCountry.text = danceData[2]
                txtCountry.visibility = View.VISIBLE
            }




            val txtBar = itemView.findViewById(R.id.txt_bar) as TextView
            val txtLaunge = itemView.findViewById(R.id.txt_lounge) as TextView
            val txtEvent = itemView.findViewById(R.id.txt_event) as TextView
            val club_name = itemView.findViewById(R.id.club_name) as TextView
            val distance = itemView.findViewById(R.id.distance) as TextView
            val address = itemView.findViewById(R.id.address) as TextView

            distance.text = "%.2f".format(venueListData!![adapterPosition].distance_from_mylocation) + " miles"
            club_name.text = venueListData!![adapterPosition].club_name
            address.text = venueListData!![adapterPosition].address

            val dataVenueType: List<String> = venueListData!![adapterPosition].venue_type.split(",")

            if (dataVenueType.isEmpty()) {
                txtBar.visibility = View.GONE
                txtLaunge.visibility = View.GONE
                txtEvent.visibility = View.GONE
            }

            if (dataVenueType.isNotEmpty()) {
                txtBar.text = dataVenueType[0]
                txtBar.visibility = View.VISIBLE
            }

            if (dataVenueType.size > 1) {
                txtLaunge.text = dataVenueType[1]
                txtLaunge.visibility = View.VISIBLE
            }

            if (dataVenueType.size > 2) {
                txtEvent.text = dataVenueType[2]
                txtEvent.visibility = View.VISIBLE
            }


        }
    }



    fun add(r: NearestClub) {
        r.loadingStatus =true
        venueListData.add(r)
        notifyItemInserted(venueListData.size - 1)
    }

    fun addAll(moveResults: List<NearestClub>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: NearestClub?) {
        val position = venueListData.indexOf(r)
        if (position > -1) {
            venueListData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(NearestClub())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = venueListData.size - 1
        val result = getItem(position)

        if (result != null) {
            venueListData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): NearestClub? {
        return venueListData.get(position)
    }


}