package com.tekzee.amiggos.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.agegroup.AgeGroupActivity
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib
import org.json.JSONObject


class FirebasePushNotificationService : FirebaseMessagingService() {

    private var sharedPreferences: SharedPreference? = null
    val CHANNEL_ID = "AMIGGOS"
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = SharedPreference(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Logger.d("From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Logger.d("Message data payload: " + remoteMessage.data)
            handleNotifications(remoteMessage)
        }
    }
    private fun handleNotifications(remoteMessage: RemoteMessage?) {

        val jsonData = JSONObject(remoteMessage!!.data as Map<String, String>)
        val userid = if (jsonData.has("user_id")) {
            jsonData.getString("user_id")
        } else {
            ""
        }


        val friendid = if (jsonData.has("friend_id")) {
            jsonData.getString("friend_id")
        } else {
            ""
        }

        val notificationId = if (jsonData.has("notification_id")) {
            jsonData.getString("notification_id")
        } else {
            ""
        }
        var notiKey = if (jsonData.has("notification_key")) {
            jsonData.getString("notification_key")
        } else {
            ""
        }
        var bookingid = if (jsonData.has("booking_id")) {
            jsonData.getString("booking_id")
        } else {
            ""
        }
        var creatorid = if (jsonData.has("creator_id")) {
            jsonData.getString("creator_id")
        } else {
            ""
        }
        var storyid = if (jsonData.has("our_story_id")) {
            jsonData.getString("our_story_id")
        } else {
            ""
        }
        var senderid = if (jsonData.has("sender_id")) {
            jsonData.getString("sender_id")
        } else {
            ""
        }
        val invitefrnd = if (jsonData.has("invite_friend_count")) {
            jsonData.getString("invite_friend_count").toInt()
        } else {
            0
        }
        val rejectionMessage = if (jsonData.has("rejectReason")) {
            jsonData.getString("rejectReason")
        } else {
            ""
        }
        val userAgeis1 = if (jsonData.has("age")) {
            jsonData.getString("age")
        } else {
            ""
        }
        val title = if (jsonData.has("title")) {
            jsonData.getString("title")
        } else {
            ""
        }
        var body = if (jsonData.has("body")) {
            jsonData.getString("body")
        } else {
            ""
        }

        sharedPreferences!!.save(ConstantLib.REJECTION_MESSAGE, rejectionMessage!!)

        /*    doc_verification  : 1
             doc_reject  : 6
             friend_request    : 2 ( friend_id (s) , user_id (r))
             friend_req_accept : 3 { friend_id (a), user_id (s) }
             our_memory_invite : 4 {our_story_id, creator_id , sender_id ,user_id}
             send_party_invite : 5 {booking_id , creator_id , user_id} */

        when (notiKey) {
            "0" -> {
                Logger.d("do nothing")
            }
            "1" -> {
                sharedPreferences!!.save(ConstantLib.INVITE_FRIEND, invitefrnd)
                sharedPreferences!!.save(ConstantLib.USER_AGE, userAgeis1!!)
                gotoDocVerification(userid!!, friendid!!, title, body)


            }
            "2" -> {
                gotoFriendRequest(remoteMessage)
            }
            "3" -> {
                gotoFriendRequestAccept(remoteMessage)
            }
            "4" -> {
//               SenderID = senderid
//               UserID = userid
//               OurStoryID = storyid
//               kUserDefault.setValue(storyid, forKey:StoriesKey.OurStoryID.rawValue)
//               kUserDefault.setValue(senderid, forKey: StoriesKey.NotifierUserId.rawValue)
//               requestToOurStorisBYID(getOurStoryID: storyid, getUserID: userid, getSenderID: senderid, notificationID:notificationId)
            }
            "5" -> {
                gotoSendPartyInvitation(bookingid!!, creatorid!!, userid!!)
            }
            "6" -> {
                gotoDocRejected(userid!!, friendid!!)
            }
            "7" -> {
//               print("Update Our Memory and Rejoin it")
//               SenderID = creatorid
//               OurStoryID = storyid
//               kUserDefault.setValue(storyid, forKey:StoriesKey.OurStoryID.rawValue)
//               kUserDefault.setValue(senderid, forKey: StoriesKey.NotifierUserId.rawValue)
//               requestToOurStorisBYID(getOurStoryID: storyid, getUserID: userid, getSenderID: senderid, notificationID:notificationId)
            }
            "8" -> {
                gotoPartyDetailsScreen()
            }
            "9" -> {
                print("Party Invitation reject")
            }
            "10" -> {
                gotoBookingScreen()
            }
            "60" ->{
                showChatNotification(title,body)
            }

        }


    }

    private fun showChatNotification(title: String, body: String) {
        createNotificationChannel()

        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun gotoBookingScreen() {

    }

    private fun gotoPartyDetailsScreen() {

    }

    private fun gotoDocRejected(userid: String, friendid: String) {

    }

    private fun gotoSendPartyInvitation(bookingid: String, creatorid: String, userid: String) {

    }

    private fun gotoFriendRequestAccept(remoteMessage: RemoteMessage) {

    }

    private fun gotoFriendRequest(remoteMessage: RemoteMessage) {

    }

    private fun gotoDocVerification(uid: Any, fid: Any, title: String?, body: String?) {

        createNotificationChannel()

        val intent = Intent(this, AgeGroupActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d("Refreshed token: " + token!!)
        sharedPreferences!!.save(ConstantLib.FCMTOKEN, token)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}