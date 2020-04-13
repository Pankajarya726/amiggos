package com.tekzee.amiggos.ui.friendinviteconfirmation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.FriendInviteConfirmationBinding
import com.tekzee.amiggos.ui.friendlist.FriendListActivity
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib

class FriendInviteConfirmation : BaseActivity() {

    private var binding: FriendInviteConfirmationBinding? = null
    private var bookingId :String =""
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.friend_invite_confirmation)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupClickListener()
        setupUi()
    }

    private fun setupUi() {
        Glide.with(applicationContext).load(sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE)).into(binding!!.imgUser)
        if (intent.getStringExtra(ConstantLib.FROM).equals("CHECKOUTACTIVITY", true)) {
            binding!!.btnDone.text = "INVITE FRIENDS"
            binding!!.txtSentMessage.text ="You have successfully booked. Invite friends to your party by clicking the button below."
            bookingId =intent.getStringExtra(ConstantLib.BOOKING_ID)
        } else {
            binding!!.btnDone.text = "DONE"
            binding!!.txtSentMessage.text ="Congratulations you have successfully invited your friends."
            bookingId =""
        }
    }

    private fun setupClickListener() {
        binding!!.btnDone.setOnClickListener {
            if (intent.getStringExtra(ConstantLib.FROM).equals("CHECKOUTACTIVITY", true)) {
                val intent = Intent(applicationContext, FriendListActivity::class.java)
                intent.putExtra(ConstantLib.BOOKING_ID, bookingId)
                startActivity(intent)
            } else {
                startActivity(Intent(this, AHomeScreen::class.java))
                finishAffinity()
            }

        }
    }

    override fun validateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {

    }
}