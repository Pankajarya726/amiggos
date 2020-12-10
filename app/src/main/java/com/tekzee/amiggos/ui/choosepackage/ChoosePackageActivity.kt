package com.tekzee.amiggos.ui.choosepackage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ChoosePackageBinding
import com.tekzee.amiggos.stripe.CheckoutActivity
import com.tekzee.amiggos.ui.choosepackage.adapter.ChoosePackageAdapter
import com.tekzee.amiggos.ui.choosepackage.interfaces.ChoosePackageInterface
import com.tekzee.amiggos.ui.choosepackage.model.PackageBookResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageData
import com.tekzee.amiggos.ui.choosepackage.model.PackageResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib


class ChoosePackageActivity : BaseActivity(), ChoosePackagePresenter.ChoosePackageMainView {


    private var response: PackageResponse? = null
    private lateinit var adapter: ChoosePackageAdapter
    private lateinit var binding: ChoosePackageBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var choosePackagePresenterImplementation: ChoosePackagePresenterImplementation? = null
    private var data = ArrayList<PackageData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.choose_package)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        choosePackagePresenterImplementation = ChoosePackagePresenterImplementation(this, this)
        setupToolBar()
        callChoosePackageApi()
        setupRecyclerView()
        initView()


    }

    private fun initView() {

        binding.headerTitle.text = languageData!!.kllblChoosPkgTitle


    }
//    0: No document
//    1: Document uploaded
//    2: Document Approved

    private fun setupRecyclerView() {
        binding.chooseRecyclerview.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.chooseRecyclerview.layoutManager = layoutManager
        adapter = ChoosePackageAdapter(data, languageData!!, object : ChoosePackageInterface {
            override fun onClickWeek(packageData: PackageData) {
//                if(response!!.data.userFirstPackage.equals("1")){
//                    var intent: Intent?= null
//                    when(response!!.data.userDocumentStatus){
//                        "0"->{
//                             intent = Intent(applicationContext, AttachIdActivity::class.java)
//                            startActivity(intent)
//                        }
//                        "1"->{
//                            intent = Intent(applicationContext, StatusViewActivity::class.java)
//                            startActivity(intent)
//                        }
//                        "2"->{
//                            val pDialog =
//                                SweetAlertDialog(this@ChoosePackageActivity, SweetAlertDialog.WARNING_TYPE)
//                            pDialog.titleText = languageData!!.klAlertmessageBooking
//                            pDialog.setCancelable(false)
//                            pDialog.setCancelButton(languageData!!.klCancel) {
//                                pDialog.dismiss()
//                            }
//                            pDialog.setConfirmButton(languageData!!.klOk) {
//                                pDialog.dismiss()
//                                callBookPackage(packageData.packageId, packageData.clubId)
//                            }
//                            pDialog.show()
//                        }
//
//                    }
//
//                }else
//                {
                    val pDialog =
                        SweetAlertDialog(this@ChoosePackageActivity, SweetAlertDialog.WARNING_TYPE)
                    pDialog.titleText = languageData!!.klAlertmessageBooking
                    pDialog.setCancelable(false)
                    pDialog.setCancelButton(languageData!!.klCancel) {
                        pDialog.dismiss()
                    }
                    pDialog.setConfirmButton(languageData!!.klOk) {
                        pDialog.dismiss()
                        callBookPackage(packageData.packageId, packageData.clubId)
                    }
                    pDialog.show()
//                }

            }
        })
        binding.chooseRecyclerview.adapter = adapter
    }

    private fun callBookPackage(packageId: Int, clubId: Int) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", clubId.toString())
        input.addProperty("package_id", packageId.toString())
        input.addProperty("booking_date", intent.getStringExtra(ConstantLib.PACKAGE_DATE))

        choosePackagePresenterImplementation!!.doBookPackage(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }


    private fun callChoosePackageApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", intent.getStringExtra(ConstantLib.CLUB_ID))
        input.addProperty("package_date", intent.getStringExtra(ConstantLib.PACKAGE_DATE))
        choosePackagePresenterImplementation!!.doCallPackageApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }

    override fun onChoosePackageSuccess(responseData: PackageResponse?) {
        response = responseData
        data.addAll(responseData!!.data.packageData)
        adapter.notifyDataSetChanged()
    }

    override fun onBookPackageSuccess(responseData: PackageBookResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        val intent = Intent(this,CheckoutActivity::class.java)
        intent.putExtra(ConstantLib.BOOKING_ID,responseData.data.bookingId)
        intent.putExtra(ConstantLib.BOOKING_AMOUNt,responseData.data.amount)
        startActivity(intent)
        Animatoo.animateSlideLeft(this)

    }



}