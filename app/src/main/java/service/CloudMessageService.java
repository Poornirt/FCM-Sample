package service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import jdo.Books;
import sample.activity.DescriptionActivity;
import sample.activity.MainActivity;

import static constants.Constants.ACTION_PERFORMED;
import static constants.Constants.BOOK_OBJECT;
import static constants.Constants.UPDATE_UI;

public class CloudMessageService extends FirebaseMessagingService {

    private String TAG = "FirebaseMessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        if(!MainActivity.isActive && !DescriptionActivity.isActive){
            triggerNotifcation(remoteMessage.getData().toString());
        }
        Books bookJdo = new Books();
        bookJdo.setmId(remoteMessage.getData().get("ID"));
        bookJdo.setmTitle(remoteMessage.getData().get("Title"));
        bookJdo.setmDescription(remoteMessage.getData().get("Description"));
        bookJdo.setmPublishDate(remoteMessage.getData().get("PublishDate"));
        bookJdo.setmExcerpt(remoteMessage.getData().get("Excerpt"));
        bookJdo.setmPageCount(remoteMessage.getData().get("PageCount"));

        Intent intent = new Intent(UPDATE_UI);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BOOK_OBJECT, bookJdo);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().get("RequestMethod"));
        bundle.putString(ACTION_PERFORMED, remoteMessage.getData().get("RequestMethod"));
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void triggerNotifcation(String messageBody) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        Notification.Builder builder;
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.setLightColor(Color.GREEN);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new Notification.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle("Sample")  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(messageBody)  // required
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("sample");
        } else {

            builder = new Notification.Builder(this);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle("Sample")                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(messageBody)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("sample")
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: " + token);
    }
}
