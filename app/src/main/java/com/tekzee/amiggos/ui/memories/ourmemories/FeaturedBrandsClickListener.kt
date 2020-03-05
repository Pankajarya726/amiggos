package com.tekzee.amiggos.ui.memories.ourmemories

import com.tekzee.amiggos.ui.memories.ourmemories.model.FeaturedBrandProductResponse

interface FeaturedBrandsClickListener {
    fun OnFeaturedBrandsClicked(featuredBrandsData: FeaturedBrandProductResponse.Data.FeaturedProduct)
}