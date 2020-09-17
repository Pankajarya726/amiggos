package com.tekzee.amiggoss.ui.mylifestyle

import `in`.madapps.placesautocomplete.PlaceAPI
import `in`.madapps.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import `in`.madapps.placesautocomplete.listener.OnPlacesDetailsListener
import `in`.madapps.placesautocomplete.model.Place
import `in`.madapps.placesautocomplete.model.PlaceDetails
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.MyLifestyleFragmentBinding
import com.tekzee.amiggoss.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggoss.ui.mylifestyle.custom.CustomErrorItem
import com.tekzee.amiggoss.ui.mylifestyle.custom.CustomLoadingItem
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.constant.ConstantLib
import ru.alexbykov.nopaginate.paginate.NoPaginate


class AMyLifeStyle : BaseFragment(), Listener, MyLifestylePresenter.MyLifestylePresenterMainView {

    private var placesApi: PlaceAPI? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: MyLifestyleFragmentBinding? = null
    private var noPaginate: NoPaginate? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var longitude: String? = " 0.0"
    private var latitude: String? = "0.0"
    private var presenterImplementation: MyLifestylePresenterImplementation? = null

    companion object {

        fun newInstance(): AMyLifeStyle {
            return AMyLifeStyle()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.my_lifestyle_fragment, container, false)
        sharedPreference = SharedPreference(context!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterImplementation = MyLifestylePresenterImplementation(this, context!!)
        setupLanguage()
        setupView()
        checkLocationPermissions()
        setupPlaceAutoComplete()

    }



    private fun setupPlaceAutoComplete() {
        placesApi =
            PlaceAPI.Builder().apiKey(resources.getString(R.string.placeapi)).build(activity!!)
        binding!!.autoCompleteEditText.setAdapter(
            PlacesAutoCompleteAdapter(activity!!, placesApi!!)
        )
        binding!!.autoCompleteEditText.setOnClickListener{
            binding!!.autoCompleteEditText.setSelectAllOnFocus(true)
            binding!!.autoCompleteEditText.selectAll()
        }

        binding!!.autoCompleteEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                hideKeyboard()
                val place = parent.getItemAtPosition(position) as Place
                binding!!.autoCompleteEditText.setText(place.description)
                callPlaceApiDetails(place.id)
            }


    }

    private fun callPlaceApiDetails(id: String) {
        placesApi!!.fetchPlaceDetails(id, object : OnPlacesDetailsListener {
            override fun onError(errorMessage: String) {
                Log.d("error--->", errorMessage)
            }

            override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                latitude = placeDetails.lat.toString()
                longitude = placeDetails.lng.toString()
                activity!!.runOnUiThread(Runnable {
                    hideKeyboard()
                    callMyLifestyle()
                })

            }

        })
    }

    private fun checkLocationPermissions() {
        showProgressbar()
        easyWayLocation = EasyWayLocation(activity, false, this)
        easyWayLocation!!.startLocation()
    }


    private fun setupLanguage() {
        binding!!.searchTxt.text = languageData!!.PWhatWouldYouLikeToDo
        binding!!.autoCompleteEditText.hint = languageData!!.PCurrentLocation

    }

    private fun callMyLifestyle() {

    }

    private fun setupView() {

    }

    override fun onMyLifeStyleSuccess(responseData: HomeApiResponse?) {
        TODO("Not yet implemented")
    }

    override fun onMyLifestyleFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun validateError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        easyWayLocation!!.endUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterImplementation!!.onStop()
        if (noPaginate != null)
            noPaginate!!.unbind()
    }

    //location request
    override fun locationCancelled() {
        hideProgressbar()
        Logger.d("Location cancelled called")
    }

    override fun locationOn() {
        Logger.d("Location on called")
    }

    override fun currentLocation(location: Location?) {
        hideProgressbar()
        latitude = location!!.latitude.toString()
        longitude = location.longitude.toString()
        easyWayLocation!!.endUpdates()
        Logger.d("Location updates--->" + latitude + "----" + longitude)
        callMyLifestyle()
        //callPagination()

    }

    private fun callPagination() {
        noPaginate = NoPaginate.with(binding!!.mylifestyleRecyclerview)
            .setOnLoadMoreListener {
                callMyLifestyle()
            }
            .setLoadingTriggerThreshold(5) //0 by default
            .setCustomErrorItem(CustomErrorItem())
            .setCustomLoadingItem(CustomLoadingItem())
            .build()

    }

}