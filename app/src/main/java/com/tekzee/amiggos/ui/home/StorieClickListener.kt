package com.tekzee.amiggos.ui.home

import com.tekzee.amiggos.ui.home.model.StoriesData

interface StorieClickListener {
    fun onStorieClick(storiesData: StoriesData)
}