package kz.growit.smartservice.Utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Date;

import kz.growit.smartservice.MainActivity;
import kz.growit.smartservice.R;


/**
 * Created by Талгат on 19.06.2015.
 */
public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 100500;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public GCMIntentService() {
        super("GCMIntentService");
    }

    public static final String TAG = "GCM SmartService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(new Date(), "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(new Date(), "");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String myMsg = extras.getString("message");
                sendNotification(new Date(), myMsg);
                Log.i(TAG, "Received: " + myMsg);
            }
        }
        GCMBroadcastReciever.completeWakefulIntent(intent);
    }

    private void sendNotification(Date date, String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resetNotifs = new Intent(this, MainActivity.class);
        resetNotifs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                resetNotifs, 0);

        //NotificationCompat.InboxStyle myStyle = new NotificationCompat.InboxStyle();

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ss_symbol)
                .setContentTitle(msg)
                .setTicker(msg)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup("SmartService.KZ")
                .setContentTitle("SmartService.KZ")
                .setContentText(msg)
                .setWhen(date.getTime())
                .setAutoCancel(true);

        builder.setContentIntent(contentIntent);


        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}