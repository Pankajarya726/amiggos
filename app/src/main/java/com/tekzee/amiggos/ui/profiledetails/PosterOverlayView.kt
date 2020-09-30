package com.tekzee.amiggos.ui.profiledetails

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tekzee.amiggos.R

class PosterOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onDeleteClick: (String) -> Unit = {}


    init {
        View.inflate(context, R.layout.view_poster_overlay, this)
        findViewById<ImageView>(R.id.posterOverlayDeleteButton).setOnClickListener {
            onDeleteClick.invoke("clicked")
        }
        setBackgroundColor(Color.TRANSPARENT)
    }


}