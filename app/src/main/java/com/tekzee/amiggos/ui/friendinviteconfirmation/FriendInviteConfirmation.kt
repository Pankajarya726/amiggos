package com.tekzee.amiggos.ui.friendinviteconfirmation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.databinding.FriendInviteConfirmationBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen

class FriendInviteConfirmation:BaseActivity() {

    private var binding: FriendInviteConfirmationBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.friend_invite_confirmation)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding!!.btnDone.setOnClickListener {
            startActivity(Intent(this,AHomeScreen::class.java))
            finishAffinity()
        }
    }

    override fun validateError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {

    }
}