package com.tekzee.amiggoss.ui.venuedetails

import com.tekzee.amiggoss.ui.venuedetailsnew.model.ClubDetailResponse

interface ImageClickListener {

    fun onItemClick(imageVideoData: ClubDetailResponse.Data.ClubData.ImageVideoData);
}