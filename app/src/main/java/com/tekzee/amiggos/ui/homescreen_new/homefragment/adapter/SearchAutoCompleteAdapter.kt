package com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class SearchAutoCompleteAdapter(
    mContext: Context,
    private val input: JsonObject,
    private val sharedPreference: SharedPreference
) :
    ArrayAdapter<HomeResponse.Data.Venue>(mContext, R.layout.list_row),
    Filterable {

    var resultList: ArrayList<HomeResponse.Data.Venue>? = ArrayList()

    override fun getCount(): Int {
        return when {
            resultList.isNullOrEmpty() -> 0
            else -> resultList?.size!!
        }
    }

    override fun getItem(position: Int): HomeResponse.Data.Venue? {
        return when {
            resultList.isNullOrEmpty() -> null
            else -> resultList!![position]
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_row, parent, false)
            viewHolder.textView = view.findViewById(R.id.text_title) as TextView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val place = resultList!![position]
        bindView(viewHolder, place, position)
        return view!!
    }

    private fun bindView(
        viewHolder: ViewHolder,
        place: HomeResponse.Data.Venue,
        position: Int
    ) {
        if (!resultList.isNullOrEmpty()) {
            if (position != resultList!!.size - 1) {
                viewHolder.textView?.text = place.name
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    resultList = autocompleteApi(constraint.toString())
                    filterResults.values = resultList
                    filterResults.count = resultList!!.size
                }
                return filterResults
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun autocompleteApi(searchString: String): ArrayList<HomeResponse.Data.Venue>? {
        input.addProperty("search","")
        val listOfData = ArrayList<HomeResponse.Data.Venue>()
        ApiClient.instance.doCallHomeApi(input, Utility.createHeaders(sharedPreference))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response: Response<HomeResponse> ->
              when (response.code()) {
                200 -> {
                  val responseData: HomeResponse = response.body()!!
                  if (responseData.status) {
                    listOfData.addAll(responseData.data.venue)
                  }
                }
              }
            }
      return listOfData
    }

    internal class ViewHolder {
        var textView: TextView? = null
    }
}
