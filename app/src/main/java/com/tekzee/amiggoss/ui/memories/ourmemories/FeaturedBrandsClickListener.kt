package com.tekzee.amiggoss.ui.memories.ourmemories

import com.tekzee.amiggoss.ui.memories.ourmemories.model.FeaturedBrandProductResponse

interface FeaturedBrandsClickListener {
    fun OnFeaturedBrandsClicked(featuredBrandsData: FeaturedBrandProductResponse.Data.FeaturedProduct)
}