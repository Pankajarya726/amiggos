package com.tekzee.amiggos.ui.memories.ourmemories

import com.tekzee.amiggos.ui.memories.ourmemories.model.GetOurMemoriesResponse

interface FeaturedBrandsClickListener {
    fun OnFeaturedBrandsClicked(featuredBrandsData: GetOurMemoriesResponse.Data.FeaturedProduct)
}