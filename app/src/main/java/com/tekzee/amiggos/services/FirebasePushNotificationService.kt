package com.tekzee.amiggos.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.chat.myfriendchatlist.MyFriendChatActivity
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.realfriends.RealFriendsActivity
import com.tekzee.amiggos.util.SharedPreference
import org.json.JSONObject


class FirebasePushNotificationService : FirebaseMessagingService() {

    private val TAG: String? = FirebasePushNotificationService::class.simpleName
    private var alarmSound: Uri? = null
    private var sharedPreferences: SharedPreference? = null
    val CHANNEL_ID = "AMIGGOS"
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = SharedPreference(this)
        alarmSound =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val jsonData = JSONObject(remoteMessage.data as Map<String, String>)
            Log.d(TAG, "Message Notification Body: " + jsonData);
            handleNotifications(remoteMessage)
        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "title : " + remoteMessage.notification!!.title)
            Log.d(TAG, "body : " + remoteMessage.notification!!.body)
            Log.d(TAG, "body : " + remoteMessage.notification!!)
        }
    }

    private fun handleNotifications(remoteMessage: RemoteMessage?) {

        val jsonData = JSONObject(remoteMessage!!.data as Map<String, String>)


        var userid = if (jsonData.has("userid")) {
            jsonData.getString("userid")
        } else {
            ""
        }


        var friendid = if (jsonData.has("friend_id")) {
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
                gotoFriendRequest(title, body, userid)
            }
            "3" -> {
                gotoFriendRequest(title, body, userid)
            }
            "4" -> {
                createDefaultNotification(title, body)
            }
            "5" -> {
                gotoSendPartyInvitation(title, body)
            }
            "6" -> {
                createDefaultNotification(title, body)
            }
            "7" -> {
                createDefaultNotification(title, body)
            }
            "8" -> {
                createDefaultNotification(title, body)
            }
            "9" -> {
                goToAttachidActivity(title, body)
            }
            "10" -> {
                createDefaultNotification(title, body)
            }
            "22" -> {
                newMemory(title, body)
            }
            "100" -> {
                showChatNotification(title, body)
            }
            else -> { // Note the block
                createDefaultNotification(title, body)
            }

        }


    }

    private fun goToAttachidActivity(title: String?, body: String?) {

        createNotificationChannel()
        playSound()

        val intent = Intent(this, AttachIdActivity::class.java)/*.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }*/

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

    private fun createDefaultNotification(title: String, body: String) {
        createNotificationChannel()
        playSound()
        val intent = Intent(this, AHomeScreen::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun newMemory(title: String, body: String) {
        createNotificationChannel()
        playSound()

        val intent = Intent(this, AHomeScreen::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun showChatNotification(title: String, body: String) {
        createNotificationChannel()
        playSound()

        val intent = Intent(this, MyFriendChatActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun playSound() {
        try {

            val mediaPlayer: MediaPlayer? = MediaPlayer.create(applicationContext, R.raw.coin)
            mediaPlayer?.start() // no need to call prepare(); create() does that for you
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun gotoBookingScreen() {

    }

    private fun gotoPartyDetailsScreen() {

    }

    private fun gotoDocRejected(userid: String, friendid: String) {

    }

    private fun gotoSendPartyInvitation(title: String, body: String) {
        createNotificationChannel()
        playSound()

        val intent = Intent(this, PartyDetailsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun gotoFriendRequestAccept(title: String, body: String) {
        createNotificationChannel()
        playSound()

        val intent = Intent(this, RealFriendsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun gotoFriendRequest(title: String, body: String, muserid: String) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.CLICK.action
            putExtra(packageName, notificationId)
            putExtra(ConstantLib.FRIEND_ID, muserid)
            putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(ConstantLib.SUB_TAB, 2)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setShowWhen(true)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

    }

    private fun gotoDocVerification(uid: Any, fid: Any, title: String?, body: String?) {

        createNotificationChannel()
        playSound()

        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(body)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(title)
            )
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
        //   Logger.d("Refreshed token: " + token!!)
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