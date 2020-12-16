package com.tekzee.amiggos.services

//  notification_key = 5 (show accept reject for booking invitaion)
//  notification_key = 200 (Show only message for accepted booking)
//  notification_key = 201 (Show only message for rejected booking)

//  notification_key = 2 (Friend Request)
//  notification_key = 3 (Friend Request accepted)
//  notification_key = 202 (Friend Request rejected)

//  notification_key = 4 (our memory join reject notification)
//  notification_key = 204 (reject our memory)

//  notification_key = 1 (approved by brand or venue)


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
import com.tekzee.amiggos.broadcasts.NotificationBroadcastReceiver
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.chat.myfriendchatlist.MyFriendChatActivity
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.message.MessageActivity
import com.tekzee.amiggos.ui.notification_new.ANotification
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
            Log.d(TAG, "Message Notification Body: $jsonData")
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
                goToShowMemory(title,body)
            }
            "2" -> {
                gotoFriendRequest(title, body, userid)
            }
            "3" -> {
                gotoRealFriend(title, body, userid)
            }
            "4" -> {
                gotoCreateMemoryInvitation(title, body, senderid, storyid)
            }
            "5" -> {
                gotoSendPartyInvitation(title, body, bookingid)
            }
            "6" -> {
                createDefaultNotification(title, body)
            }
            "7" -> {
                createDefaultNotification(title, body)
            }
            "8" -> {
                createChatNotification(title, body)
            }
            "9" -> {
                gotoSendPartyInvitation(title, body, userid)
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
            "200" -> {
                createDefaultNotification(title, body)
            }
            "201" -> {
                createDefaultNotification(title, body)
            }
            "202" -> {
                goToNotification(title, body)
            }
            "203" -> {
                createDefaultNotification(title, body)
            }
            "204" -> {
                createDefaultNotification(title, body)
            }
            else -> { // Note the block
                createDefaultNotification(title, body)
            }

        }


    }

    private fun goToNotification(title: String?, body: String?) {
        createNotificationChannel()
        playSound()
        val intent = Intent(this, ANotification::class.java)
        intent.putExtra(ConstantLib.SUB_TAB, 1)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
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

    private fun createChatNotification(title: String, body: String) {
        createNotificationChannel()
        playSound()
        val intent = Intent(this, MessageActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
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
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
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

    private fun gotoCreateMemoryInvitation(
        title: String,
        body: String,
        senderid: String,
        ourstoryid: String
    ) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.CREATE_MEMORY_INVITATIONS.action
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            0
        )

        val acceptIntent = Intent(
            this,
            CameraActivity::class.java
        )
        acceptIntent.action = FriendsAction.ACCEPT_CREATE_MEMORY_INVITATIONS.action
        acceptIntent.putExtra(ConstantLib.SENDER_ID, senderid)
        acceptIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)
        acceptIntent.putExtra(ConstantLib.FROM_ACTIVITY, ConstantLib.OURSTORYINVITE)
        acceptIntent.putExtra(ConstantLib.SENDER_ID, senderid)
        acceptIntent.putExtra(ConstantLib.OURSTORYID, ourstoryid)



        val pendingAcceptIntent = PendingIntent.getActivity(
            this,
            0,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val rejectIntent = Intent(
            this,
            NotificationBroadcastReceiver::class.java
        )
        rejectIntent.action = FriendsAction.REJECT_CREATE_MEMORY_INVITATIONS.action
        rejectIntent.putExtra(ConstantLib.SENDER_ID, senderid)
        rejectIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)
        rejectIntent.putExtra(ConstantLib.OURSTORYID, ourstoryid)

        val pendingRejectIntentIntent = PendingIntent.getBroadcast(
            this,
            0,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val friendRequestBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(body)
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(contentIntent)
            .addAction(android.R.drawable.ic_menu_gallery, ConstantLib.JOIN, pendingAcceptIntent)
            .addAction(
                android.R.drawable.ic_menu_gallery,
                ConstantLib.REJECT,
                pendingRejectIntentIntent
            )
            .setOnlyAlertOnce(true)
            .setAutoCancel(false).build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, friendRequestBuilder)
        }
    }

    private fun gotoSendPartyInvitation(title: String, body: String, bookingid: String) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.PARTY_INVITATIONS.action
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            0
        )

        val acceptIntent = Intent(
            this,
            NotificationBroadcastReceiver::class.java
        )
        acceptIntent.action = FriendsAction.ACCEPT_PARTY_INVITATIONS.action
        acceptIntent.putExtra(ConstantLib.BOOKING_ID, bookingid)
        acceptIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)

        val pendingAcceptIntent = PendingIntent.getBroadcast(
            this,
            0,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val rejectIntent = Intent(
            this,
            NotificationBroadcastReceiver::class.java
        )
        rejectIntent.action = FriendsAction.REJECT_PARTY_INVITATIONS.action
        rejectIntent.putExtra(ConstantLib.BOOKING_ID, bookingid)
        rejectIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)

        val pendingRejectIntentIntent = PendingIntent.getBroadcast(
            this,
            0,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val friendRequestBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(body)
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(contentIntent)
            .addAction(android.R.drawable.ic_menu_gallery, ConstantLib.ACCEPT, pendingAcceptIntent)
            .addAction(
                android.R.drawable.ic_menu_gallery,
                ConstantLib.REJECT,
                pendingRejectIntentIntent
            )
            .setOnlyAlertOnce(true)
            .setAutoCancel(false).build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, friendRequestBuilder)
        }
    }

