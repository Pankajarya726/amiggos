package com.tekzee.amiggoss.ui.homescreen_new.homefragment

import com.tekzee.amiggoss.ui.homescreen_new.homefragment.model.HomeApiResponse

class MarkerCompartor(): Comparator< HomeApiResponse.Data.NearestClub > {

    override fun compare(
        lhs: HomeApiResponse.Data.NearestClub?,
        rhs: HomeApiResponse.Data.NearestClub?
    ): Int {
        if (lhs!!.distanceFromMylocation != rhs!!.distanceFromMylocation) {
            return return if (lhs.distanceFromMylocation < rhs.distanceFromMylocation) -1 else 1
        }else {
            return 1
        }
    }

}