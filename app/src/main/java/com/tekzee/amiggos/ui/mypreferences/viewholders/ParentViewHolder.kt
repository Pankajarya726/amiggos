package com.tekzee.amiggos.ui.mypreferences.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tekzee.amiggos.R
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class ParentViewHolder(itemView: View) : GroupViewHolder(itemView) {

    val preference_txt_name = itemView.findViewById(R.id.preference_txt_name) as TextView
    val img_check_language = itemView.findViewById(R.id.img_check_language) as ImageView

    fun bindParentData(group: ExpandableGroup<*>?) {
        preference_txt_name.text = group!!.title.split("--")[0]
        img_check_language.setBackgroundResource(R.drawable.drop)
    }

    override fun expand() {
        super.expand()
        img_check_language.setBackgroundResource(R.drawable.dropup)
    }

    override fun collapse() {
        super.collapse()
        img_check_language.setBackgroundResource(R.drawable.drop)
    }


}