//    private fun gotoFriendRequestAccept(title: String, body: String) {
//        createNotificationChannel()
//        playSound()
//
//        val intent = Intent(this, RealFriendsActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle(body)
//            .setContentText(title)
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(title)
//            )
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//
//            .setAutoCancel(true)
//        with(NotificationManagerCompat.from(this)) {
//            notify(System.currentTimeMillis().toInt(), builder.build())
//        }
//    }

    private fun gotoFriendRequest(title: String, body: String, muserid: String) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.SHOW_FRIEND_REQUEST.action
            putExtra(packageName, notificationId)
            putExtra(ConstantLib.FRIEND_ID, muserid)
            putExtra(ConstantLib.FROM, ConstantLib.FRIENDREQUEST)
            putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(ConstantLib.SUB_TAB, 2)
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            0
        )

        val acceptIntent = Intent(
            this,
            NotificationBroadcastReceiver::class.java
        )
        acceptIntent.action = FriendsAction.ACCEPT.action
        acceptIntent.putExtra(ConstantLib.FRIEND_ID, muserid)
        acceptIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)

        val pendingAcceptIntent = PendingIntent.getBroadcast(
            this,
            0,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val rejectIntent = Intent(
            this,
            NotificationBroadcastReceiver::class.java
        )
        rejectIntent.action = FriendsAction.REJECT.action
        rejectIntent.putExtra(ConstantLib.FRIEND_ID, muserid)
        rejectIntent.putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)

        val pendingRejectIntentIntent = PendingIntent.getBroadcast(
            this,
            0,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val friendRequestBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(body)
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(contentIntent)
            .addAction(android.R.drawable.ic_menu_gallery, ConstantLib.ACCEPT, pendingAcceptIntent)
            .addAction(
                android.R.drawable.ic_menu_gallery,
                ConstantLib.REJECT,
                pendingRejectIntentIntent
            )
            .setOnlyAlertOnce(true)
            .setAutoCancel(true).build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, friendRequestBuilder)
        }

    }

    fun getLaunchIntent(notificationId: Int, context: Context?): PendingIntent? {
        val intent = Intent(context, AHomeScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("notificationId", notificationId)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }


    private fun gotoRealFriend(title: String, body: String, muserid: String) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.SHOW_FRIENDS.action
            putExtra(packageName, notificationId)
            putExtra(ConstantLib.FRIEND_ID, muserid)
            putExtra(ConstantLib.FROM, ConstantLib.FRIENDREQUEST)
            putExtra(ConstantLib.EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(ConstantLib.SUB_TAB, 1)
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

    private fun goToShowMemory(title: String, body: String) {
        createNotificationChannel()
        playSound()

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(this, AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.SHOW_MY_MEMORY.action
            putExtra(packageName, notificationId)

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
            val importance = NotificationManager.IMPORTANCE_HIGH

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