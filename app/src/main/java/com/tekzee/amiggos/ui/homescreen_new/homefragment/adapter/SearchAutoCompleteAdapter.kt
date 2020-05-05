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
    ArrayAdapter<HomeApiResponse.Data.NearestClub>(mContext, R.layout.list_row),
    Filterable {

    var resultList: ArrayList<HomeApiResponse.Data.NearestClub>? = ArrayList()

    override fun getCount(): Int {
        return when {
            resultList.isNullOrEmpty() -> 0
            else -> resultList?.size!!
        }
    }

    override fun getItem(position: Int): HomeApiResponse.Data.NearestClub? {
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
        place: HomeApiResponse.Data.NearestClub,
        position: Int
    ) {
        if (!resultList.isNullOrEmpty()) {
            if (position != resultList!!.size - 1) {
                viewHolder.textView?.text = place.clubName
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
    private fun autocompleteApi(searchString: String): ArrayList<HomeApiResponse.Data.NearestClub>? {
        input.addProperty("search","")
        val listOfData = ArrayList<HomeApiResponse.Data.NearestClub>()
        ApiClient.instance.doCallHomeApi(input, Utility.createHeaders(sharedPreference))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response: Response<HomeApiResponse> ->
              when (response.code()) {
                200 -> {
                  val responseData: HomeApiResponse = response.body()!!
                  if (responseData.status) {
                    listOfData.addAll(responseData.data.nearestClubs)
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
