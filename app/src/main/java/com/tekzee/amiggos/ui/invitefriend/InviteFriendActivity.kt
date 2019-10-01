package com.tekzee.amiggos.ui.invitefriend

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.InviteFriendActivityBinding
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class InviteFriendActivity : BaseActivity(), InviteFriendActivityPresenter.InviteFriendMainView {
    
    lateinit var binding : InviteFriendActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var inviteFriendPresenterImplementation: InviteFriendPresenterImplementation? = null

    companion object {
        private const val CONTACT_PICKER_REQUEST = 2
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.invite_friend_activity)
        inviteFriendPresenterImplementation = InviteFriendPresenterImplementation(this,this)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViewNames()
        setupToolBar()
        setupClickListeners()

    }

    private fun setupClickListeners() {
        binding.btnInviteFriend.setOnClickListener{
          checkFriendRequest()
        }
    }

    private fun checkFriendRequest() {
        askPermission(Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS) {
            MultiContactPicker.Builder(this) //Activity/fragment context
                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .searchIconColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                ) //Option - default: White
                .showTrack(true) //Optional - default: true
                .handleColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                ) //Optional - default: Azure Blue
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                ) //Optional - default: Azure Blue
                .bubbleColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                ) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                ) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST)
        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(this)
                    .setMessage("Need Your contact permission to read contact")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show()
            }
        }
    }

    private fun callUpdateInviteFriendCount(size: Int) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("invite_friend_count", size.toString())
        inviteFriendPresenterImplementation!!.doUpdateFriendCount(
            input,
            Utility.createHeaders(sharedPreference)
        )

    }



    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klInviteTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupViewNames() {
        binding.txtITitile.text = languageData!!.klINViteFRiends
        binding.txtMessage.text = languageData!!.klnewInvitemsg1 +" "+ sharedPreference!!.getValueInt(ConstantLib.INVITE_FRIEND).toString()
        binding.txtMessageTwo.text = languageData!!.klnewInvitemsg2
        binding.btnInviteFriend.text = languageData!!.klINViteFRiends
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val results: ArrayList<ContactResult> = MultiContactPicker.obtainResult(data)
                if (results.size < sharedPreference!!.getValueInt(ConstantLib.INVITE_FRIEND)) {
                    val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    pDialog.setTitle(
                        "Please select mimimum " + sharedPreference!!.getValueInt(
                            ConstantLib.INVITE_FRIEND
                        ) + " Friends"
                    )
                    pDialog.setCancelable(false)
                    pDialog.setCancelButton(languageData!!.klCancel) {
                        pDialog.dismiss()
                    }
                    pDialog.setConfirmButton(languageData!!.klOk) {
                        pDialog.dismiss()
                        checkFriendRequest()
                    }
                    pDialog.show()
                }else{

                    Observable.fromIterable(results).subscribe(object : Observer<ContactResult> {
                        override fun onComplete() {
                            callUpdateInviteFriendCount(results.size)
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: ContactResult) {
                            sendMessageToNumber(t.phoneNumbers[0].number)
                        }

                        override fun onError(e: Throwable) {
                            Logger.d("onError")
                        }

                    })
                }



            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    private fun sendMessageToNumber(phoneNumbers: String) {

        try {
            val sms: SmsManager = SmsManager.getDefault()
            sms.sendTextMessage(
                phoneNumbers,
                null,
                "Hi Himanshu This is testing message from amiggos",
                null,
                null
            ); // adding
        } catch (e: Exception) {
            e.printStackTrace();
        }

    }


    override fun validateError(message: String) {

    }

    override fun onUpdateFriendCountSuccess(responseData: UpdateFriendCountResponse?) {
        sharedPreference!!.save(ConstantLib.INVITE_FRIEND,0)
       finish()
    }

}