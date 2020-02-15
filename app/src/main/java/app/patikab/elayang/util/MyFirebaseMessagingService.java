package app.patikab.elayang.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import app.patikab.elayang.NotificationActivity;
import app.patikab.elayang.R;

/**
 * Created by paymin on 23/08/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String channelId = "CHANNEL";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        channelId = getString(R.string.default_notification_channel_id);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            Log.i("response notif", "isNull");
        }

//        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        Log.i("response notif", new Gson().toJson(remoteMessage.getNotification()));
        Log.i("response notif data", new Gson().toJson(remoteMessage.getData()));

    }

    private void sendNotification(String title, String body) {

        Intent resultIntent = new Intent(this, NotificationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri customSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.the_little_dwarf);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel(mNotifyManager);

        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_laptop);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle().bigPicture(image);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.lg_pati_round)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent)
//                .setStyle(style)
                .setAutoCancel(true)
                .setSound(customSoundUri);
        mNotifyManager.notify(0, mBuilder.build());

        Log.i("notif", "created builder");
    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String name = "Broadcast Notification";
        String description = "Notifications for download status";
        Uri customSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.the_little_dwarf);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setSound(customSoundUri, attributes);
        mChannel.setVibrationPattern(new long[]{100, 200, 100, 300, 100});
        notificationManager.createNotificationChannel(mChannel);

        Log.i("notif", "created channel");
    }
}