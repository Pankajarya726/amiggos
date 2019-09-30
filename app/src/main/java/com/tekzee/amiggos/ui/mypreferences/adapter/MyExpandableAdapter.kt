package com.tekzee.amiggos.ui.mypreferences.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.mypreferences.interfaces.MyPreferenceClicked
import com.tekzee.amiggos.ui.mypreferences.model.ParentData
import com.tekzee.amiggos.ui.mypreferences.model.Value

import com.tekzee.amiggos.ui.mypreferences.viewholders.ParentViewHolder
import com.tekzee.mallortaxi.util.SharedPreference
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import io.apptik.widget.MultiSlider


class MyExpandableAdapter(
    groups: ArrayList<ParentData>,
    sharedPreference: SharedPreference?,
    param: MyPreferenceClicked
) : MultiTypeExpandableRecyclerViewAdapter<ParentViewHolder, ChildViewHolder>(groups) {

    var listOfValue = ArrayList<Value>()
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.single_myprefrence_list, parent, false)
        return ParentViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): ChildViewHolder {
        when (viewType) {
            MULTIPLE_CHECK_BOX_VIEW -> {
                val inflater = LayoutInflater.from(parent!!.context)
                val view = inflater.inflate(R.layout.single_checkbox_layout, parent, false)
                return ChildMultipleCheckViewHolder(view)
            }
            SLIDER_VIEW -> {
                val inflater = LayoutInflater.from(parent!!.context)
                val view = inflater.inflate(R.layout.single_slider_layout, parent, false)
                return ChildSliderViewHolder(view)
            }
        }
        throw IllegalArgumentException("Invalid viewType");

    }

    override fun onBindChildViewHolder(
        holder: ChildViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        childIndex: Int
    ) {
        val viewType = getItemViewType(flatPosition)
        var childDataItem = group!!.items[childIndex] as Value
        listOfValue = group.items as ArrayList<Value>

        when (viewType) {
            MULTIPLE_CHECK_BOX_VIEW -> {
                val mholder = holder as ChildMultipleCheckViewHolder
                mholder.bindData(
                    childDataItem,
                    flatPosition,
                    group.title.split("--")[2]
                )
            }
            SLIDER_VIEW -> {
                val mholder = holder as ChildSliderViewHolder
                mholder.bindData(childDataItem, flatPosition)
            }
        }

    }

    override fun onBindGroupViewHolder(
        holder: ParentViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holder!!.bindParentData(group)
    }


    companion object {
        private const val MULTIPLE_CHECK_BOX_VIEW = 1
        private const val SLIDER_VIEW = 3
    }


    override fun getChildViewType(position: Int, group: ExpandableGroup<*>?, childIndex: Int): Int {
        val type: String = group!!.title.split("--")[1]
        val title: String = group.title.split("--")[0]

        if (type.equals("range", true)) {
            return SLIDER_VIEW
        } else {
            return MULTIPLE_CHECK_BOX_VIEW
        }
    }

    override fun isChild(viewType: Int): Boolean {
        return viewType == MULTIPLE_CHECK_BOX_VIEW || viewType == SLIDER_VIEW

    }

    inner class ChildSliderViewHolder(itemView: View) : ChildViewHolder(itemView) {

        var range_slider = itemView.findViewById(R.id.range_slider) as MultiSlider
        var valueslider = itemView.findViewById(R.id.valueslider) as TextView

        fun bindData(
            childDataItem: Value,
            flatPosition: Int
        ) {


            valueslider.setText("From " + childDataItem.filter_from + " Till " + childDataItem.filter_to)
            var thumb1 = range_slider.getThumb(0)
            var thumb2 = range_slider.getThumb(1)
            thumb1.setValue(childDataItem.filter_from.toInt())
            thumb2.setValue(childDataItem.filter_to.toInt())



            range_slider.setOnThumbValueChangeListener { multiSlider, thumb, thumbIndex, value ->
                if (thumbIndex == 0) {
                    valueslider.text = "From " + value + " Till " + childDataItem.filter_to
                    childDataItem.filter_from = value.toString()
                } else {
                    valueslider.text = "From " + childDataItem.filter_from + " Till " + value
                    childDataItem.filter_to = value.toString()
                }
            }

        }
    }

    inner class ChildMultipleCheckViewHolder(itemView: View) : ChildViewHolder(itemView) {

        val txt_single_check = itemView.findViewById(R.id.txt_single_check) as TextView
        val mainlayout = itemView.findViewById(R.id.mainlayout) as ConstraintLayout
        val img_check = itemView.findViewById(R.id.img_check) as ImageView

        fun bindData(
            childDataItem: Value,
            flatPosition: Int,
            maxCount: String
        ) {
            txt_single_check.text = childDataItem.name
            if (childDataItem.isSet == "1") {
                img_check.setBackgroundResource(R.drawable.check)
            } else {
                img_check.setBackgroundResource(R.drawable.transparent)
            }


            mainlayout.setOnClickListener {

                if (childDataItem.isSet == "1") {
                    childDataItem.isSet = "0"
                } else {
                    if(checkMaxSelectedCount(flatPosition) < maxCount.toInt()){
                        if (childDataItem.isSet == "1") {
                            childDataItem.isSet = "0"
                        } else {
                            childDataItem.isSet = "1"
                        }
                    }else{
                        Logger.d("Maximum count selected")
                    }
                }
                notifyItemChanged(flatPosition)




            }


        }

    }

    fun checkMaxSelectedCount(position: Int): Int {
        var countTemp = 0
        for(itemData in listOfValue){
            if(itemData.isSet == "1"){
                countTemp++
            }
        }
        return countTemp
    }

}