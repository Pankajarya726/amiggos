package com.tekzee.amiggos.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.NearestClub
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView


class HomeVenueAdapter(
    val mContext: Context,
    mRecyclerView: RecyclerView,
    val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    var mDataList: ArrayList<NearestClub>?,
    val mItemClickCallback: HomeVenueItemClick?
) : RecyclerView.Adapter<HomeVenueAdapter.ViewHolder>() {

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(
            mContext,
            mRecyclerView,
            mLayoutManager,
            mRecyclerViewAdapterCallback
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

    override fun getItemCount(): Int = mDataList!!.size

    override fun getItemViewType(position: Int): Int = when (mDataList!![position].loadingStatus) {
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

            itemView.setOnClickListener {
                mItemClickCallback?.let {
                    mItemClickCallback.itemVenueClickCallback(adapterPosition)
                }
            }

            Glide.with(itemView.context).load(mDataList!![adapterPosition].image).placeholder(R.drawable.user).into(itemView.findViewById(R.id.imageView6))

            val txtAge = itemView.findViewById(R.id.txt_age) as TextView
            if (mDataList!![adapterPosition].agelimit.isEmpty()) {
                txtAge.visibility = View.GONE
            } else {
                txtAge.visibility = View.VISIBLE
                txtAge.text = mDataList!![adapterPosition].agelimit
            }

            val txtLanguage = itemView.findViewById(R.id.txt_language) as TextView
            val txtType = itemView.findViewById(R.id.txt_type) as TextView
            val txtCountry = itemView.findViewById(R.id.txt_country) as TextView

            val danceData = mDataList!![adapterPosition].music_type.split(",");

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

            distance.text = "%.2f".format(mDataList!![adapterPosition].distance_from_mylocation) + " miles"
            club_name.text = mDataList!![adapterPosition].club_name
            address.text = mDataList!![adapterPosition].address

            val dataVenueType: List<String> = mDataList!![adapterPosition].venue_type.split(",")

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

    interface HomeVenueItemClick {
        fun itemVenueClickCallback(position: Int)
    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
    }
}