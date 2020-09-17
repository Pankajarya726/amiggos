package com.tekzee.amiggoss.broadcasts

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.enums.FriendsAction
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class NotificationBroadcastReceiver : BroadcastReceiver() {
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var mContext: Context? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        sharedPreference = SharedPreference(mContext!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        NotificationManagerCompat.from(mContext!!).cancel(intent!!.getIntExtra(ConstantLib.EXTRA_NOTIFICATION_ID,0))
        if (intent.action == FriendsAction.ACCEPT.action) {
            callAcceptApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
        } else if (intent.action == FriendsAction.REJECT.action) {
            callRejectApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
        }
    }

    @SuppressLint("CheckResult")
    private fun callRejectApi(friendId: String?) {
        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", friendId)

        ApiClient.instance.doRejectInvitationApi(input, Utility.createHeaders(sharedPreference))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                callResponseCode(response)
            }

    }

    private fun callResponseCode(response: Response<CommonResponse>?) {
        when (response!!.code()) {
            200 -> {
                val responseData: CommonResponse? = response.body()
                if (responseData!!.status) {
                    Toast.makeText(mContext, responseData.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(mContext, responseData.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun callAcceptApi(friendId: String?) {

        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", friendId)
        ApiClient.instance.doAcceptInvitationApi(input, Utility.createHeaders(sharedPreference))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                callResponseCode(response)
            }, { error ->
                Toast.makeText(mContext, error.message.toString(), Toast.LENGTH_LONG).show()
            })


    }
}