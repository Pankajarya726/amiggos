package com.tekzee.amiggos.ui.newpreferences.avenuetypefragment

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.VenueTypeFragmentBinding
import com.tekzee.amiggos.ui.newpreferences.avenuetypefragment.adapter.VenueTypeAdapter
import com.tekzee.amiggos.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib

class AVenueTypeFragment : BaseFragment(), AVenueTypePresenter.AVenueTypePresenterMainView {

    private var binding: VenueTypeFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aVenueTypeImplementation: AVenueTypeImplementation? = null
    private var adapter: VenueTypeAdapter? = null
    private var data = ArrayList<AVenueTypeResponse.Data.VenueType>()


    companion object {

        fun newInstance(): AVenueTypeFragment {
            return AVenueTypeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.venue_type_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aVenueTypeImplementation = AVenueTypeImplementation(this, context!!)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        callVenueTypeApi()
    }

    private fun setupRecyclerView() {
        binding!!.venueTypeRecyclerview.setHasFixedSize(true)
        binding!!.venueTypeRecyclerview.layoutManager = GridLayoutManager(context, 3)
        adapter = VenueTypeAdapter(data,getWidth())
        binding!!.venueTypeRecyclerview.adapter = adapter
    }

    private fun callVenueTypeApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        aVenueTypeImplementation!!.doCallVenueTypeApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun onVenueTypeSuccess(responseData: AVenueTypeResponse?) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData!!.data.venueType)
        adapter!!.notifyDataSetChanged()
    }

    override fun onVenueTypeFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    fun getWidth(): Int {
        val display: Display = activity!!.windowManager.defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        val width = size.x
        return width/2
    }
}